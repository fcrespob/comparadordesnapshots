package es.mapfre.solvencia.utils.executor;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdjustableThreadPoolExecutor extends ThreadPoolExecutor {
	private static Logger LOG = LoggerFactory.getLogger(AdjustableThreadPoolExecutor.class);
	
	// 10 minutes. Time in miliseconds
	private static final long MINIMUM_TIME_BETWEEN_ADJUST = 30*60*1000;
	
	private static final double MAX_SYSTEM_LOAD_TO_SPAWN_THREAD = 1.0;
	Integer[] lastCheckedSize = new Integer[3];
	private long lastCheckedTime = System.currentTimeMillis();
	
	private int batchSize;
	
	private OperatingSystemMXBean systemMXBean = ManagementFactory.getOperatingSystemMXBean();

	private BlockingQueue<Runnable> innerQueue;
	private BlockingQueue<Runnable> intermediateQueue;
	
	private String memberName = "motor";

	public AdjustableThreadPoolExecutor(int size, int batchSize, String memberName) {
		super(1, 1,  0L, TimeUnit.MILLISECONDS,  new LinkedBlockingQueue<Runnable>(size));
		this.innerQueue = new LinkedBlockingQueue<Runnable>(size);
		this.batchSize = batchSize;
		this.intermediateQueue = new ArrayBlockingQueue<Runnable>(batchSize);
		this.memberName  = memberName;
	}

	public AdjustableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
		innerQueue = new LinkedBlockingQueue<Runnable>(workQueue.remainingCapacity());
		this.batchSize = 100;
		intermediateQueue = new ArrayBlockingQueue<Runnable>(batchSize);
	}

	public AdjustableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
		innerQueue = new LinkedBlockingQueue<Runnable>(workQueue.remainingCapacity());
		this.batchSize = 100;
		intermediateQueue = new ArrayBlockingQueue<Runnable>(batchSize);
	}

	public AdjustableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
		innerQueue = new LinkedBlockingQueue<Runnable>(workQueue.remainingCapacity());
		this.batchSize = 100;
		intermediateQueue = new ArrayBlockingQueue<Runnable>(batchSize);
	}

	public AdjustableThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
			BlockingQueue<Runnable> workQueue) {
		super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
		innerQueue = new LinkedBlockingQueue<Runnable>(workQueue.remainingCapacity());
		this.batchSize = 100;
		intermediateQueue = new ArrayBlockingQueue<Runnable>(batchSize);
	}
	
	@Override
	public void execute(Runnable paramRunnable) {
		if (paramRunnable == null) {
			throw new NullPointerException();
		}
		
		innerQueue.offer(paramRunnable);
	}
 

	/**
	 * Adjust the pool size based on cpu load
	 * @param adjustMaximum maximum number of threads. Set 0 for unlimited
	 */
	public void adjustPoolSize(int adjustMaximum) {
		int previousSize = this.getCorePoolSize();
		int calculatedSize = calculatePoolSize();
		int size = Math.min(adjustMaximum, calculatedSize);
		
		long now = System.currentTimeMillis();
		if (now - lastCheckedTime >= MINIMUM_TIME_BETWEEN_ADJUST) {
			if (size != previousSize) {
				// TODO Change only if last 3 times had to change value
				boolean increase = size > previousSize;
				boolean adjust = true;
				for (int i = 1; i < lastCheckedSize.length ; i++) {
					lastCheckedSize[i-1] = lastCheckedSize[i];
					adjust &= (lastCheckedSize[i] != null && (increase == (lastCheckedSize[i] > previousSize)));
				}
				
				if (adjust) {
					// Clear last checks
					for (int i = 1; i < lastCheckedSize.length ; i++) { lastCheckedSize[i] = null; } 
					
					// Adjust one by one
					size = (increase ? previousSize + 1 : previousSize - 1);
					LOG.info("[{}] Adjusting thread pool size from {} to {}...", memberName, previousSize, size);
					this.setCorePoolSize(size);
					this.setMaximumPoolSize(size);
					while (size < previousSize && super.getActiveCount() > 0 && innerQueue.size() > 0) {
						try {
							Thread.sleep(500);
						} catch (InterruptedException e) {
							break;
						}
					}
					LOG.info("[{}] Adjusted thread pool size from {} to {}", memberName, previousSize, size);
					
				} else {
					lastCheckedSize[lastCheckedSize.length-1] = size;
				}
			}
			lastCheckedTime = now;
		}
	}
	
	private int calculatePoolSize() {
		int maxThreads = Runtime.getRuntime().availableProcessors();
		// Usamos como maximo la mitad de cores
		if (maxThreads > 1) {
			maxThreads /= 2;
		}
		double systemLoad = 0.0;
		if (systemMXBean instanceof com.sun.management.OperatingSystemMXBean) {
			systemLoad = ((com.sun.management.OperatingSystemMXBean) systemMXBean).getSystemCpuLoad();
		} else {
			systemLoad = systemMXBean.getSystemLoadAverage();
		}
		
		if (systemLoad >= 0) {
			maxThreads *= (1 - systemLoad);
		}
		
		return Math.max(1, maxThreads);
	}

	public void runNextBatch() {
		if (super.getQueue().size() <= batchSize / 2) {
			innerQueue.drainTo(intermediateQueue, batchSize);
			int tasks = intermediateQueue.size();
			if (tasks > 0) {
				LOG.info("[{}] New batch of {} tasks to execute...", memberName, tasks);
				for (int i = 0; i < tasks; i++) {
					Runnable runnable = intermediateQueue.poll();
					if (runnable != null) {
						super.execute(runnable);
					}
				}
			}
		}
	}
}

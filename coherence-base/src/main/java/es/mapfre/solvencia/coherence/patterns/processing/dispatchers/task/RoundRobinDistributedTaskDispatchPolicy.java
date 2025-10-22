package es.mapfre.solvencia.coherence.patterns.processing.dispatchers.task;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.lang3.builder.CompareToBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.common.identifiers.Identifier;
import com.oracle.coherence.patterns.processing.SubmissionConfiguration;
import com.oracle.coherence.patterns.processing.dispatchers.task.TaskDispatchPolicy;
import com.oracle.coherence.patterns.processing.internal.task.TaskProcessorMediator;
import com.oracle.coherence.patterns.processing.internal.task.TaskProcessorMediatorKey;
import com.oracle.coherence.patterns.processing.internal.task.TaskProcessorStateEnum;
import com.oracle.coherence.patterns.processing.task.Task;
import com.oracle.coherence.patterns.processing.task.TaskProcessorDefinition;
import com.tangosol.io.ExternalizableLite;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.net.CacheFactory;
import com.tangosol.net.NamedCache;

@SuppressWarnings("serial")
public class RoundRobinDistributedTaskDispatchPolicy implements TaskDispatchPolicy, ExternalizableLite, PortableObject {

	private static Logger logger = LoggerFactory.getLogger(RoundRobinDistributedTaskDispatchPolicy.class);
	
    /**
     * The index of the item in the collection.
     */
    private transient Integer roundRobinIndex;
    private transient Integer roundRobinEntregablesIndex;


    /**
     * Default Constructor.
     */
    public RoundRobinDistributedTaskDispatchPolicy()
    {
    }

    private Comparator<TaskProcessorMediatorKey> taskProcessorKeyComparator = new Comparator<TaskProcessorMediatorKey>() {
		@Override
		public int compare(TaskProcessorMediatorKey o1, TaskProcessorMediatorKey o2) {
				CompareToBuilder compareToBuilder = new CompareToBuilder();
				compareToBuilder.append(o1.getMemberId(), o2.getMemberId());
				compareToBuilder.append(o1.getTaskProcessorDefinitionIdentifier().hashCode(), o2.getTaskProcessorDefinitionIdentifier().hashCode());
				compareToBuilder.append(o1.getUniqueId(), o2.getUniqueId());
				return compareToBuilder.toComparison();
		}        	
    };

    /**
     * {@inheritDoc}
     */
    public Map<TaskProcessorMediatorKey, TaskProcessorMediator> selectTaskProcessorSet(Task                     task,
                                                                                       SubmissionConfiguration  submissionConfiguration,
                                                                                       Map<TaskProcessorMediatorKey,
                                                                                       TaskProcessorMediator>   taskProcessorMediators,
                                                                                       ConcurrentHashMap<Identifier,
                                                                                       TaskProcessorDefinition> taskProcessorDefinitions)
    {
        if (taskProcessorMediators.isEmpty())
        {
            return taskProcessorMediators;
        }
        
        TaskProcessorMediatorKey selectedKey       = null;
        TaskProcessorMediator    selectedMediator  = null;        
        
        String processKey;
        int index;
        List<TaskProcessorMediatorKey> keyList = null;
        
        // comprobar si existen nodos dedicados para las tareas de entregables 
        Boolean isEntregable = isEntregableNodes(submissionConfiguration,taskProcessorMediators);        
             
        if (isEntregable) {
        	processKey = "roundRobinEntregablesIndex";
        	keyList = new ArrayList<TaskProcessorMediatorKey>(getEntregablesTaskMediatorsKeylist(taskProcessorMediators));
        	index = generateNext(keyList.size(), processKey);
        } else {
        	processKey = "roundRobinIndex";
        	keyList = new ArrayList<TaskProcessorMediatorKey>(taskProcessorMediators.keySet());
        	index = generateNext(taskProcessorMediators.size(), processKey);
        }
        
             
//   	int index = generateNext(taskProcessorMediators.size(), processKey);
//      int index = generateNext(taskProcessorMediators.size(),"roundRobinIndex");
        int                      collectionCounter = 0;
        
//        List<TaskProcessorMediatorKey> keyList = new ArrayList<TaskProcessorMediatorKey>(taskProcessorMediators.keySet());
        
        Collections.sort(keyList, taskProcessorKeyComparator);
        
        
        final Iterator<TaskProcessorMediatorKey> iter = keyList.iterator();
        

        while (iter.hasNext() && selectedKey == null)
        {
            if (collectionCounter == index)
            {
            	
            	selectedKey = iter.next();
            	selectedMediator = taskProcessorMediators.get(selectedKey);
                
                // FIXME: Detectar si el nodo de procesamiento sigue activo
                if (TaskProcessorStateEnum.INACTIVE.equals(selectedMediator.getProcessorState())) {
                	
                	logger.warn("El nodo {} seleccionado {} est� inactivo", index, selectedMediator);
                	
                	index = generateNext(taskProcessorMediators.size(),processKey);
                	selectedKey = null;
                	selectedMediator = null;
                }
                
                if (logger.isInfoEnabled()) {
                	logger.info("Task {} - Seleccionado {} : {}", task, index, selectedMediator);
                }
            }
            else
            {
                iter.next();
            }

            collectionCounter++;
        }

        return Collections.singletonMap(selectedKey, selectedMediator);
    }


    
    private Boolean isEntregableNodes(SubmissionConfiguration submissionConfiguration, Map<TaskProcessorMediatorKey, TaskProcessorMediator> taskProcessorMediators) {  
    	List<TaskProcessorMediatorKey> keyList = null; 
    	
         if (submissionConfiguration.getConfigurationDataMap().containsValue("entregables")) {
          	keyList = new ArrayList<TaskProcessorMediatorKey>(getEntregablesTaskMediatorsKeylist(taskProcessorMediators));
          	
          	if (keyList != null & keyList.size() > 0) {
          		return Boolean.TRUE;
          	}
         }
         
         return Boolean.FALSE;         
	}



	/**
     * Devuelve los nodos que estan listos para ejecutar una tarea de entregables.
     * @return 
     */
    private Set<TaskProcessorMediatorKey> getEntregablesTaskMediatorsKeylist (Map<TaskProcessorMediatorKey, TaskProcessorMediator> taskProcessorMediators) {
    
    	TaskProcessorMediatorKey key = null;
    	TaskProcessorMediator mediator = null;
    	Map<TaskProcessorMediatorKey, TaskProcessorMediator> entregablesTaskProcessors = new HashMap<TaskProcessorMediatorKey, TaskProcessorMediator>();
    	
    	List<TaskProcessorMediatorKey> keyList = new ArrayList<TaskProcessorMediatorKey>(taskProcessorMediators.keySet());
        Collections.sort(keyList, taskProcessorKeyComparator);
        Iterator<TaskProcessorMediatorKey> iter = keyList.iterator();
    	
        while (iter.hasNext()) {
        	
        	key = iter.next();    		
    		mediator = taskProcessorMediators.get(key);
        	
    		String member = (String) mediator.getAttributeMap().get("membername");
    		
    		if (member.contains("entregable")) {
    			entregablesTaskProcessors.put(key, mediator);    			
    		}        	
        }
        
        return entregablesTaskProcessors.keySet();
    
    }


	/**
     * Generate the next round robin item. 
     *
     * @param upperBoundary the max index
     * @param processKey the key of the process
     * @return the new index
     */
    private int generateNext(int upperBoundary, String processKey)
    {
    	NamedCache processingCache = CacheFactory.getCache("propiedades");
//    	Object key = "roundRobinIndex";
    	processingCache.lock(processKey, -1);
    	
    	try {
	        roundRobinIndex = (Integer) processingCache.get(processKey);
	        
	        if (roundRobinIndex == null) {
	        	roundRobinIndex = 0;
	        }
	        roundRobinIndex++;
	
	        if (roundRobinIndex >= upperBoundary)
	        {
	            roundRobinIndex = 0;
	        }
	        
	        processingCache.put(processKey, roundRobinIndex);
    	} finally {
    	    // Always unlock in a "finally" block
    	    // to ensure that uncaught exceptions
    	    // don't leave data locked
    		processingCache.unlock(processKey);
    	}

        return roundRobinIndex;
    }


    /**
     * {@inheritDoc}
     */
    public void readExternal(DataInput in) throws IOException
    {
        roundRobinIndex = 0;
    }


    /**
     * {@inheritDoc}
     */
    public void writeExternal(DataOutput out) throws IOException
    {
    }


    /**
     * {@inheritDoc}
     */
    public void readExternal(PofReader reader) throws IOException
    {
        roundRobinIndex = 0;
    }


    /**
     * {@inheritDoc}
     */
    public void writeExternal(PofWriter writer) throws IOException
    {
    }
}

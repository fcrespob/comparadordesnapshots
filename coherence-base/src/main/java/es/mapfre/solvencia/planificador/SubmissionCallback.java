package es.mapfre.solvencia.planificador;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.SubmissionOutcomeListener;


public class SubmissionCallback implements SubmissionOutcomeListener {
	private static Logger log = LoggerFactory.getLogger(SubmissionCallback.class);
	
	/**
	 * The name of the task.
	 */
	private String taskName;

	/**
	 * Constructor which takes a name of the task it is a callback for.
	 * 
	 * @param taskName
	 *            the name of the task
	 */
	public SubmissionCallback(String taskName) {
		this.taskName = taskName;
	}

	/**
	 * {@inheritDoc}
	 */
	public void onDone(Object oResult) {
		log.info("{}: Submission done", taskName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void onFailed(Object oResult) {
		log.info("{}: Submission failed: {}", taskName, oResult instanceof RuntimeException ? ((RuntimeException) oResult).getMessage() : oResult);
	}

	/**
	 * {@inheritDoc}
	 */
	public void onProgress(Object oProgress) {
		if (log.isDebugEnabled()) {
			log.debug("{}: Submission progress: {}", taskName, oProgress);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	public void onStarted() {
		log.info("{}: Submission started", taskName);
	}

	/**
	 * {@inheritDoc}
	 */
	public void onSuspended() {
		log.info("{}: Submission suspended", taskName);
	}
}
package es.mapfre.solvencia.planificador.coherence;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.oracle.coherence.patterns.processing.ProcessingSession;
import com.oracle.coherence.patterns.processing.SubmissionOutcome;
import com.oracle.coherence.patterns.processing.internal.DefaultSubmissionConfiguration;
import com.oracle.coherence.patterns.processing.task.ResumableTask;
import com.tangosol.net.Member;
import com.tangosol.net.MemberEvent;
import com.tangosol.net.MemberListener;

import es.mapfre.solvencia.dominio.parametrizacionGeneral.FichaProceso;
import es.mapfre.solvencia.motor.ResumableMotor;
import es.mapfre.solvencia.planificador.SubmissionCallback;

public class MemberEventListener implements MemberListener {
	private static final Logger log = LoggerFactory.getLogger(MemberEventListener.class);
	private static final long TIEMPO_ESPERA_CARENCIA = 10000L;

	private FichaProceso ficha;
	private Map<String, SubmissionOutcome> submissionOutcomes;
	private ProcessingSession session;
	private DefaultSubmissionConfiguration submissionConfiguration;
	private Boolean active = Boolean.TRUE;

	public void setFichaProceso(FichaProceso ficha) {
		this.ficha = ficha;
	}

	public void setSubmissionOutcomes(Map<String, SubmissionOutcome> submissionOutcomes) {
		this.submissionOutcomes = submissionOutcomes;
	}

	public void setSession(ProcessingSession session) {
		this.session = session;
	}

	public void setSubmissionConfiguration(DefaultSubmissionConfiguration submissionConfiguration) {
		this.submissionConfiguration = submissionConfiguration;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void memberJoined(MemberEvent evt) {
		// Tenemos que crear una tarea para este miembro
		Member member = evt.getMember();
		log.info("Detectado nuevo motor de calculo: {}", member);
		// Esperamos antes de mandarle ninguna tarea
		try {
			Thread.sleep(TIEMPO_ESPERA_CARENCIA);
		} catch (InterruptedException e1) {
			active = Boolean.FALSE;
		}
		synchronized (submissionOutcomes) {
			if (active && !evt.isLocal()) {
				ResumableTask tarea = new ResumableMotor(ficha);
				try {
					String nombreTarea = member.getMachineName() + StringUtils.SPACE + member.getMemberName();
					submissionOutcomes.put(nombreTarea,
							session.submit(tarea, submissionConfiguration, new SubmissionCallback(nombreTarea)));
				} catch (Throwable e) {
					log.warn("No se pudo lanzar la tarea", e);
				}
			}
		}
	}

	public void memberLeaving(MemberEvent evt) {
		log.warn("El motor de calculo {} está desconectando del cluster...", evt.getMember());
	}

	public void memberLeft(MemberEvent evt) {
		log.warn("El motor de calculo {} se ha desconectado del cluster...", evt.getMember());
	}
}
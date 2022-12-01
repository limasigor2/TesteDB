package br.com.igor.TesteDB.batch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;

import br.com.igor.TesteDB.processor.FileProcessor;

public class JobCompletionNotificationListerner extends JobExecutionListenerSupport {

	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileProcessor.class);

	@Override
	public void afterJob(JobExecution jobExecution) {
		if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
	        LOGGER.info("!!! FINALIZOU !!!");
	    }
	}
}

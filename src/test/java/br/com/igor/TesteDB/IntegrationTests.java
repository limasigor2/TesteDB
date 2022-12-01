package br.com.igor.TesteDB;

import javax.batch.runtime.JobInstance;

import static org.hamcrest.Matchers.is;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
//import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

import br.com.igor.TesteDB.batch.BatchConfiguration;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = { BatchConfiguration.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class IntegrationTests {

	private static final String TEST_OUTPUT = "src/test/resources/actual-output.csv";

	private static final String EXPECTED_OUTPUT = "src/test/resources/expected-output.csv";

	private static final String TEST_INPUT = "src/test/resources/test-input.csv";

	
	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	static {
		System.setProperty("file.output", TEST_OUTPUT);
		System.setProperty("file.input", TEST_INPUT);
	}

	@After
	public void cleanUp() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	private JobParameters defaultJobParameters() {
		JobParametersBuilder paramsBuilder = new JobParametersBuilder();
		return paramsBuilder.toJobParameters();
	}
	
	@BeforeEach
	public void setup() {
		
		try {
			PrintWriter writer = new PrintWriter(TEST_OUTPUT);
			writer.print("");
			writer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void integration_test_success() throws Exception {
		// given
		FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
		FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
		JobInstance actualJobInstance = jobExecution.getJobInstance();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualJobInstance.getJobName(), is("importContasJob"));
		assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
		AssertFile.assertFileEquals(expectedResult, actualResult);
	}

	@Test
	public void givenReferenceOutput_whenStep1Executed_thenSuccess() throws Exception {
		// given
		FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT);
		FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

		// when
		JobExecution jobExecution = jobLauncherTestUtils.launchStep("step1", defaultJobParameters());
		Collection<StepExecution> actualStepExecutions = jobExecution.getStepExecutions();
		ExitStatus actualJobExitStatus = jobExecution.getExitStatus();

		// then
		assertThat(actualStepExecutions.size(), is(1));
		assertThat(actualJobExitStatus.getExitCode(), is("COMPLETED"));
		AssertFile.assertFileEquals(expectedResult, actualResult);
	}
}

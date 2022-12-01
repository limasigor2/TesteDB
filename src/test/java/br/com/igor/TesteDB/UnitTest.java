package br.com.igor.TesteDB;

import static org.hamcrest.Matchers.is;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;

import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;

import org.junit.After;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.MetaDataInstanceFactory;
import org.springframework.batch.test.StepScopeTestUtils;
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
import br.com.igor.TesteDB.bean.Input;
import br.com.igor.TesteDB.bean.Output;
import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@SpringBatchTest
@EnableAutoConfiguration
@ContextConfiguration(classes = { BatchConfiguration.class })
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class })
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
class UnitTest extends TestCase {

	private static final String TEST_OUTPUT = "src/test/resources/output-one.csv";

	private static final String EXPECTED_OUTPUT_ONE = "src/test/resources/expected-output-one.csv";

	private static final String TEST_INPUT = "src/test/resources/test-input.csv";

	@Autowired
	private FlatFileItemWriter<Output> itemWriter;

	@Autowired
	private FlatFileItemReader<Input> itemReader;

	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;

	private JobParameters defaultJobParameters() {
		JobParametersBuilder paramsBuilder = new JobParametersBuilder();
		return paramsBuilder.toJobParameters();
	}

	static {
		System.setProperty("file.output", TEST_OUTPUT);
		System.setProperty("file.input", TEST_INPUT);
	}

	@After
	public void cleanUp() {
		jobRepositoryTestUtils.removeJobExecutions();
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
	public void givenMockedStep_whenReaderCalled_thenSuccess() throws Exception {

		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());

		StepScopeTestUtils.doInStepScope(stepExecution, () -> {
			Input input;
			itemReader.open(stepExecution.getExecutionContext());
			input = itemReader.read();
			assertThat(input.getAgencia(), is("0101"));
			assertThat(input.getConta(), is("12225-6"));
			assertThat(input.getSaldo(), is(100.00F));
			assertThat(input.getStatus(), is("A"));

			itemReader.close();
			return null;
		});
	}

	@Test
	public void givenMockedStep_whenWriterCalled_thenSuccess() throws Exception {

		FileSystemResource expectedResult = new FileSystemResource(EXPECTED_OUTPUT_ONE);
		FileSystemResource actualResult = new FileSystemResource(TEST_OUTPUT);

		Output output = new Output();
		output.setAgencia("0101;12225");
		output.setConta("12225-6");
		output.setSaldo(100.0F);
		output.setStatus("A");
		output.setResult(false);

		StepExecution stepExecution = MetaDataInstanceFactory.createStepExecution(defaultJobParameters());

		StepScopeTestUtils.doInStepScope(stepExecution, () -> {

			itemWriter.open(stepExecution.getExecutionContext());
			itemWriter.write(Arrays.asList(output));
			itemWriter.close();
			return null;
		});

		AssertFile.assertFileEquals(expectedResult, actualResult);

	}

}

package br.com.igor.TesteDB.batch;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import br.com.igor.TesteDB.bean.Input;
import br.com.igor.TesteDB.bean.Output;
import br.com.igor.TesteDB.mapper.InputMapper;
import br.com.igor.TesteDB.processor.FileProcessor;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	public JobBuilderFactory jobBuilderFactory;

	public StepBuilderFactory stepBuilderFactory;
		
	private Resource outputResource;
	
	private String fileInput;
	
	
	@Bean
	public FlatFileItemReader<Input> reader() {
	    return new FlatFileItemReaderBuilder<Input>().name("InputItemReader")
	      .resource(new FileSystemResource(fileInput))
	      .delimited()
	      .delimiter(";")
	      .names(new String[] { "agencia", "conta", "saldo", "status" })
	      .fieldSetMapper(new InputMapper())
	      .build();
	}
	
	@Bean
	public FlatFileItemWriter<Output> writer() {
	    FlatFileItemWriter<Output> writer = new FlatFileItemWriter<>();
	     
	    writer.setResource(outputResource);
	     
	    writer.setAppendAllowed(true);
	     
	    writer.setLineAggregator(new DelimitedLineAggregator<Output>() {
	      {
	        setDelimiter(";");
	        setFieldExtractor(new BeanWrapperFieldExtractor<Output>() {
	          {
	            setNames(new String[] { "agencia", "conta", "saldo", "status", "result" });
	          }
	        });
	      }
	    });
	    return writer;
	    
	}
	
	@Bean
	public Job importContasJob(JobCompletionNotificationListerner listener, Step step1) {
	    return jobBuilderFactory.get("importContasJob")
	      .incrementer(new RunIdIncrementer())
	      .listener(listener)
	      .flow(step1)
	      .end()
	      .build();
	}
	 
	@Bean
	public Step step1(FlatFileItemWriter<Output> writer) {
		FlatFileItemReader<Input> reader = reader();
		reader.setLinesToSkip(1);
		return stepBuilderFactory.get("step1")
	      .<Input, Output> chunk(10)
	      .reader(reader)
	      .processor(processor())
	      .listener(job())
	      .writer(writer)
	      .build();
	}
	 
	@Bean
	public FileProcessor processor() {
	    return new FileProcessor();
	}
	
	@Bean
	public JobCompletionNotificationListerner job() {
		return new JobCompletionNotificationListerner();
	}
	
	public BatchConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
			Environment env) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.outputResource = new FileSystemResource(env.getProperty("file.output"));
		this.fileInput = env.getProperty("file.input");
	}
	
	
}

package com.example.config;


import javax.sql.DataSource;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.batch.support.DatabaseType;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.example.demo.PersonFieldSetMapper;
import com.example.demo.PersonItemProcessor;
import com.example.model.Person;

@Configuration
@EnableBatchProcessing
public class SpringBatchConfig {
    
	
	
	@Bean
	public Job jobCsvXml(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory ) {
		return jobBuilderFactory.get("jobCsvXml").start(step1(stepBuilderFactory)).build();
		
	}

	
	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory) {
		
		return stepBuilderFactory.get("step1").<Person, Person>chunk(10).reader(flatFileItemReader()).processor(ItemProcessor()).writer(staxEventItemWriter(jaxb2Marshaller())).build();
	}
	
	
	
	@Bean
	public DataSource dataSource() {
		
		EmbeddedDatabaseBuilder embeddedDatabaseBuilder  = new EmbeddedDatabaseBuilder();
		return embeddedDatabaseBuilder.addScript("classpath:org/springframework/batch/core/schema-drop-h2.sql").addScript("classpath:org/springframework/batch/core/schema-h2.sql").setType(EmbeddedDatabaseType.H2)
		.build();
	}
	
	@Bean
	public ResourcelessTransactionManager txManager() {
		
		return new ResourcelessTransactionManager();
	}
	
	@Bean
	public JobRepository jbRepository(DataSource dataSource, ResourcelessTransactionManager transactionManager)
			throws Exception {
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDatabaseType(DatabaseType.H2.getProductName());
		factory.setDataSource(dataSource);
		factory.setTransactionManager(transactionManager);
		return factory.getObject();
	}
	
	
	@Bean
	public FlatFileItemReader<Person> flatFileItemReader(){
		
		return new FlatFileItemReaderBuilder<Person>()
		        .name("personItemReader")
		        .resource(new ClassPathResource("person.csv"))
		        .delimited().names(new String[] {"id", "firstName", "lastName" })
		        .targetType(Person.class).build();
	
	}
	
	@Bean
	public PersonItemProcessor ItemProcessor(){
		
		return new PersonItemProcessor();
	}
	
	@Bean
	public PersonFieldSetMapper PersonFieldSetMapper() {
		return new PersonFieldSetMapper();
	
	}
	

	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(Person.class);
		
		return jaxb2Marshaller;
	}
	
	@Bean(destroyMethod = "")
	public StaxEventItemWriter<Person> staxEventItemWriter(Jaxb2Marshaller marshaller){
		
		
		return new StaxEventItemWriterBuilder<Person>().name("personItemWriter").resource(new FileSystemResource("target/test-outputs/person.xml"))
				.marshaller(jaxb2Marshaller()).rootTagName("personInfo").build();
	
	}
	
	
	
	}
	


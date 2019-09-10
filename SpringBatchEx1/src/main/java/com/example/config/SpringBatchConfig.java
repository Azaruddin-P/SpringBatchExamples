package com.example.config;


import javax.sql.DataSource;


import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.support.DatabaseType;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
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
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
	public Person person() {
		
		return new Person();
	}
	
	@Bean
	@Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
	public PersonItemProcessor itemProcessor(){
		
		return new PersonItemProcessor();
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
	public JobRepository jobRepository(DataSource dataSource, ResourcelessTransactionManager resourcelessTransactionManager) throws Exception{
		
		JobRepositoryFactoryBean factory = new JobRepositoryFactoryBean();
		factory.setDatabaseType(DatabaseType.H2.getProductName());
		factory.setDataSource(dataSource);
		factory.setTransactionManager(txManager());
		return factory.getObject();
	}
	@Bean
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		return jobLauncher;
		
	}
	@Bean
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(Person.class);
		
		return jaxb2Marshaller;
	}
	@Bean
	public FlatFileItemReader<Person> flatFileItemReader(){
		FlatFileItemReader<Person> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new ClassPathResource("person.csv"));
		
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames("id", "firstName", "lastName");
		
		DefaultLineMapper<Person> defaultLineMapper = new DefaultLineMapper<>();
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		defaultLineMapper.setFieldSetMapper(PersonFieldSetMapper());
		
		flatFileItemReader.setLineMapper(defaultLineMapper);
		
		return flatFileItemReader;
	}
	
	@Bean
	public PersonFieldSetMapper PersonFieldSetMapper() {
		return new PersonFieldSetMapper();
	}
	@Bean(destroyMethod= "")
	public StaxEventItemWriter<Person> staxEventItemWriter(Jaxb2Marshaller marshaller){
		StaxEventItemWriter<Person> staxEventItemWriter = new StaxEventItemWriter<>();
		staxEventItemWriter.setResource(new FileSystemResource("C:/workspace/person.xml"));
		staxEventItemWriter.setMarshaller(marshaller);
		staxEventItemWriter.setRootTagName("personInfo");
		
		return staxEventItemWriter;
	}
	@Bean
	public Job jobCsvXml(JobBuilderFactory jobBuilderFactory, Step step) {
		return jobBuilderFactory.get("jobCsvXml").incrementer(new RunIdIncrementer()).flow(step).end().build();
	}
	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory, ResourcelessTransactionManager transactionManager, ItemReader<Person> reader, ItemWriter<Person> writer, ItemProcessor<Person, Person> processor) {
		
		return stepBuilderFactory.get("step1").<Person, Person>chunk(10).reader(reader).processor(processor).writer(writer).build();
	}
	
	}
	


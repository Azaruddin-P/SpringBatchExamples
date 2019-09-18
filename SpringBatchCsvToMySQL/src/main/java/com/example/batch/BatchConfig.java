package com.example.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.example.model.User;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	public DataSource dataSource;
	
	@Bean
	public Job userJob(JobBuilderFactory jobBuilderFactory, Step userStep) {
		return jobBuilderFactory.get("userJob").incrementer(new RunIdIncrementer()).flow(userStep).end().build();
		
	}
	@Bean
	public Step userStep(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("userStep").<User, User> chunk(3).reader(reader()).processor(processor()).writer(writer()).build();
	}
	@Bean
	public DataSource datSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/springbatch");
		dataSource.setUsername("root");
		dataSource.setPassword("Welcome");
		return dataSource;

	}
	@Bean
	public FlatFileItemReader<User> reader(){
		FlatFileItemReader<User> reader = new FlatFileItemReader<User>();
		reader.setResource(new ClassPathResource("users.csv"));
		reader.setLineMapper(new DefaultLineMapper<User>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] {"name"});
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<User>() {{
				setTargetType(User.class);
			}});
			
		
		}});
		return reader;
		
	}
	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
		
	}
	@Bean
	public JdbcBatchItemWriter<User> writer(){
		JdbcBatchItemWriter<User> writer = new JdbcBatchItemWriter<User>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<User>());
		writer.setSql("INSERT INTO user1(name) VALUES (:name)");
		writer.setDataSource(dataSource);
	
		return writer;
		
	}
}

package com.example.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.example.model.Student;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public Job excelJob(JobBuilderFactory jobBuilderFactory, Step step) {
		return jobBuilderFactory.get("excelJob").incrementer(new RunIdIncrementer()).flow(step).end().build();
		
	}
	@Bean
	public Step step(StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("step").<Student, Student>chunk(10).reader(reader()).processor(processor()).writer(writer()).build();
		
	}
	@Bean
	public DataSource datSource() {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/springbatch");
		dataSource.setUsername("root");
		dataSource.setPassword("Welcome");
		return dataSource;
		
	}
	@Bean(destroyMethod ="")
	public JdbcCursorItemReader<Student> reader(){
		JdbcCursorItemReader<Student> reader = new JdbcCursorItemReader<Student>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT * FROM STUDENT");
		reader.setRowMapper(new StudentRowMapper());
		return reader;
		
	}
	@Bean
	public StudentItemProcessor processor() {
		return new StudentItemProcessor();
	}
	@Bean
	public StudentRowMapper StudentRowMapper() {
		return new StudentRowMapper();
		
	}
	@Bean
	public ExcelWriter writer() {
		return new ExcelWriter();
		
	}
	
}

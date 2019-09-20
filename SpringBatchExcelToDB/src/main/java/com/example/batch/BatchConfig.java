package com.example.batch;


import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.excel.mapping.BeanWrapperRowMapper;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;



import com.example.model.Student;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	private DataSource dataSource;
	
	@Bean
	public Job studentJob(JobBuilderFactory jobBuilderFactory, Step step) {
		return jobBuilderFactory.get("studentJob").incrementer(new RunIdIncrementer()).flow(step).end().build();
		
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
	
	@Bean
	public org.springframework.batch.item.excel.RowMapper<Student> excelRowMapper() {
		BeanWrapperRowMapper<Student> rowMapper = new BeanWrapperRowMapper<>();
		rowMapper.setTargetType(Student.class);
		return rowMapper;
	}

	
	@Bean
	public ItemReader<Student> reader() {
		PoiItemReader<Student> reader = new PoiItemReader<Student>();
		reader.setResource(new ClassPathResource("student.xlsx"));
		reader.setLinesToSkip(1);
		reader.setRowMapper(excelRowMapper());
		return reader;
		
	}
	@Bean
	public StudentItemProcessor processor() {
		return new StudentItemProcessor();
	}
	@Bean
	public JdbcBatchItemWriter<Student> writer(){
		JdbcBatchItemWriter<Student> writer = new JdbcBatchItemWriter<Student>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Student>());
		writer.setSql("INSERT INTO student VALUES (:firstName, :lastName, :email)");
		writer.setDataSource(dataSource);
		return writer;
		
	}
}

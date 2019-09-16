package com.example.batch;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.example.model.User;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

	@Autowired
	public DataSource dataSource;

	@Bean
	public Job mysqlToCsv(JobBuilderFactory jobBuilderFactory, Step step1) {

		return jobBuilderFactory.get("mysqlToCsv").incrementer(new RunIdIncrementer()).flow(step1).end().build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory) {

		return stepBuilderFactory.get("step1").<User, User>chunk(5).reader(reader()).processor(processor())
				.writer(writer()).build();
	}

	@Bean
	public DataSource dataSource() {

		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl("jdbc:mysql://localhost:3306/springbatch");
		dataSource.setUsername("root");
		dataSource.setPassword("Welcome");

		return dataSource;
	}

	@Bean
	public JdbcCursorItemReader<User> reader() {
		JdbcCursorItemReader<User> reader = new JdbcCursorItemReader<User>();
		reader.setDataSource(dataSource);
		reader.setSql("SELECT * FROM USER");
		reader.setRowMapper(new UserRowMapper());

		return reader;

	}

	@Bean
	public UserItemProcessor processor() {
		return new UserItemProcessor();
	}

	@Bean
	public UserRowMapper UserRowMapper() {

		return new UserRowMapper();
	}

	@Bean
	public FlatFileItemWriter<User> writer() {

		FlatFileItemWriter<User> writer = new FlatFileItemWriter<User>();
		writer.setResource(new FileSystemResource("target/test-outputs/user.csv"));
		writer.setLineAggregator(new DelimitedLineAggregator<User>() {
			{
				setDelimiter(",");
				setFieldExtractor(new BeanWrapperFieldExtractor<User>() {
					{
						setNames(new String[] { "id", "name" });
					}
				});
			}
		});

		return writer;
	}

}

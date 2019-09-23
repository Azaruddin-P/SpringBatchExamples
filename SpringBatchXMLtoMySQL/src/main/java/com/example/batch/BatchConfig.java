package com.example.batch;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.example.model.Employee;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Autowired
	public DataSource dataSource;
	
	@Bean
	public DataSource dataSource() {
		
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setUrl("jdbc:mysql://localhost/springbatch");
		dataSource.setUsername("root");
		dataSource.setPassword("Welcome");
		return dataSource;
		
	}
	@Bean
	public StaxEventItemReader<Employee> reader(){
		StaxEventItemReader<Employee> reader = new StaxEventItemReader<>();
		reader.setResource(new ClassPathResource("employee.xml"));
		reader.setFragmentRootElementName("employee");
		
		Map<String, String> aliases = new HashMap<String, String>();
		aliases.put("employee", "com.example.model.Employee");
		
		XStreamMarshaller xStreamMarshaller = new XStreamMarshaller();
		xStreamMarshaller.setAliases(aliases);
		reader.setUnmarshaller(xStreamMarshaller);
		return reader;
	}
	
	@SuppressWarnings("unused")
	private class EmployeeItemPreparedStatementSetter implements ItemPreparedStatementSetter<Employee>{

		@Override
		public void setValues(Employee item, PreparedStatement ps) throws SQLException {
			ps.setString(1, item.getName());
			ps.setString(2, item.getDesignation());
			ps.setString(3, item.getCity());
		}
		
	}
	
	 
	 @Bean
	 public JdbcBatchItemWriter<Employee> writer(){
	  JdbcBatchItemWriter<Employee> writer = new JdbcBatchItemWriter<Employee>();
	  writer.setDataSource(dataSource);
	  writer.setSql("insert into employee values(?, ?, ?)");
	  writer.setItemPreparedStatementSetter(new EmployeeItemPreparedStatementSetter());
	  
	  return writer;
	 }
	 
	 
	 @Bean
	 public Step step1(StepBuilderFactory stepBuilderFactory) {
	  return stepBuilderFactory.get("step1")
	    .<Employee, Employee> chunk(10)
	    .reader(reader())
	    .writer(writer())
	    .build();
	 }
	 
	 @Bean
	 public Job employeeJob(JobBuilderFactory jobBuilderFactory, Step step1) {
	  return jobBuilderFactory.get("employeeJob")
	    .incrementer(new RunIdIncrementer())
	    .flow(step1)
	    .end()
	    .build();
	    
	 }
}

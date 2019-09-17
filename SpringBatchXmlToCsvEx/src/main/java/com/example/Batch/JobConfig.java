package com.example.Batch;

import java.util.HashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.example.model.Employee;

@Configuration
public class JobConfig {


	
	  @Bean
	  public Job XmlToCsvJob(JobBuilderFactory jobBuilderFactory, Step step1) {
	    return jobBuilderFactory.get("XmlToCsvJob").incrementer(new RunIdIncrementer()).flow(step1).end().build();
	  }
	  
	  @Bean
	  public Step step1(StepBuilderFactory stepBuilderFactory) {
	    return stepBuilderFactory.get("step1").<Employee, Employee>chunk(10).reader(reader()).processor(processor())
	        .writer(writer()).build();
	  }
	
	  
	  @Bean
	  public StaxEventItemReader<Employee> reader() {
	    StaxEventItemReader<Employee> reader = new StaxEventItemReader<>();
	    reader.setResource(new ClassPathResource("employee.xml"));
	    reader.setFragmentRootElementName("employee");
	    reader.setUnmarshaller(unMarshaller());
	    return reader;
	  }
	  
	  @SuppressWarnings("rawtypes")
		@Bean
		  public Unmarshaller unMarshaller() {
			    XStreamMarshaller unMarshal = new XStreamMarshaller();
			    unMarshal.setAliases(new HashMap<String, Class>() {
					private static final long serialVersionUID = 1L;

				{
			      put("employee", Employee.class);
			    }});
			    return unMarshal;
			  }
		  
	  
	  @Bean
	  public EmployeeItemProcessor processor() {
	    return new EmployeeItemProcessor();
	  }
	
	  @Bean
	public EmployeeFieldSetMapper EmployeeFieldSetMapper() {
		return new EmployeeFieldSetMapper();
		
	}
	  
	 
	  
	  @Bean
	  public FlatFileItemWriter<Employee> writer() {
	    FlatFileItemWriter<Employee> writer = new FlatFileItemWriter<>();
	    writer.setResource(new FileSystemResource("target/test-outputs/employee.csv"));
	    writer.setLineAggregator(new DelimitedLineAggregator<Employee>() {{
	      setDelimiter(",");
	      setFieldExtractor(new BeanWrapperFieldExtractor<Employee>() {{
	        setNames(new String[]{"name", "designation", "city"});
	      }});
	    }});
	    return writer;
	  }
}

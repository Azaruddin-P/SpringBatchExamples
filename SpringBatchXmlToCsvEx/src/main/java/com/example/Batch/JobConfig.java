package com.example.Batch;

import java.util.HashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.Unmarshaller;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.example.model.Employee;

@Configuration
public class JobConfig {


	 @Autowired
	  private JobBuilderFactory jobBuilderFactory;
	  @Autowired
	  private StepBuilderFactory stepBuilderFactory;
	
	  @Bean
	  public Job XmlToCsvJob() {
	    return jobBuilderFactory.get("XmlToCsvJob").flow(step1()).end().build();
	  }
	  
	  @Bean
	  public Step step1() {
	    return stepBuilderFactory.get("step1").<Employee, Employee>chunk(10).reader(reader())
	        .writer(writer()).processor(processor()).build();
	  }
	  
	  @Bean
	  public EmployeeItemProcessor processor() {
	    return new EmployeeItemProcessor();
	  }
	  @Bean
	  public Unmarshaller unMarshaller() {
		    XStreamMarshaller unMarshal = new XStreamMarshaller();
		    unMarshal.setAliases(new HashMap<String, Class>() {{
		      put("employee", Employee.class);
		    }});
		    return unMarshal;
		  }
	  
	  @Bean
	  public StaxEventItemReader<Employee> reader() {
	    StaxEventItemReader<Employee> reader = new StaxEventItemReader<>();
	    reader.setResource(new ClassPathResource("employee.xml"));
	    reader.setFragmentRootElementName("employee");
	    reader.setUnmarshaller(unMarshaller());
	    return reader;
	  }
	  
	  @Bean
	  public FlatFileItemWriter<Employee> writer() {
	    FlatFileItemWriter<Employee> writer = new FlatFileItemWriter<>();
	    writer.setResource(new FileSystemResource("csv/employee.csv"));
	    writer.setLineAggregator(new DelimitedLineAggregator<Employee>() {{
	      setDelimiter(",");
	      setFieldExtractor(new BeanWrapperFieldExtractor<Employee>() {{
	        setNames(new String[]{"name", "designation", "city"});
	      }});
	    }});
	    return writer;
	  }
}

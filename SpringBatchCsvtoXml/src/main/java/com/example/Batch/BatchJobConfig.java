package com.example.Batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.batch.item.xml.builder.StaxEventItemWriterBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import com.example.model.Employee;


@Configuration
public class BatchJobConfig {

	@Bean
	public Job job(JobBuilderFactory jobBuilderFactory, Step helloStep) {
		
		return jobBuilderFactory.get("job").incrementer(new RunIdIncrementer()).flow(helloStep).end().build();
	}

	@Bean
	public Step helloStep(StepBuilderFactory stepBuilderFactory) {
		
		return stepBuilderFactory.get("helloStep").<Employee, Employee>chunk(3).reader(flatFileItemReader()).processor(processor()).writer(staxEventItemWriter(jaxb2Marshaller())).build();
	}
	
	
	
	@Bean
	public FlatFileItemReader<Employee> flatFileItemReader(){
		/*FlatFileItemReader<Employee> flatFileItemReader = new FlatFileItemReader<>();
		flatFileItemReader.setResource(new ClassPathResource("employee.csv"));
		
		DelimitedLineTokenizer delimitedLineTokenizer = new DelimitedLineTokenizer();
		delimitedLineTokenizer.setNames(new String[] { "name", "designation", "city" });

		
		DefaultLineMapper<Employee> defaultLineMapper = new DefaultLineMapper<>();
		defaultLineMapper.setLineTokenizer(delimitedLineTokenizer);
		defaultLineMapper.setFieldSetMapper(new EmployeeFieldSetMapper());
		
		flatFileItemReader.setLineMapper(defaultLineMapper);
		return flatFileItemReader;*/
		
	    return new FlatFileItemReaderBuilder<Employee>()
		        .name("employeeItemReader")
		        .resource(new ClassPathResource("employee.csv"))
		        .delimited().names(new String[] {"name", "designation", "city" })
		        .targetType(Employee.class).build();
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
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
		jaxb2Marshaller.setClassesToBeBound(Employee.class);
		return jaxb2Marshaller;
		
	}
	@Bean(destroyMethod ="")
	public StaxEventItemWriter<Employee> staxEventItemWriter(Jaxb2Marshaller jaxb2marshaller){
		
			return new StaxEventItemWriterBuilder<Employee>().name("employeeItemWriter").resource(new FileSystemResource("target/test-outputs/employee.xml"))
					.marshaller(jaxb2Marshaller()).rootTagName("employeeInfo").build();
		
	}
	
}

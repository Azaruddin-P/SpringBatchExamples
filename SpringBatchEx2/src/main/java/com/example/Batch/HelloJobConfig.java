package com.example.Batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.PassThroughLineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;

import com.example.model.Student;

public class HelloJobConfig {

	@Bean
	public Job helloJob(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		return jobBuilderFactory.get("helloJob").start(helloStep(stepBuilderFactory)).build();
		
	}

	@Bean
	public Step helloStep(StepBuilderFactory stepBuilderFactory) {
		
		return stepBuilderFactory.get("helloStep").<Student, String>chunk(10).reader(reader()).processor(processor()).writer(writer()).build();
	}
	
	@Bean
	public FlatFileItemReader<Student> reader(){
	    return new FlatFileItemReaderBuilder<Student>()

				.name("studentItemReader")
				.resource (new ClassPathResource("Student.java"))
		        .delimited().names(new String[] {"name", "course","city"}).targetType(Student.class).build();
	}
	
	@Bean
	public StudentItemProcessor processor() {
		return new StudentItemProcessor();
	}
	
	@Bean
	public FlatFileItemWriter<String> writer() {
	    return new FlatFileItemWriterBuilder<String>()
	        .name("greetingItemWriter")
	        .resource(new FileSystemResource(
	            "target/test-outputs/greetings1.txt"))
	        .lineAggregator(new PassThroughLineAggregator<>()).build();
				
		
	}
	
	}

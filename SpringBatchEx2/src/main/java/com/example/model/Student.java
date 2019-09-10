package com.example.model;

public class Student {

	private String name = "azar";
	private String course = "java";
	private String city = "bng";
	
	public Student() {}

	public Student(String name, String course, String city) {
		super();
		this.name = name;
		this.course = course;
		this.city = city;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCourse() {
		return course;
	}

	public void setCourse(String course) {
		this.course = course;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	
}

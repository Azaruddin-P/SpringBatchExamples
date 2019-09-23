package com.example.batch;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ItemWriter;

import com.example.model.Student;

public class ExcelWriter implements ItemWriter<Student> {

	@Override
	public void write(List<? extends Student> items) throws Exception {
		
		XSSFWorkbook workbook = new  XSSFWorkbook();
		XSSFSheet spreadsheet = workbook.createSheet("student db");
		
		XSSFRow row = spreadsheet.createRow(1);
		XSSFCell cell;
		
		cell = row.createCell(1);
		cell.setCellValue("FIRST NAME");
		cell = row.createCell(2);
		cell.setCellValue("LAST NAME");
		cell = row.createCell(3);
		cell.setCellValue("EMAIL");
		
		int i = 2;
		
		for(Student s : items) {
			
			row = spreadsheet.createRow(i);
			cell = row.createCell(1);
			cell.setCellValue(s.getFirstName());
			cell = row.createCell(2);
			cell.setCellValue(s.getLastName());
			cell = row.createCell(3);
			cell.setCellValue(s.getEmail());
			
			i++;
		}
		
	
	
		try(FileOutputStream out = new FileOutputStream(new File("studentexcel.xlsx"))){
		workbook.write(out);
		//out.close();
		
	}

	}
	
}

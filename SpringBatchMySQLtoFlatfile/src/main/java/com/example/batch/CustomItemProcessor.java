package com.example.batch;

import org.springframework.batch.item.ItemProcessor;

import com.example.model.Tutorial;

public class CustomItemProcessor implements ItemProcessor<Tutorial, Tutorial> {

	@Override
	public Tutorial process(Tutorial item) throws Exception {
		System.out.println("processing..."+item);
		return item;
	}

}

package com.example.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mongodb.client.MongoIterable;

@RunWith(SpringRunner.class)
@SpringBootTest
public abstract class DemoApplicationTests {

	protected <T> List<T> toList(MongoIterable<T> iterable) {
		final List<T> result = new ArrayList<>();
		iterable.forEach((Consumer<T>) result::add);
		return result;
	}

}

/*******************************************************************************
 * Copyright 2017 Francesco Cina'
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package com.example.demo.repository.person;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.time.LocalDateTime;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.DemoApplicationTests;

public class SpringDataMongoTest extends DemoApplicationTests {

	@Autowired
	private PersonRepository personRepository;

	@Test
	public void personRepositoryShouldNotBeNull() {
		assertNotNull(personRepository);
	}

	@Test
	public void testMongoWithSpringData() {

		personRepository.deleteAll();

		final Person john = new Person("John", "Doe", LocalDateTime.now(), "Winner", 100);
		final Person joe = new Person("Joe", "Blogs", LocalDateTime.now(), "Loser", 50);
		assertNull(john.getId());
		assertNull(joe.getId());

		personRepository.save(john);
		personRepository.save(joe);
		assertNotNull(john.getId());
		assertNotNull(joe.getId());

		System.out.println("Find all");
		personRepository.findAll().forEach(System.out::println);

		System.out.println("Find by findBySalary");
		personRepository.findBySalary(100).forEach(System.out::println);

		System.out.println("Making John a loser");
		john.setProfession("Loser");
		personRepository.save(john);

		final Person foundJohn = personRepository.findOne(john.getId());
		assertNotNull(foundJohn);
		assertEquals(foundJohn.getId(), john.getId());
		System.out.println("John new profession is: " + foundJohn.getProfession());
		assertEquals(foundJohn.getProfession(), john.getProfession());
	}
}

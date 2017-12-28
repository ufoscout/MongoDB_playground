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
package com.example.demo.morphia;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;
import org.mongodb.morphia.query.UpdateResults;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.DemoApplicationTests;
import com.mongodb.MongoClient;

public class MongoMorphiaTest extends DemoApplicationTests {

	@Autowired
	private MongoClient mongo;

	@Test
	public void mongoClientShouldBeInjected() {
		assertNotNull(mongo);
	}

	@Test
	public void useMongoMorphia() throws InterruptedException {

		final Morphia morphia = new Morphia();

		// tell Morphia where to find your classes
		// can be called multiple times with different packages or classes
		morphia.mapPackage(getClass().getPackage().getName());

		// create the Datastore connecting to the default port on the local host
		final String databaseName = "testDataBaseMorphia";
		final Datastore ds = morphia.createDatastore(mongo, databaseName);
		ds.ensureIndexes();

		assertTrue(ds.delete(ds.createQuery(Employee.class)).wasAcknowledged());

		// create Employees
		final Employee elmer = new Employee("Elmer Fudd", 50000.0);
		assertNull(elmer.getId());
		ds.save(elmer);
		System.out.println("Saved one employee with id: " + elmer.getId());
		assertNotNull(elmer.getId());

		final Employee daffy = new Employee("Daffy Duck", 40000.0);
		ds.save(daffy);

		final Employee pepe = new Employee("Pepé Le Pew", 25000.0);
		ds.save(pepe);

		elmer.getDirectReports().add(daffy);
		elmer.getDirectReports().add(pepe);
		ds.save(elmer);

		pepe.setManager(elmer);
		ds.save(pepe);

		assertEquals(3, ds.getCount(Employee.class));

		// find Employee
		{
			final Employee found = ds.find(Employee.class).filter("id = ", daffy.getId()).get();
			assertNotNull(found);
			assertEquals(daffy.getName(), found.getName());
			assertNotNull(found.getDirectReports());
			assertTrue(found.getDirectReports().isEmpty());
			assertNull(found.getManager());
		}

		// find Employee with Manager
		{
			final Employee found = ds.find(Employee.class).filter("id = ", pepe.getId()).get();
			assertNotNull(found);
			assertEquals(pepe.getName(), found.getName());
			assertNotNull(found.getDirectReports());
			assertTrue(found.getDirectReports().isEmpty());
			assertNotNull(found.getManager());
		}

		// find Employee with directReports
		{
			final Employee found = ds.find(Employee.class).filter("id = ", elmer.getId()).get();
			assertNotNull(found);
			assertEquals(elmer.getName(), found.getName());
			assertFalse(found.getDirectReports().isEmpty());
		}

		// find underpaid Employees
		{
			List<Employee> underpaid = ds.find(Employee.class).filter("salary <=", 30000).asList();
			assertEquals(1, underpaid.size());

			underpaid = ds.find(Employee.class).field("salary").lessThanOrEq(30000).asList();
			assertEquals(1, underpaid.size());
		}

		// increase salary of underpaid Employees
		{
			final Query<Employee> underPaidQuery = ds.find(Employee.class).filter("salary <=", 30000);
			final UpdateOperations<Employee> updateOperations = ds.createUpdateOperations(Employee.class).inc("salary", 10000);

			final UpdateResults results = ds.update(underPaidQuery, updateOperations);
			assertEquals(1, results.getUpdatedCount());
		}

	}

}

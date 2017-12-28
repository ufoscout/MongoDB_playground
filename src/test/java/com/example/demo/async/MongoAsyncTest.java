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
package com.example.demo.async;

import static com.mongodb.client.model.Filters.eq;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.UUID;
import java.util.concurrent.CountDownLatch;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;

import com.example.demo.DemoApplicationTests;
import com.mongodb.ConnectionString;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoClients;
import com.mongodb.async.client.MongoCollection;
import com.mongodb.async.client.MongoDatabase;

public class MongoAsyncTest extends DemoApplicationTests {

	private final MongoClient mongo = MongoClients.create(new ConnectionString("mongodb://localhost"));

	@Test
	public void useMongoAsync() throws InterruptedException {

		final String databaseName = "testDataBaseAsync";
		final MongoDatabase db = mongo.getDatabase(databaseName);

		final User user = createUser();
		final Document doc = createDBObject(user);

		final MongoCollection<Document> col = db.getCollection("users");

		// create user
		{
			final CountDownLatch waiter = new CountDownLatch(1);

			col.insertOne(doc, (Void result, final Throwable t) -> {
				if (t == null) {
					System.out.println("Inserted!");
				} else {
					System.out.println("Error while trying to insert");
				}
				waiter.countDown();
			});
			waiter.await();
		}
		final ObjectId id = doc.getObjectId("_id");
		System.out.println("document generated id: " + id);

		// count users
		{
			final CountDownLatch waiter = new CountDownLatch(1);
			col.count((Long count, Throwable t) -> {
					          System.out.println(count);
					          assertTrue(count>0);
					          waiter.countDown();
					  });
			waiter.await();
		}

		// read users
		{
			final CountDownLatch waiter = new CountDownLatch(1);
			col.find(eq("name", user.getName())).first((Document userFound, Throwable t) -> {
						assertEquals(user.getRole(), userFound.get("role", String.class));
					    waiter.countDown();
					});
			waiter.await();
		}

	}

	private static Document createDBObject(User user) {
		return new Document()
				// .append("_id", user.getId())
				.append("name", user.getName()).append("role", user.getRole()).append("isEmployee", user.isEmployee());
	}

	private static User createUser() {
		final User u = new User();
		// u.setId(2);
		u.setName(UUID.randomUUID().toString());
		u.setEmployee(true);
		u.setRole(UUID.randomUUID().toString());
		return u;
	}

}

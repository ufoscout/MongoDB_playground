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
package com.example.demo.sync;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.set;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;
import java.util.UUID;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.DemoApplicationTests;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

public class MongoSyncTest extends DemoApplicationTests {

	@Autowired
	private MongoClient mongo;

	@Test
	public void mongoClientShouldBeInjected() {
		assertNotNull(mongo);
	}

	@Test
	public void useMongo() {

		final String databaseName = "testDataBase";
		final MongoDatabase db = mongo.getDatabase(databaseName);
		final List<String> collectionNames = toList(db.listCollectionNames());
		System.out.println("Found collections: " + collectionNames);

		final User user = createUser();
		final Document doc = createDBObject(user);

		final MongoCollection<Document> col = db.getCollection("users");

		//create user
		col.insertOne(doc);
		final ObjectId id = doc.getObjectId("_id");
		System.out.println("document generated id: " + id);

		//read user
		Document userFound = col.find(eq("name", user.getName())).first();
		assertEquals(user.getRole(), userFound.get("role", String.class));


		user.setRole(UUID.randomUUID().toString());
		// Update One
		final UpdateResult updateResult = col.updateOne(eq("name", user.getName()), set("role", user.getRole()));
		assertEquals(1, updateResult.getModifiedCount());
		userFound = col.find(eq("name", user.getName())).first();
		assertEquals(user.getRole(), userFound.get("role", String.class));

        // Delete One
		final DeleteResult deleteResult = col.deleteOne(eq("role", user.getRole()));
		assertEquals(1, deleteResult.getDeletedCount());

	}

	private static Document createDBObject(User user) {
		return new Document()
			//.append("_id", user.getId())
		 	.append("name", user.getName())
		 	.append("role", user.getRole())
		 	.append("isEmployee", user.isEmployee());
	}

	private static User createUser() {
		final User u = new User();
		//u.setId(2);
		u.setName(UUID.randomUUID().toString());
		u.setEmployee(true);
		u.setRole(UUID.randomUUID().toString());
		return u;
	}


}

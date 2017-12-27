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
package com.example.demo.pojo;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static org.bson.codecs.configuration.CodecRegistries.fromProviders;
import static org.bson.codecs.configuration.CodecRegistries.fromRegistries;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.example.demo.DemoApplicationTests;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;


public class MongoPojoTest extends DemoApplicationTests {

	@Autowired
	private MongoClient mongo;

	@Test
	public void mongoClientShouldBeInjected() {
		assertNotNull(mongo);
	}

	@Test
	public void useMongoWithPojo() {

		final String databaseName = "testDataBasePojo";

		final CodecRegistry pojoCodecRegistry = fromRegistries(MongoClient.getDefaultCodecRegistry(), fromProviders(PojoCodecProvider.builder().automatic(true).build()));

		final MongoDatabase database = mongo.getDatabase(databaseName).withCodecRegistry(pojoCodecRegistry);

		// get a handle to the "people" collection
		final MongoCollection<Person> collection = database.getCollection("people", Person.class);

		// drop all the data in it
		collection.drop();

		// make a document and insert it
		final Person ada = new Person("Ada Byron", 20, new Address("St James Square", "London", "W1"));
		System.out.println("Created person with _id " + ada.getId());

		collection.insertOne(ada);
		System.out.println("Inserted person with _id " + ada.getId());

		{
			final Person somebody = collection.find(eq("address.city", "London")).first();
			assertEquals(ada.getName(), somebody.getName());
			assertEquals(ada.getId(), somebody.getId());
		}

		// Update One
		final UpdateResult updateResult = collection.updateOne(eq("name", "Ada Byron"), combine(set("age", 23), set("name", "Ada Lovelace")));
        System.out.println(updateResult.getModifiedCount());
        assertEquals(1, updateResult.getModifiedCount());

		{
			final Person somebody = collection.find(gte("age", 23)).first();
			assertEquals("Ada Lovelace", somebody.getName());
			assertEquals(ada.getId(), somebody.getId());
		}

		// Replace One
        final UpdateResult replaceResult = collection.replaceOne(eq("name", "Ada Lovelace"), ada);
        System.out.println(replaceResult.getModifiedCount());
        assertEquals(1, replaceResult.getModifiedCount());

        // Delete One
        final DeleteResult deleteResult = collection.deleteOne(eq("name", "Ada Byron"));
        assertEquals(1, deleteResult.getDeletedCount());

	}

}

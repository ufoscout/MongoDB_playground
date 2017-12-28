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
package com.example.demo.sync_pojo;

import org.bson.types.ObjectId;

public final class Person {

	private ObjectId id;
	private String name;
	private int age;
	private Address address;

	public Person() {
	}

	public Person(final String name, final int age, final Address address) {
		this.id = new ObjectId();
		this.name = name;
		this.age = age;
		this.address = address;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(final ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(final int age) {
		this.age = age;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(final Address address) {
		this.address = address;
	}

	// Rest of implementation
}
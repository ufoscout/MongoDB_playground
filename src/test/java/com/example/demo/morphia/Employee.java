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

import java.util.ArrayList;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Reference;

@Entity("employees")
@Indexes(@Index(fields = @Field("salary")))
public class Employee {

	@Id
	private ObjectId id;
	private String name;
	@Reference
	private Employee manager;
	@Reference
	private List<Employee> directReports = new ArrayList<>();
	@Property("wage")
	private Double salary;

	public Employee() {
	}

	public Employee(final String name, final Double salary) {
		this.name = name;
		this.salary = salary;
	}

	public Employee(ObjectId id, String name, Employee manager, List<Employee> directReports, Double salary) {
		this.id = id;
		this.name = name;
		this.manager = manager;
		this.directReports = directReports;
		this.salary = salary;
	}

	public ObjectId getId() {
		return id;
	}

	public void setId(ObjectId id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Employee getManager() {
		return manager;
	}

	public void setManager(Employee manager) {
		this.manager = manager;
	}

	public List<Employee> getDirectReports() {
		return directReports;
	}

	public void setDirectReports(List<Employee> directReports) {
		this.directReports = directReports;
	}

	public Double getSalary() {
		return salary;
	}

	public void setSalary(Double salary) {
		this.salary = salary;
	}

}
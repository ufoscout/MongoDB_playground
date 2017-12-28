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
package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;

@Configuration
public class MongoConfig {

	private final String host = "127.0.0.1";
	private final int port = 27017;

	@Bean
	public MongoClient mongoClient() {

		// The MongoClient is Thread safe
		return new MongoClient( host , port );

//		 or, to connect to a replica set, with auto-discovery of the primary
//		new MongoClient(Arrays.asList(new ServerAddress("localhost", 27017),
//				                      new ServerAddress("localhost", 27018),
//		                              new ServerAddress("localhost", 27019),
//		                              etc...)
	}

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
    	final String dataBaseName = "SpringDataDB";
        return new MongoTemplate(mongoClient(), dataBaseName);
    }

}

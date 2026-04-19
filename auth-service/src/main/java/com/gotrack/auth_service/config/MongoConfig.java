package com.gotrack.auth_service.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@Configuration
public class MongoConfig {
    @Autowired
    private Environment environment;

    @Bean
    public MongoClient mongoClient() {
        
        


        String mongoUri = environment.getProperty("spring.data.mongodb.uri");
        if (mongoUri == null) {
            mongoUri = environment.getProperty("SPRING_DATA_MONGODB_URI");
        }
        if (mongoUri == null) {
            mongoUri = "mongodb://localhost:27017";
        }
        return MongoClients.create(mongoUri);

    }
}

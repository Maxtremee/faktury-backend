package com.pwr.faktury;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication()
@EnableMongoRepositories()
public class FakturyApplication {

	public static void main(String[] args) {
		SpringApplication.run(FakturyApplication.class, args);
	}

}

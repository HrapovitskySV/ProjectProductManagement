package ru.otus.hw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.SQLException;

@SpringBootApplication
public class Application {
	// http://localhost:8081/
	// http://localhost:8081/api/tasks

	public static void main(String[] args)  throws SQLException {
		SpringApplication.run(Application.class, args);
	}

}

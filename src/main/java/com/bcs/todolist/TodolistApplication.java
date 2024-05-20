package com.bcs.todolist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.*;

@SpringBootApplication
public class TodolistApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodolistApplication.class, args);

		String JDBC_URL="jdbc:postgresql://localhost:5432/todolist";

		try (
				Connection conn = DriverManager.getConnection(JDBC_URL, "postgres", "root");
				Statement stmt = conn.createStatement();
				PreparedStatement preparedStmt = conn.prepareStatement("SELECT * FROM role WHERE id = ?");
		) {
			stmt.executeUpdate("CREATE TABLE IF NOT EXISTS role (id SERIAL PRIMARY KEY, name VARCHAR(255))");
			System.out.println("Role table created");

			stmt.executeUpdate("INSERT INTO role (name) VALUES ('admin')");

			ResultSet rs = stmt.executeQuery("SELECT * FROM role");
			while(rs.next()) {
				System.out.println(rs.getLong("id") + ": " + rs.getString("name"));
			}

			preparedStmt.setInt(1, 2);
			ResultSet preparedRs = preparedStmt.executeQuery();
			while(preparedRs.next()) {
				System.out.println("Prepared stmt - " + preparedRs.getLong("id") + ": " + preparedRs.getString("name"));
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
}

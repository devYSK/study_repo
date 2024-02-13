package com.example.performance;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PerformanceasyncApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformanceasyncApplication.class, args);
    }

    // private static final String URL = "jdbc:mysql://localhost:3306/ngrinder?useSSL=false&serverTimezone=UTC";
    // private static final String USER = "root";
    // private static final String PASSWORD = "root";
    //
    // public static void insertDummyData(int numberOfRecords) {
    //     String sql = "INSERT INTO notice(title, content, who, createDate, updateDate) VALUES (?, ?, ?, NOW(), NOW())";
    //
    //     try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
    //          PreparedStatement pstmt = conn.prepareStatement(sql)) {
    //
    //         for (int i = 1; i <= numberOfRecords; i++) {
    //             pstmt.setString(1, "Title " + i);
    //             pstmt.setString(2, "Content for notice " + i);
    //             pstmt.setString(3, "User" + (i % 10 + 1)); // Assuming 10 different users
    //
    //             pstmt.executeUpdate();
    //         }
    //
    //         System.out.println(numberOfRecords + " dummy records inserted successfully.");
    //     } catch (SQLException e) {
    //         e.printStackTrace();
    //     }
    // }
    //
    // public static void main(String[] args) {
    //     insertDummyData(10000); // Insert 1000 dummy records
    // }
}

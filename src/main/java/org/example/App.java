package org.example;

import java.sql.*;

public class App {
    private static final String USER_NAME = "postgres";
    private static final String PASSWORD = "postgres";
    private static final String URI = "jdbc:postgresql://localhost:5432/studentdb";

    public static void main(String[] args) {
        Student student = new Student();
        student.setFirstName("Nickolay");
        student.setSecondName("Koshkin");
        getStudents(student);

    }

    private static void getStudents(Student student) {
        try (Connection connection = DriverManager.getConnection(URI, USER_NAME, PASSWORD)) {
            System.out.println("Соединение с БД");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * FROM students " +
                    "WHERE firstname='%s' AND lastname='%s';", student.getFirstName(), student.getSecondName()));
            if (resultSet.isBeforeFirst()) {
                System.out.println("По данному запросу найдено:");
                System.out.printf("%-20s %-20s %-20s\n", "Id", "Имя", "Фамилия");
                while (resultSet.next()) {
                    System.out.printf(
                            "%-20s %-20s %-20s\n",
                            resultSet.getString("id"),
                            resultSet.getString("firstname"),
                            resultSet.getString("lastname"));
                }
            } else {
                statement.execute(String.format("INSERT INTO students (firstname, lastname) " +
                        "VALUES ('%s', '%s');", student.getFirstName(), student.getSecondName()));
                System.out.println("Студент не найден и добавлен в БД");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

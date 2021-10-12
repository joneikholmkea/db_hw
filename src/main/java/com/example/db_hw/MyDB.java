package com.example.db_hw;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MyDB {
    String username = "root";
    String password = DbHwApplication.password;
    String url = "jdbc:mysql://"+ DbHwApplication.ip +":3306";  // private EC2 ip address
    //String url = "jdbc:mysql://172.31.49.98:3306";  // private EC2 ip address
//    String url = "jdbc:mysql://1.2.3.4:3306/?useSSL=false";
    String schemaName = "mydb";
    String tableName = "persons"; // comment
    List<String > persons = new ArrayList();
    private Connection conn;

    public MyDB(){
        connectAndQuery();
    }

    private void connectAndQuery(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try{
            conn = DriverManager.getConnection(url, username,password);
            if(!conn.isClosed()){
                System.out.println("DB Conn ok ");
                initializeDatabase(conn);
            }
        }catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }
    }

    private Connection getConnection(){
        try {
            return DriverManager.getConnection(url, username,password);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void getAllUsers() {
        //                // Get the rows:
        String sql = "SELECT * FROM " + tableName;
        ResultSet resultSet = null;
        persons.clear();
        try {
            conn = getConnection();
            Statement statement = conn.createStatement();
            statement.execute("USE " + schemaName );
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()){
                String firstName = resultSet.getString("name");
                System.out.println("Name: " + firstName);
                persons.add(firstName);
            }
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void addUser(){
        String sql = "INSERT IGNORE INTO " + tableName + " VALUES (null, 'User')";
        try {
            conn = getConnection();
            Statement statement = conn.createStatement();
            statement.execute("USE " + schemaName );
            statement.execute(sql);
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initializeDatabase(Connection conn) throws Exception{
        // 1. make sure there exists a schema, named mydb. If not, create one
        String sql = "CREATE DATABASE IF NOT EXISTS " + schemaName;
        Statement statement = conn.createStatement();
        statement.execute(sql);
        statement.execute("USE " + schemaName );
        System.out.println("done selecting schema");
        // 2. make sure there exists a table, named persons. If not, create one:
        //    Primary key: idpersons INT AUTO_INCREMENT
        //    Column: name VARCHAR(45)
        sql = "CREATE TABLE IF NOT EXISTS `" + schemaName + "`.`persons` (\n" +
                "  `idpersons` INT NOT NULL AUTO_INCREMENT,\n" +
                "  `name` VARCHAR(45) NULL,\n" +
                "  PRIMARY KEY (`idpersons`));";
        statement.execute(sql); // DDL

        // 3. If there was no table named persons, then insert two rows into the new table: "Anna" and "Bent"
        // lav denne øvelse indtil kl 9.05
        sql = "INSERT IGNORE INTO " + tableName + " VALUES (1, 'Anna')";
        statement.execute(sql);
        sql = "INSERT IGNORE INTO " + tableName + " VALUES (2, 'Bent')";
        statement.execute(sql);
        conn.close();
    }

    public List<String> getPersons() {
        return persons;
    }

}

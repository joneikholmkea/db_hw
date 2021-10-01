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
    String schemaName = "mydb2";
    String tableName = "persons";
    List<String > persons = new ArrayList();

    public MyDB(){
        connectAndQuery();
    }

    private void connectAndQuery(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        try(Connection conn = DriverManager.getConnection(url, username,password)){
            if(!conn.isClosed()){
                System.out.println("DB Conn ok ");
                initializeDatabase(conn);
//                // Get the rows:
                String sql = "SELECT * FROM " + tableName;
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet resultSet = ps.executeQuery();
                persons.clear();
                while (resultSet.next()){
                    String firstName = resultSet.getString("name");
                    System.out.println("Name: " + firstName);
                    persons.add(firstName);
                }
            }
        }catch (Exception e){
            System.out.println("Error " + e.getMessage());
        }
    }

    private void initializeDatabase(Connection conn) throws Exception{
        // 1. make sure there exists a schema, named mydb. If not, create one
        String sql = "CREATE DATABASE IF NOT EXISTS " + schemaName;
        Statement statement = conn.createStatement();
        statement.execute(sql);
        statement.execute("USE " + schemaName );

        // 2. make sure there exists a table, named persons. If not, create one:
        //    Primary key: idpersons INT AUTO_INCREMENT
        //    Column: name VARCHAR(45)
        sql = "CREATE TABLE IF NOT EXISTS `mydb2`.`persons` (\n" +
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
    }

    public List<String> getPersons() {
        return persons;
    }

}

package org.example;

import javax.validation.constraints.Null;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Properties;

public final class DBConnection {

    private static DBConnection instance = null;
    private Connection connection;

    private DBConnection() {

    }

    public static DBConnection getInstance() {
        if (instance == null) {
            synchronized (DBConnection.class) {
                if (instance == null) {
                    instance = new DBConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        if (connection==null) {
            synchronized (this) {
                if (connection==null) {
                    try {
                        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/example",
                                "postgres", "postgres");
						//connection = DriverManager.getConnection("postgres://aflojvdvygwmed:5f5cf7056381117a5d1d7212a4da7a7d8e269828b6f8df0228f3827203750791@ec2-54-228-209-117.eu-west-1.compute.amazonaws.com:5432/d6p463nif4scp8","aflojvdvygwmed","5f5cf7056381117a5d1d7212a4da7a7d8e269828b6f8df0228f3827203750791");		
                    }
                    catch (Exception e) {
                        System.out.println("Cannot connect to db.");
                        e.printStackTrace();
                    }
                }
            }
        }
        return connection;
    }
}

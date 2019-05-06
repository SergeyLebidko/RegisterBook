package registerbook.data_access_components;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import static registerbook.ResourcesList.*;

public class DBHandler {

    private Statement statement;

    public DBHandler() throws Exception {
        Class.forName(jdbcClassName);
        Connection connection = DriverManager.getConnection(databaseConnectionString);
        statement = connection.createStatement();
    }

}

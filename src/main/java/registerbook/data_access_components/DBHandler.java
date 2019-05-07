package registerbook.data_access_components;

import java.sql.*;
import java.util.ArrayList;

import static registerbook.ResourcesList.*;

public class DBHandler {

    private Statement statement;

    public DBHandler() throws Exception {
        Class.forName(jdbcClassName);
        Connection connection = DriverManager.getConnection(databaseConnectionString);
        statement = connection.createStatement();
    }

    public ArrayList<Object[]> getCatalog() throws Exception {
        String query = "SELECT * FROM CATALOG";
        ResultSet resultSet = statement.executeQuery(query);
        return convertSetToList(resultSet);
    }

    public ArrayList<Object[]> getOperations() throws Exception{
        String query = "SELECT OPERATIONS.ID, DATE, NAME, COUNT " +
                "FROM CATALOG, OPERATIONS " +
                "WHERE CATALOG.ID=OPERATIONS.CATALOG_ID " +
                "ORDER BY DATE(DATE) ASC";
        ResultSet resultSet = statement.executeQuery(query);
        return convertSetToList(resultSet);
    }

    private ArrayList<Object[]> convertSetToList(ResultSet resultSet) throws Exception {
        ArrayList<Object[]> list = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()){
            Object[] row = new Object[columnCount];
            for (int i=1;i<=columnCount;i++){
                row[i]=resultSet.getObject(i);
            }
            list.add(row);
        }

        return list;
    }

}

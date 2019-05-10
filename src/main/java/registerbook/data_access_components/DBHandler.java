package registerbook.data_access_components;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

import static registerbook.ResourcesList.*;

public class DBHandler {

    private Statement statement;

    public DBHandler() throws Exception {
        Class.forName(jdbcClassName);
        Connection connection = DriverManager.getConnection(databaseConnectionString);
        statement = connection.createStatement();
    }

    public ArrayList<Object[]> getCatalog() throws Exception {
        String query = "SELECT * FROM CATALOG ORDER BY NAME";
        ResultSet resultSet = statement.executeQuery(query);
        return convertSetToList(resultSet);
    }

    public ArrayList<Object[]> getOperations() throws Exception {
        String query = "SELECT OPERATIONS.ID, DATE, NAME, COUNT " +
                "FROM CATALOG, OPERATIONS " +
                "WHERE CATALOG.ID=OPERATIONS.CATALOG_ID " +
                "ORDER BY DATE(DATE) ASC";
        ResultSet resultSet = statement.executeQuery(query);
        return convertSetToList(resultSet);
    }

    public void addCatalogElement(String name) throws Exception {
        String query = "INSERT INTO CATALOG (NAME) VALUES (\"" + name + "\")";
        statement.executeUpdate(query);
    }

    public void addOperationsElement(String date, int count, int catalogId) throws Exception {
        String startTransactionQuery = "BEGIN TRANSACTION";
        String insertQuery = "INSERT INTO OPERATIONS (CATALOG_ID, DATE, COUNT) " +
                "VALUES (" + catalogId + ", \"" + date + "\", " + count + ")";
        String checkQuery = "SELECT SUM(COUNT) FROM OPERATIONS WHERE CATALOG_ID=" + catalogId + " GROUP BY DATE";
        String abortTransaction = "ROLLBACK";
        String endTransactionQuery = "COMMIT";

        try {
            statement.executeUpdate(startTransactionQuery);
            statement.executeUpdate(insertQuery);

            //Проверяем корректность операции. Если она некорректа - делаем откат
            if (count < 0) {
                ResultSet resultSet = statement.executeQuery(checkQuery);
                ArrayList<Object[]> list = convertSetToList(resultSet);
                int sum = 0;
                for (Object[] row : list) {
                    sum += (Integer) row[0];
                    if (sum < 0) {
                        statement.executeUpdate(abortTransaction);
                        throw new Exception(valueIsNotCorrect);
                    }
                }
            }

            statement.executeUpdate(endTransactionQuery);
        } catch (SQLException ex) {
            throw new Exception(failAddToOperations);
        }
    }

    private ArrayList<Object[]> convertSetToList(ResultSet resultSet) throws Exception {
        ArrayList<Object[]> list = new ArrayList<>();

        ResultSetMetaData metaData = resultSet.getMetaData();
        int columnCount = metaData.getColumnCount();

        while (resultSet.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 1; i <= columnCount; i++) {
                row[i - 1] = resultSet.getObject(i);
            }
            list.add(row);
        }

        return list;
    }

    private void showList(ArrayList<Object[]> list) {
        if (list.isEmpty()) {
            System.out.println("Список не содержит строк...");
            return;
        }
        System.out.println("Список:");
        for (Object[] row : list) {
            System.out.println(Arrays.toString(row));
        }
        System.out.println();
        System.out.println();
    }

}

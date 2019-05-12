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
            throw new Exception(failAddOperationElement);
        }
    }

    public void removeCatalogElement(int id) throws Exception {
        String query = "DELETE FROM CATALOG WHERE ID=" + id;
        statement.executeUpdate(query);
    }

    public void removeOperationsElement(int id) throws Exception {
        String startTransactionQuery = "BEGIN TRANSACTION";
        String removeQuery = "DELETE FROM OPERATIONS WHERE ID=" + id;
        String abortTransaction = "ROLLBACK";
        String endTransactionQuery = "COMMIT";

        try {
            int catalogId = getCatalogId(id);
            statement.executeUpdate(startTransactionQuery);
            statement.executeUpdate(removeQuery);

            //Проверяем корректность операции. Если она некорректа - делаем откат
            String checkQuery = "SELECT SUM(COUNT) FROM OPERATIONS WHERE CATALOG_ID=" + catalogId + " GROUP BY DATE";

            ResultSet resultSet = statement.executeQuery(checkQuery);
            ArrayList<Object[]> list = convertSetToList(resultSet);

            int sum = 0;
            for (Object[] row : list) {
                sum += (Integer) row[0];
                if (sum < 0) {
                    statement.executeUpdate(abortTransaction);
                    throw new Exception(operationIsNotBeRemove);
                }
            }

            statement.executeUpdate(endTransactionQuery);
        } catch (SQLException ex) {
            throw new Exception(failRemoveOperationElement);
        }

    }

    public void editCatalogElement(int id, String nextName) throws Exception {
        String query = "UPDATE CATALOG SET NAME=\"" + nextName + "\" WHERE ID=" + id;
        statement.executeUpdate(query);
    }

    public void editOperationsElement(int id, int nextCount, String nextDate) throws Exception {
        String startTransactionQuery = "BEGIN TRANSACTION";
        String updateQuery = "UPDATE OPERATIONS SET DATE=\"" + nextDate + "\", COUNT=" + nextCount + " " +
                "WHERE ID=" + id;
        String abortTransaction = "ROLLBACK";
        String endTransactionQuery = "COMMIT";

        try {
            statement.executeUpdate(startTransactionQuery);
            statement.executeUpdate(updateQuery);

            //Проверяем корректность операции. Если она некорректа - делаем откат
            int catalogId;
            catalogId = getCatalogId(id);
            String checkQuery = "SELECT SUM(COUNT) FROM OPERATIONS WHERE CATALOG_ID=" + catalogId + " GROUP BY DATE";

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

            statement.executeUpdate(endTransactionQuery);
        } catch (SQLException ex) {
            throw new Exception(failUpdateOperationsElement + " " + ex.getMessage());
        }
    }

    public ArrayList<Object[]> getRemainsReport(String dateStr) throws Exception {
        String query = "SELECT NAME, SUM(COUNT) " +
                "FROM CATALOG, OPERATIONS " +
                "WHERE CATALOG.ID=CATALOG_ID AND DATE(DATE)<=DATE('" + dateStr + "') " +
                "GROUP BY NAME " +
                "ORDER BY NAME";

        System.out.println(query);

        ResultSet resultSet = statement.executeQuery(query);
        return convertSetToList(resultSet);
    }

    public ArrayList<Object[]> getTurnoverReport(int catalogId) throws Exception{
        String query="SELECT DATE, " +
                "CASE WHEN COUNT>0 THEN COUNT ELSE '' END, " +
                "CASE WHEN COUNT<=0 THEN (-1)*COUNT ELSE '' END "  +
                "FROM OPERATIONS " +
                "WHERE CATALOG_ID="+catalogId+" " +
                "ORDER BY DATE(DATE)";

        ResultSet resultSet = statement.executeQuery(query);
        return convertSetToList(resultSet);
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

    private int getCatalogId(int operationId) throws Exception {
        String query = "SELECT CATALOG_ID FROM OPERATIONS WHERE ID=" + operationId;
        ResultSet resultSet = statement.executeQuery(query);
        ArrayList<Object[]> list = convertSetToList(resultSet);
        int result = (Integer) list.get(0)[0];
        return result;
    }

}

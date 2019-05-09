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

        //Проверяем корректность введенных данных согласно уже имеющейся в БД информации
        //Данные считаются корректными, если не делают остатки на дату их добавления отрицательными
        if (count < 0) {
            String tmpQuery = "SELECT SUM(COUNT) " +
                    "FROM OPERATIONS " +
                    "WHERE CATALOG_ID=" + catalogId + " AND DATE(DATE)<=DATE(\"" + date + "\") " +
                    "ORDER BY DATE(DATE)";

            ResultSet tmpSet;
            try {
                tmpSet = statement.executeQuery(tmpQuery);
            }catch (Exception ex){
                throw new Exception(failCheckCount);
            }

            Integer sumCount = getIntegerValue(tmpSet);
            if (sumCount == null) {
                sumCount = 0;
            }
            if ((sumCount + count) < 0) {
                throw new Exception(notCorrectCount);
            }
        }

        //Пытаемся выполнить запрос на добавление строки в БД
        String insertQuery = "INSERT INTO OPERATIONS (CATALOG_ID, DATE, COUNT) " +
                "VALUES (" + catalogId + ", \"" + date + "\", " + count + ")";
        try {
            statement.executeUpdate(insertQuery);
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

    //Данный метод предназначен для извлечения целого числа из результата SQL-запроса
    //Предполагается, что результат содержит только это число и больше никаких других данных в нем нет
    private Integer getIntegerValue(ResultSet resultSet) throws Exception {
        ArrayList<Object[]> list = convertSetToList(resultSet);
        Object value = list.get(0)[0];
        return value == null ? null : (Integer) value;
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

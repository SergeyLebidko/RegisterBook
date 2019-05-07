package registerbook;

import registerbook.data_access_components.DBHandler;
import registerbook.table_component.DataSet;
import registerbook.table_component.DataTable;

import static registerbook.ResourcesList.*;

import javax.swing.*;
import java.util.ArrayList;

public class ActionHandler {

    private DBHandler dbHandler;

    public static final String OPEN_CATALOG_COMMAND = "open catalog";
    public static final String OPEN_OPERATIONS_COMMAND = "open operations";


    private static final String CATALOG_DATASET = "catalog";
    private static final String OPERATIONS_DATASET = "operations";

    private DataTable mainTable;

    //В переменной хранится наименование набора данных, отображаемого в настоящий момент
    //Это может быть таблица базы данных, либо отчет
    private String state;

    public ActionHandler() {
        dbHandler = MainClass.getDbHandler();
        state = "";
    }

    public void setMainTable(DataTable mainTable) {
        this.mainTable = mainTable;
    }

    public void commandHandler(String command) {

        //Выводим каталог
        if (command.equals(OPEN_CATALOG_COMMAND)) {
            try {
                setMainTableContent(CATALOG_DATASET);
                state = CATALOG_DATASET;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, doNotOpenCatalog, "Ошибка", JOptionPane.ERROR_MESSAGE);
                state="";
                return;
            }
            return;
        }

        //Выводим журнал операций
        if (command.equals(OPEN_OPERATIONS_COMMAND)) {
            try {
                setMainTableContent(OPERATIONS_DATASET);
                state = OPERATIONS_DATASET;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, doNotOpenOperations, "Ошибка", JOptionPane.ERROR_MESSAGE);
                state="";
                return;
            }
            return;
        }

    }

    private void setMainTableContent(String nameDataSet) throws Exception {
        mainTable.refresh(getDataSet(nameDataSet));
    }

    private DataSet getDataSet(String nameDataSet) throws Exception {

        ArrayList<Object[]> list = null;
        DataSet dataSet = null;

        //Открываем таблицу "каталог"
        if (nameDataSet.equals(CATALOG_DATASET)) {
            list = dbHandler.getCatalog();
            dataSet = convertListToDataSet(list);
            dataSet.setColumnNames(new String[]{"Наименование"});
            dataSet.setDisplayName("Каталог");
        }

        //Открываем таблицу "журнал операций"
        if (nameDataSet.equals(OPERATIONS_DATASET)) {
            list = dbHandler.getOperations();
            dataSet = convertListToDataSet(list);
            dataSet.setColumnNames(new String[]{"Дата", "Наименование", "Количество"});
            dataSet.setDisplayName("Журнал операций");
        }

        return dataSet;
    }

    private DataSet convertListToDataSet(ArrayList<Object[]> list) {
        Object[][] data = new Object[list.size()][];
        int i = 0;
        for (Object[] row : list) {
            data[i] = row;
            i++;
        }
        return new DataSet(data);
    }

}

package registerbook;

import registerbook.data_access_components.DBHandler;
import registerbook.table_component.DataSet;
import registerbook.table_component.DataTable;

import java.util.ArrayList;

public class ActionHandler {

    private DBHandler dbHandler;

    public static final String CATALOG = "catalog";
    public static final String OPERATIONS = "operations";

    private DataTable mainTable;

    public ActionHandler() {
        dbHandler = MainClass.getDbHandler();
    }

    public void setMainTable(DataTable mainTable) {
        this.mainTable = mainTable;
    }

    public void setMainTableContent(String nameDataSet){
        mainTable.refresh(getDataSet(nameDataSet));
    }

    private DataSet getDataSet(String nameDataSet) {

        ArrayList<Object[]> list = null;
        DataSet dataSet = null;

        //Вывести таблицу "каталог"
        if (nameDataSet.equals(CATALOG)) {
            try {
                list = dbHandler.getCatalog();
            } catch (Exception e) {
                // ********** Вставить код обработки ошибки **********
            }
            dataSet = convertListToDataSet(list);
            dataSet.setColumnNames(new String[]{"Наименование"});
            dataSet.setDisplayName("Каталог");
        }

        //Вывести таблицу "журнал операций"
        if (nameDataSet.equals(OPERATIONS)) {
            try {
                list = dbHandler.getOperations();
            } catch (Exception e) {
                // ********** Вставить код обработки ошибки **********
            }
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

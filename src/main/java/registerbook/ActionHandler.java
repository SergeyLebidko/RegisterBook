package registerbook;

import registerbook.data_access_components.DBHandler;
import registerbook.table_component.*;

import static registerbook.ResourcesList.*;

import javax.swing.*;
import java.util.ArrayList;

public class ActionHandler {

    private DBHandler dbHandler;

    public static final String OPEN_CATALOG_COMMAND = "open catalog";
    public static final String OPEN_OPERATIONS_COMMAND = "open operations";
    public static final String ADD_COMMAND = "add";

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

        //Добавляем новую позицию в каталог
        if (command.equals(ADD_COMMAND) & state.equals(CATALOG_DATASET)){
            System.out.println("Добавляем в каталог");
        }

        //Добавляем новую операцию в журнал операций
        if (command.equals(ADD_COMMAND) & state.equals(OPERATIONS_DATASET)){
            System.out.println("Добавляем в журнал операций");
        }

    }

    private void setMainTableContent(String nameDataSet) throws Exception {
        mainTable.refresh(createTableContent(nameDataSet));
    }

    private TableContent createTableContent(String nameTableContent) throws Exception {

        ArrayList<Object[]> list;
        TableContent tableContent = null;

        //Открываем таблицу "каталог"
        if (nameTableContent.equals(CATALOG_DATASET)) {
            list = dbHandler.getCatalog();
            tableContent = new TableContent(list);
            tableContent.setColumnEnableds(false, true);
            tableContent.setColumnNames("", "Наименование");
            tableContent.setDisplayName("Каталог");
        }

        //Открываем таблицу "журнал операций"
        if (nameTableContent.equals(OPERATIONS_DATASET)) {
            list = dbHandler.getOperations();
            tableContent = new TableContent(list);
            tableContent.setColumnEnableds(false, true, true, true);
            tableContent.setColumnNames("","Дата", "Наименование", "Количество");
            tableContent.setDisplayName("Журнал операций");
        }

        return tableContent;
    }


}

package registerbook;

import registerbook.data_access_components.DBHandler;
import registerbook.table_component.*;

import static registerbook.ResourcesList.*;

import javax.swing.*;
import java.sql.SQLException;
import java.util.ArrayList;

public class ActionHandler {

    private DBHandler dbHandler;

    public static final String OPEN_CATALOG_COMMAND = "open catalog";
    public static final String OPEN_OPERATIONS_COMMAND = "open operations";
    public static final String ADD_COMMAND = "add";

    private static final String CATALOG_DATASET = "catalog";
    private static final String OPERATIONS_DATASET = "operations";

    private DTablePane mainTable;

    //В переменной хранится наименование набора данных, отображаемого в настоящий момент
    //Это может быть таблица базы данных, либо отчет
    private String state;

    public ActionHandler() {
        dbHandler = MainClass.getDbHandler();
        state = "";
    }

    public void setMainTable(DTablePane mainTable) {
        this.mainTable = mainTable;
    }

    public void commandHandler(String command) {

        //Выводим каталог
        if (command.equals(OPEN_CATALOG_COMMAND)) {
            try {
                setMainTableContent(createTableContent(CATALOG_DATASET));
                state = CATALOG_DATASET;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, doNotOpenCatalog, "Ошибка", JOptionPane.ERROR_MESSAGE);
                state = "";
                return;
            }
            return;
        }

        //Выводим журнал операций
        if (command.equals(OPEN_OPERATIONS_COMMAND)) {
            try {
                setMainTableContent(createTableContent(OPERATIONS_DATASET));
                state = OPERATIONS_DATASET;
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, doNotOpenOperations, "Ошибка", JOptionPane.ERROR_MESSAGE);
                state = "";
                return;
            }
            return;
        }

        //Добавляем новую позицию в каталог
        if (command.equals(ADD_COMMAND) & state.equals(CATALOG_DATASET)) {
            String name = getNameString();
            if (name==null)return;
            try {
                dbHandler.addNewNameToCatalog(name);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, failAddToCatalog+" "+name, "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            commandHandler(OPEN_CATALOG_COMMAND);
            return;
        }

        //Добавляем новую операцию в журнал операций
        if (command.equals(ADD_COMMAND) & state.equals(OPERATIONS_DATASET)) {
            System.out.println("Добавляем в журнал операций");
        }

    }

    private void setMainTableContent(DTableContent tableContent) throws Exception {
        mainTable.refresh(tableContent);
    }

    private String getNameString() {
        String disabledChars = "_%*\"?'";
        boolean findDeniedChar;
        String name;

        do {
            name = JOptionPane.showInputDialog(null, "Введите имя");
            if (name == null) return null;
            name = name.trim();
            if (name.equals("")) {
                JOptionPane.showMessageDialog(null, nameIsNotEmpty, "", JOptionPane.INFORMATION_MESSAGE);
                continue;
            }
            findDeniedChar = false;
            for (char c : name.toCharArray()) {
                if (disabledChars.indexOf(c) != (-1)) {
                    findDeniedChar = true;
                    break;
                }
            }
            if (findDeniedChar) {
                JOptionPane.showMessageDialog(null, nameContainsDisabledChars + " " + disabledChars, "", JOptionPane.INFORMATION_MESSAGE);
                continue;
            }
            break;
        } while (true);

        return name;
    }

    private DTableContent createTableContent(String nameTableContent) throws Exception {

        ArrayList<Object[]> list;
        DTableContent tableContent = null;

        //Открываем таблицу "каталог"
        if (nameTableContent.equals(CATALOG_DATASET)) {
            list = dbHandler.getCatalog();
            tableContent = new DTableContent(list);
            tableContent.setColumnEnableds(false, true);
            tableContent.setColumnNames("", "Наименование");
            tableContent.setDisplayName("Каталог");
        }

        //Открываем таблицу "журнал операций"
        if (nameTableContent.equals(OPERATIONS_DATASET)) {
            list = dbHandler.getOperations();
            tableContent = new DTableContent(list);
            tableContent.setColumnEnableds(false, true, true, true);
            tableContent.setColumnNames("", "Дата", "Наименование", "Количество");
            tableContent.setDisplayName("Журнал операций");
        }

        return tableContent;
    }


}

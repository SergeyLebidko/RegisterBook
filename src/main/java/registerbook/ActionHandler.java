package registerbook;

import com.github.lgooddatepicker.components.DatePicker;
import registerbook.data_access_components.DBHandler;
import registerbook.table_component.*;

import static registerbook.ResourcesList.*;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

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
            String name = showInputCatalogElementDialog();
            if (name == null) return;
            try {
                dbHandler.addNewNameToCatalog(name);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, failAddToCatalog + " " + name, "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            commandHandler(OPEN_CATALOG_COMMAND);
            return;
        }

        //Добавляем новую операцию в журнал операций
        if (command.equals(ADD_COMMAND) & state.equals(OPERATIONS_DATASET)) {
            try {
                Object[] data = showInputOperationDialog();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, doNotOpenOperations, "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

    }

    private void setMainTableContent(DTableContent tableContent) throws Exception {
        mainTable.refresh(tableContent);
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

    private boolean isCorrectNumberString(String numberString) {
        try {
            Integer.parseInt(numberString);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    // ********** Ниже идет список методов, отображающих различные диалоговые окна **********

    //Запрос имени нового элемента каталога
    private String showInputCatalogElementDialog() {
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

    //Запрос новой операции
    private Object[] showInputOperationDialog() throws Exception {
        //Создаем диалоговое окно
        JPanel dialogPane = new JPanel(new BorderLayout(5, 5));

        JPanel topPane = new JPanel(new GridLayout(0, 2));

        Box datePane = Box.createHorizontalBox();
        datePane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        Box countPane = Box.createHorizontalBox();
        countPane.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        DatePicker datePicker = new DatePicker();
        datePicker.setDateToToday();
        datePicker.getComponentDateTextField().setEditable(false);

        Dimension dim = datePicker.getPreferredSize();
        datePicker.setMaximumSize(new Dimension(dim.width, 25));

        JTextField countField = new JTextField(5);
        countField.setHorizontalAlignment(SwingConstants.RIGHT);

        dim = countField.getPreferredSize();
        countField.setMaximumSize(new Dimension(dim.width, 25));

        datePane.add(new JLabel("Дата операции:"));
        datePane.add(Box.createHorizontalStrut(10));
        datePane.add(Box.createHorizontalGlue());
        datePane.add(datePicker);

        countPane.add(new JLabel("Количество:"));
        countPane.add(Box.createHorizontalGlue());
        countPane.add(countField);

        topPane.add(datePane);
        topPane.add(Box.createHorizontalStrut(100));
        topPane.add(countPane);
        topPane.add(Box.createHorizontalStrut(100));

        dialogPane.add(topPane, BorderLayout.NORTH);

        DTableContent catalogContent = null;

        catalogContent = createTableContent(CATALOG_DATASET);

        DTablePane catalogPane = new DTablePane();
        catalogPane.refresh(catalogContent);
        catalogPane.setSingleSelectionMode();
        dialogPane.add(catalogPane.getVisualComponent(), BorderLayout.CENTER);

        //Запрашиваем данные у пользователя
        int answer;
        String date;
        int count;
        int id;
        while (true) {
            answer = JOptionPane.showConfirmDialog(null, dialogPane, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer != 0) return null;

            try {
                date = datePicker.getDate().toString();
                if (date == null) throw new Exception(selectCorrectDate);

                if (!isCorrectNumberString(countField.getText())) throw new Exception(inputCorrectCount);
                count = Integer.parseInt(countField.getText());

                if (catalogPane.getSelectionRows().isEmpty()) throw new Exception(noSelectedElements);
                id = (Integer) catalogPane.getSelectionRows().get(0)[0];

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                continue;
            }
            break;
        }

        //Формируем ответ
        Object[] result = new Object[3];
        result[0] = date;
        result[1] = count;
        result[2] = id;
        return result;
    }

}

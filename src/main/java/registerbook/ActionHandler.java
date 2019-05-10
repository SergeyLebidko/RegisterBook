package registerbook;

import com.github.lgooddatepicker.components.DatePicker;
import registerbook.data_access_components.DBHandler;
import registerbook.table_component.*;

import static registerbook.ResourcesList.*;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;

public class ActionHandler {

    private DBHandler dbHandler;

    public static final String OPEN_CATALOG_COMMAND = "open catalog";
    public static final String OPEN_OPERATIONS_COMMAND = "open operations";
    public static final String ADD_COMMAND = "add";
    public static final String REMOVE_COMMAND = "remove";
    public static final String EDIT_COMMAND = "edit";

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

        //Выводим Каталог
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

        //Выводим Журнал операций
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

        //Добавляем новую позицию в Каталог
        if (command.equals(ADD_COMMAND) & state.equals(CATALOG_DATASET)) {
            String name = showInputCatalogElementDialog();
            if (name == null) return;
            try {
                dbHandler.addCatalogElement(name);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, failAddCatalogElement, "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }
            commandHandler(OPEN_CATALOG_COMMAND);
            return;
        }

        //Добавляем новую операцию в журнал операций
        if (command.equals(ADD_COMMAND) & state.equals(OPERATIONS_DATASET)) {
            Object[] data;
            try {
                //Получаем данные от пользователя
                data = showInputOperationDialog();
                if (data == null) return;

                //Пытаемся добавить их в базу
                String date = (String) data[0];
                int count = (Integer) data[1];
                int catalogId = (Integer) data[2];
                dbHandler.addOperationsElement(date, count, catalogId);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Если всё прошло успешно - обновляем журнал операций на экране
            commandHandler(OPEN_OPERATIONS_COMMAND);
            return;
        }

        //Удаление из Каталога
        if (command.equals(REMOVE_COMMAND) & state.equals(CATALOG_DATASET)) {
            ArrayList<Object[]> list = mainTable.getSelectionRows();

            //Проверяем, есть ли выделенные элементы
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(null, noSelectedCatalogElements, "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Последовательно удаляем записи из таблицы Каталог
            int id;
            for (Object[] row : list) {
                id = (Integer) row[0];
                try {
                    dbHandler.removeCatalogElement(id);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, failRemoveCatalogElement + " " + row[1], "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }

            //Обновляем Каталог на экране
            commandHandler(OPEN_CATALOG_COMMAND);
            return;
        }

        //Удаление из Журнала операций
        if (command.equals(REMOVE_COMMAND) & state.equals(OPERATIONS_DATASET)) {
            ArrayList<Object[]> list = mainTable.getSelectionRows();

            //Проверяем, есть ли выделенные элементы
            if (list.isEmpty()) {
                JOptionPane.showMessageDialog(null, noSelectedOperationsElement, "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Последовательно удаляем записи из таблицы Журнал операций
            int id;
            for (Object[] row : list) {
                id = (Integer) row[0];
                try {
                    dbHandler.removeOperationsElement(id);
                } catch (Exception e) {
                    JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                }
            }

            //Обновляем журнал операций на экране
            commandHandler(OPEN_OPERATIONS_COMMAND);
            return;
        }

        //Редактирование элемента каталога
        if (command.equals(EDIT_COMMAND) & state.equals(CATALOG_DATASET)) {
            ArrayList<Object[]> list = mainTable.getSelectionRows();

            //Проверяем количество выделенных элементов
            if (list.isEmpty() | list.size() > 1) {
                JOptionPane.showMessageDialog(null, selectOneElement, "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Получаем новое имя для элемента
            String name;
            String nextName;
            int id;
            id = (Integer) list.get(0)[0];
            name = (String) list.get(0)[1];

            nextName = showInputCatalogElementDialog(name);
            if (nextName == null) return;

            //Пытаемся обновить имя в базе данных
            try {
                dbHandler.editCatalogElement(id, nextName);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, failUpdateCatalogElement, "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Если всё успешно - отображаем измененные данные
            commandHandler(OPEN_CATALOG_COMMAND);
            return;
        }

        //Редактирование элемента журнала операций
        if (command.equals(EDIT_COMMAND) & state.equals(OPERATIONS_DATASET)) {
            ArrayList<Object[]> list = mainTable.getSelectionRows();

            //Проверяем количество выделенных элементов
            if (list.isEmpty() | list.size() > 1) {
                JOptionPane.showMessageDialog(null, selectOneElement, "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            //Формируем запрос пользователю
            int id;
            String dateStr;
            String name;
            int count;

            id = (Integer) list.get(0)[0];
            dateStr = (String) list.get(0)[1];
            name = (String) list.get(0)[2];
            count = (Integer) list.get(0)[3];

            //Получаем ответ от пользователя
            Object[] row = showEditOperationDialog(dateStr, name, count);
            if (row == null) return;

            //Пытаемся записать изменения в базу данных
            int nextCount = (Integer) row[0];
            String nextDateStr = (String) row[1];

            try {
                dbHandler.editOperationsElement(id, nextCount, nextDateStr);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                return;
            }

            commandHandler(OPEN_OPERATIONS_COMMAND);
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

    private boolean isCorrectCountString(String numberString) {
        try {
            Integer.parseInt(numberString);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    // ********** Ниже идет список методов, отображающих различные диалоговые окна **********

    //Запрос имени элемента каталога
    private String showInputCatalogElementDialog() {
        return showInputCatalogElementDialog("");
    }

    private String showInputCatalogElementDialog(String startValue) {
        String disabledChars = "_%*\"?'";
        boolean findDeniedChar;
        String name;

        do {
            name = JOptionPane.showInputDialog(null, "Введите имя", startValue);
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

        //Получаем из БД список элементов Каталога
        try {
            catalogContent = createTableContent(CATALOG_DATASET);
        } catch (Exception e) {
            throw new Exception(doNotOpenCatalog);
        }

        //Включаем полученный список элементов Каталога в табличный компонент для вывода на экран
        DTablePane catalogPane = new DTablePane();
        catalogPane.refresh(catalogContent);
        catalogPane.setSingleSelectionMode();
        dialogPane.add(catalogPane.getVisualComponent(), BorderLayout.CENTER);

        //Запрашиваем данные у пользователя
        int answer;
        String date;
        int count;
        int catalogId;
        while (true) {
            answer = JOptionPane.showConfirmDialog(null, dialogPane, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer != 0) return null;

            try {
                date = datePicker.getDate().toString();

                if (!isCorrectCountString(countField.getText())) throw new Exception(inputCorrectCount);
                count = Integer.parseInt(countField.getText());

                if (count == 0) throw new Exception(valueMustBeNotZero);

                if (catalogPane.getSelectionRows().isEmpty()) throw new Exception(noSelectedCatalogElements);
                catalogId = (Integer) catalogPane.getSelectionRows().get(0)[0];

            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, selectCorrectDate, "Ошибка", JOptionPane.ERROR_MESSAGE);
                continue;
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
        result[2] = catalogId;
        return result;
    }

    //Диалоговое окно редактирования параметров операции
    private Object[] showEditOperationDialog(String dateStr, String name, int count) {
        JPanel dialogPane = new JPanel(new GridLayout(0, 1, 10, 10));

        Box nameBox = Box.createHorizontalBox();
        Box dateBox = Box.createHorizontalBox();
        Box countBox = Box.createHorizontalBox();

        nameBox.add(new JLabel(name));
        nameBox.add(Box.createHorizontalStrut(20));
        nameBox.add(Box.createHorizontalGlue());

        DatePicker datePicker = new DatePicker();
        datePicker.getComponentDateTextField().setEditable(false);

        int year = Integer.parseInt(dateStr.split("-")[0]);
        int month = Integer.parseInt(dateStr.split("-")[1]);
        int day = Integer.parseInt(dateStr.split("-")[2]);

        datePicker.setDate(LocalDate.of(year, month, day));

        dateBox.add(new JLabel("Дата операции: "));
        dateBox.add(Box.createHorizontalStrut(20));
        dateBox.add(Box.createHorizontalGlue());
        dateBox.add(datePicker);

        JTextField countField = new JTextField(5);
        countField.setText(count + "");
        countField.setHorizontalAlignment(SwingConstants.RIGHT);

        countBox.add(new JLabel("Количество:"));
        countBox.add(Box.createHorizontalStrut(20));
        countBox.add(Box.createHorizontalGlue());
        countBox.add(countField);

        dialogPane.add(nameBox);
        dialogPane.add(dateBox);
        dialogPane.add(countBox);

        int answer;
        String nextDateStr;
        int nextCount;
        while (true) {
            answer = JOptionPane.showConfirmDialog(null, dialogPane, "", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
            if (answer != 0) return null;

            try {
                nextDateStr = datePicker.getDate().toString();

                if (!isCorrectCountString(countField.getText())) throw new Exception(inputCorrectCount);
                nextCount = Integer.parseInt(countField.getText());

                if (count == 0) throw new Exception(valueMustBeNotZero);
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, selectCorrectDate, "Ошибка", JOptionPane.ERROR_MESSAGE);
                continue;
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage(), "Ошибка", JOptionPane.ERROR_MESSAGE);
                continue;
            }

            break;
        }

        //Формируем ответ
        Object[] result = new Object[2];
        result[0] = nextCount;
        result[1] = nextDateStr;
        return result;
    }

}

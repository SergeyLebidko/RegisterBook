package registerbook.table_component;

import java.util.ArrayList;

public class TableContent {

    private String displayName;
    private String[] columnNames;
    private boolean[] columnEnableds;

    private Object[][] data;
    private int rowCount;
    private int columnCount;

    public TableContent(ArrayList<Object[]> list) {
        //Получаем количество строк и столбцов в таблице
        rowCount = list.size();
        columnCount = 0;
        if (rowCount > 0) {
            columnCount = list.get(0).length;
        }

        //Переносим данные из списка в таблицу
        data = new Object[rowCount][columnCount];
        for (int row = 0; row < rowCount; row++) {
            for (int col = 0; col < columnCount; col++) {
                data[row][col] = list.get(row)[col];
            }
        }

        //Устанавливаем значения других параметров по-умолчанию
        displayName = "";

        columnNames = new String[columnCount];
        for (int col = 0; col < columnCount; col++) {
            columnNames[col] = "";
        }

        columnEnableds = new boolean[columnCount];
        for (int col = 0; col < columnCount; col++) {
            columnEnableds[col] = true;
        }
    }

    public void setColumnNames(String ... columnNames) {
        this.columnNames = columnNames;
    }

    public void setColumnEnableds(boolean ... columnEnableds) {
        this.columnEnableds = columnEnableds;

    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        columnIndex = getInternalColumnIndex(columnIndex);
        return data[rowIndex][columnIndex];
    }

    public String getColumnName(int columnIndex) {
        columnIndex = getInternalColumnIndex(columnIndex);
        return columnNames[columnIndex];
    }

    public int getRowCount() {
        return rowCount;
    }

    public int getColumnCount() {
        int enabledColumnCount = 0;
        for (boolean columnEnabled : columnEnableds) {
            if (columnEnabled) {
                enabledColumnCount++;
            }
        }
        return enabledColumnCount;
    }

    public Object[] getRowData(int rowIndex) {
        return data[rowIndex];
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    private int getInternalColumnIndex(int columnIndex) {
        int internalColumnIndex = -1;
        int enabledColumnCount = -1;

        for (boolean columnEnabled : columnEnableds) {
            internalColumnIndex++;
            if (columnEnabled) {
                enabledColumnCount++;
                if (enabledColumnCount == columnIndex) break;
            }
        }
        return internalColumnIndex;
    }

}

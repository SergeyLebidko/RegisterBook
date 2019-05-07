package registerbook.table_component;

public class DataSet {

    private String name;               //Имя набора данных
    private String displayName;        //Отображаемое имя набора данных
    private String[] columnNames;      //Имена столбцов

    private Object[][] data;

    public DataSet(Object[][] data) {
        this.data = data;

        int columnCount = data[0].length-1;
        columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = "Столбец";
        }

        name = "dataset";
        displayName = "dataset";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        int columnCount = data[0].length-1;
        if (columnNames.length!=columnCount)return;
        this.columnNames = columnNames;
    }

}

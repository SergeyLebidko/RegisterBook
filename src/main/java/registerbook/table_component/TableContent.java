package registerbook.table_component;

public class TableContent {

    private String displayName;        //Отображаемое имя набора данных
    private String[] columnNames;      //Имена столбцов

    private Object[][] data;

    public TableContent(){
        this(new Object[0][0]);
    }

    public TableContent(Object[][] data) {
        this.data = data;

        int columnCount=0;
        if (data.length>0){
            columnCount = data[0].length-1;
        }
        columnNames = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            columnNames[i] = (i+1)+"";
        }

        displayName = "";
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
        this.columnNames = columnNames;
    }

    public Object[][] getData() {
        return data;
    }

    public void setData(Object[][] data) {
        this.data = data;
    }

}

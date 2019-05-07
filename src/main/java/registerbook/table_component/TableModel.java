package registerbook.table_component;

import javax.swing.table.AbstractTableModel;

public class TableModel extends AbstractTableModel {

    private DataSet content;
    private int rowCount;
    private int columnCount;

    public TableModel() {
        content = null;
        rowCount = 0;
        columnCount = 0;
    }

    public void refresh(DataSet dataSet) {
        content = dataSet;
        Object[][] data = content.getData();

        rowCount=data.length;
        columnCount=0;
        if (data.length>0){
            columnCount = data[0].length-1;
        }

        fireTableDataChanged();
    }

    @Override
    public int getRowCount() {
        return rowCount;
    }

    @Override
    public int getColumnCount() {
        return columnCount;
    }

    @Override
    public String getColumnName(int column) {
        String[] columnNames = content.getColumnNames();
        return columnNames[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (content == null) return null;
        Object[][] data = content.getData();
        return data[rowIndex][columnIndex + 1];
    }

}

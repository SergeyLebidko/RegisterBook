package registerbook.table_component;

import javax.swing.table.AbstractTableModel;

public class DTableModel extends AbstractTableModel {

    private DTableContent content;
    private int rowCount;
    private int columnCount;

    public DTableModel() {
        content = null;
        rowCount = 0;
        columnCount = 0;
    }

    public void refresh(DTableContent tableContent) {
        content = tableContent;
        rowCount = tableContent.getRowCount();
        columnCount = tableContent.getColumnCount();
        fireTableDataChanged();
        fireTableStructureChanged();
    }

    public Object[] getRowData(int rowIndex) {
        return content.getRowData(rowIndex);
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
        return content.getColumnName(column);
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (content == null) return null;
        return content.getValueAt(rowIndex, columnIndex);
    }

}

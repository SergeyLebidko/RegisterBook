package registerbook.table_component;

import javax.swing.*;
import java.awt.*;

import static registerbook.ResourcesList.*;

public class DataTable {

    private JPanel contentPane;
    private JTable table;
    private TableModel tableModel;
    private TableCellRenderer cellRenderer;
    private TableHeaderRenderer headerRenderer;
    private JLabel nameLab;

    public DataTable() {
        createTable();
        createVisualComponent();
    }

    public void refresh(TableContent tableContent){
        tableModel.refresh(tableContent);
        nameLab.setText(tableContent.getDisplayName());
    }

    private void createTable(){
        tableModel = new TableModel();
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);

        cellRenderer = new TableCellRenderer();
        headerRenderer = new TableHeaderRenderer();
        table.setDefaultRenderer(Object.class, cellRenderer);
        table.getTableHeader().setDefaultRenderer(headerRenderer);

        table.setShowVerticalLines(false);
        table.setGridColor(gridColor);
        table.setRowHeight(rowHeight);
    }

    private void createVisualComponent(){
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        contentPane.setBorder(BorderFactory.createEmptyBorder(0,5,5,5));

        nameLab = new JLabel();
        nameLab.setFont(mainFont);
        Box nameBox = Box.createHorizontalBox();
        nameBox.add(nameLab);
        nameBox.add(Box.createHorizontalGlue());
        contentPane.add(nameBox, BorderLayout.NORTH);

        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    public JPanel getVisualComponent(){
        return contentPane;
    }

}

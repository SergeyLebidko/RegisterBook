package registerbook.table_component;

import javax.swing.*;
import java.awt.*;

import static registerbook.ResourcesList.*;

public class DTablePane {

    private JPanel contentPane;
    private JTable table;
    private DTableModel dataTableModel;
    private DTableCellRenderer cellRenderer;
    private DTableHeaderRenderer headerRenderer;
    private JLabel nameLab;

    public DTablePane() {
        createTable();
        createVisualComponent();
    }

    public void refresh(DTableContent tableContent){
        dataTableModel.refresh(tableContent);
        nameLab.setText(tableContent.getDisplayName());
    }

    private void createTable(){
        dataTableModel = new DTableModel();
        table = new JTable(dataTableModel);
        table.getTableHeader().setReorderingAllowed(false);

        cellRenderer = new DTableCellRenderer();
        headerRenderer = new DTableHeaderRenderer();
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

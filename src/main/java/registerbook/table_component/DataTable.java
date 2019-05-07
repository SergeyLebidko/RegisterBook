package registerbook.table_component;

import javax.swing.*;
import java.awt.*;

public class DataTable {

    private JPanel contentPane;
    private JTable table;
    private TableModel tableModel;

    public DataTable() {
        createTable();
        createVisualComponent();
    }

    private void createTable(){
        tableModel = new TableModel();
        table = new JTable(tableModel);
    }

    private void createVisualComponent(){
        contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        JScrollPane scrollPane = new JScrollPane(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);
    }

    public JPanel getVisualComponent(){
        return contentPane;
    }

}

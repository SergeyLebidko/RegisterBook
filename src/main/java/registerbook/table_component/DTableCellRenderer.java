package registerbook.table_component;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static registerbook.ResourcesList.*;

public class DTableCellRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lab = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        lab.setFont(mainFont);
        if (!isSelected){
            if ((row%2)==0){
                lab.setBackground(evenCellsColor);
            }
            if ((row%2)!=0){
                lab.setBackground(notEvenCellsColor);
            }
        }
        return lab;
    }

}

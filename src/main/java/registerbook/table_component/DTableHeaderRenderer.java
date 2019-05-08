package registerbook.table_component;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

import static registerbook.ResourcesList.*;

public class DTableHeaderRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JLabel lab = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        lab.setFont(mainFont);
        lab.setBackground(headerColor);
        lab.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        lab.setHorizontalAlignment(SwingConstants.CENTER);
        return lab;
    }

}

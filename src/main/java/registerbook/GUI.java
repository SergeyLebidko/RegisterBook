package registerbook;

import registerbook.table_component.DataTable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static registerbook.ResourcesList.*;

public class GUI {

    private JFrame frm;
    private ActionHandler actionHandler;

    private JToolBar toolBar;
    private JButton openBtn;
    private JButton addBtn;
    private JButton editBtn;
    private JButton removeBtn;
    private JButton reportBtn;

    private DataTable mainTable;

    public GUI() {
        createFrm();
        createToolbar();
        createMainTable();
        setMainTableStartContent();
        createOpenMenu();
        showFrm();
    }

    private void createFrm() {
        frm = new JFrame(frmTitle);
        frm.setIconImage(logoImage);
        frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frm.setSize(FRM_WIDTH, FRM_HEIGHT);
        frm.setMinimumSize(new Dimension(MIN_FRM_WIDTH, MIN_FRM_HEIGHT));
        int xPos = Toolkit.getDefaultToolkit().getScreenSize().width / 2 - FRM_WIDTH / 2;
        int yPos = Toolkit.getDefaultToolkit().getScreenSize().height / 2 - FRM_HEIGHT / 2;
        frm.setLocation(xPos, yPos);
        JPanel contentPane = new JPanel(new BorderLayout(5, 5));
        frm.setContentPane(contentPane);
    }

    private void createToolbar() {
        toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));

        openBtn = new JButton(openBtnText, openImage);
        openBtn.setToolTipText(openBtnToolTip);

        addBtn = new JButton(addBtnText, addImage);
        addBtn.setToolTipText(addBtnToolTip);

        editBtn = new JButton(editBtnText, editImage);
        editBtn.setToolTipText(editBtnToolTip);

        removeBtn = new JButton(removeBtnText, removeImage);
        removeBtn.setToolTipText(removeBtnToolTip);

        reportBtn = new JButton(reportBtnText, reportImage);
        reportBtn.setToolTipText(reportBtnToolTip);

        toolBar.add(openBtn);
        toolBar.add(Box.createHorizontalStrut(10));
        toolBar.add(addBtn);
        toolBar.add(Box.createHorizontalStrut(3));
        toolBar.add(editBtn);
        toolBar.add(Box.createHorizontalStrut(3));
        toolBar.add(removeBtn);
        toolBar.add(Box.createHorizontalGlue());
        toolBar.add(reportBtn);

        frm.add(toolBar, BorderLayout.NORTH);
    }

    private void createMainTable() {
        mainTable = new DataTable();
        frm.add(mainTable.getVisualComponent(), BorderLayout.CENTER);
    }

    private void setMainTableStartContent(){
        actionHandler = new ActionHandler();
        actionHandler.setMainTable(mainTable);
        actionHandler.commandHandler(ActionHandler.OPEN_CATALOG_COMMAND);
    }

    private void createOpenMenu(){
        JPopupMenu openMenu;
        openMenu = new JPopupMenu();
        JMenuItem openCatalogItem = new JMenuItem(openCatalogMenuItemText);
        JMenuItem openOperationsItem = new JMenuItem(openOperationsMenuItemText);
        openMenu.add(openCatalogItem);
        openMenu.add(openOperationsItem);

        openBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                openMenu.show(openBtn, e.getX(),e.getY());
            }
        });

        openCatalogItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionHandler.commandHandler(ActionHandler.OPEN_CATALOG_COMMAND);
            }
        });

        openOperationsItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionHandler.commandHandler(ActionHandler.OPEN_OPERATIONS_COMMAND);
            }
        });
    }

    private void showFrm() {
        frm.setVisible(true);
    }

}

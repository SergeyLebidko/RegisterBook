package registerbook;

import javax.swing.*;
import java.awt.*;

import static registerbook.ResourcesList.*;

public class GUI {

    private JFrame frm;

    private JToolBar toolBar;
    private JButton openBtn;
    private JButton addBtn;
    private JButton editBtn;
    private JButton removeBtn;
    private JButton reportBtn;

    public GUI() {
        createFrm();
        createToolbar();
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

    private void showFrm() {
        frm.setVisible(true);
    }

}

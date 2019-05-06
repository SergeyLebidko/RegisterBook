package registerbook;

import registerbook.data_access_components.DBHandler;

import javax.swing.*;

public class MainClass {

    private static DBHandler dbHandler;

    public static void main(String[] args) {

        try {
            dbHandler=new DBHandler();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "", "", JOptionPane.ERROR_MESSAGE);
            return;
        }

    }

    public static DBHandler getDbHandler() {
        return dbHandler;
    }

}

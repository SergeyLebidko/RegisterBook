package registerbook;

import registerbook.data_access_components.DBHandler;

import javax.swing.*;

public class MainClass {

    private static DBHandler dbHandler;
    private static ActionHandler actionHandler;

    public static void main(String[] args) {
        //Пытаемся получить подключение к базе данных
        try {
            dbHandler = new DBHandler();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Ошибка подключения к базе данных. Приложение будет закрыто.", "", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Создаем объект класса ActionHandler, реализующий логику приложения
        actionHandler = new ActionHandler();

        //Если подключение успешно получено, то создаем интерфейс
        new GUI();
    }

    public static DBHandler getDbHandler() {
        return dbHandler;
    }

    public static ActionHandler getActionHandler() {
        return actionHandler;
    }

}

package registerbook;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.HashMap;

public class ResourcesList {

    //Параметры подключения к базе данных
    public static final String jdbcClassName = "org.sqlite.JDBC";
    public static final String databaseConnectionString = "jdbc:sqlite:database\\database.db";

    //Ресурсы для кнопок
    private static ImageLoader imageLoader = new ImageLoader();
    public static final Image logoImage = imageLoader.getImage("logo");
    public static final ImageIcon openImage = imageLoader.getImageIcon("open");
    public static final ImageIcon addImage = imageLoader.getImageIcon("add");
    public static final ImageIcon removeImage = imageLoader.getImageIcon("remove");
    public static final ImageIcon editImage = imageLoader.getImageIcon("edit");
    public static final ImageIcon reportImage = imageLoader.getImageIcon("report");

    public static final String openBtnText = "";
    public static final String openBtnToolTip = "Открыть";

    public static final String addBtnText = "";
    public static final String addBtnToolTip = "Создать";

    public static final String editBtnText = "";
    public static final String editBtnToolTip = "Изменить";

    public static final String removeBtnText = "";
    public static final String removeBtnToolTip = "Удалить";

    public static final String reportBtnText = "";
    public static final String reportBtnToolTip = "Отчет";

    //Параметры главного окна
    public static final String frmTitle = "RegisterBook";
    public static final int FRM_WIDTH = 1000;
    public static final int FRM_HEIGHT = 800;
    public static final int MIN_FRM_WIDTH = 600;
    public static final int MIN_FRM_HEIGHT = 400;

    //Русские варианты надписей в стандартных диалоговых окнах
    public static final String yesButtonText = "Да";
    public static final String noButtonText = "Нет";
    public static final String cancelButtonText = "Отмена";
    public static final String inputDialogTitle = "";

    //Тексты сообщений об ошибках
    public static final String doNotOpenCatalog = "Не удалось открыть Каталог";
    public static final String doNotOpenOperations = "Не удалось открыть Журнал операций";
    public static final String nameIsNotEmpty = "Имя не может быть пустым";
    public static final String nameContainsDisabledChars = "Имя содержит недопустимые символы";
    public static final String failAddCatalogElement = "Не удалось добавить элемент в Каталог";
    public static final String failAddOperationElement = "Не удалось внести сведения об операции в базу данных";
    public static final String failRemoveCatalogElement = "Не удалось удалить элемент каталога";
    public static final String failRemoveOperationElement = "Не удалось удалить операцию";
    public static final String failUpdateCatalogElement = "Не удалось изменить элемент";
    public static final String failUpdateOperationsElement = "Не удалось изменить операцию";
    public static final String selectCorrectDate = "Выберите дату";
    public static final String inputCorrectCount = "Введите корректное число";
    public static final String noSelectedCatalogElements = "Выберите элемент из каталога";
    public static final String noSelectedOperationsElement = "Выберите операцию";
    public static final String valueMustBeNotZero = "Количество не может быть нулевым";
    public static final String valueIsNotCorrect = "Введено некорректное количество";
    public static final String operationIsNotBeRemove = "Операция не может быть удалена";
    public static final String selectOneElement = "Выберите один элемент";

    //Параметры табличного компонента
    public static final int rowHeight = 40;
    public static final Color gridColor = Color.LIGHT_GRAY;
    public static final Color headerColor = new Color(230, 230, 230);
    public static final Color evenCellsColor = new Color(240, 240, 240);
    public static final Color notEvenCellsColor = new Color(255, 255, 255);

    //Шрифт для таблиц
    public static final Font mainFont = new Font("Arial", Font.PLAIN, 16);

    //Подписи для пунктов всплывающих меню
    public static final String openCatalogMenuItemText = "Открыть каталог";
    public static final String openOperationsMenuItemText = "Открыть журнал операций";
    public static final String remainsReportMenuItemText = "Отчет по остаткам";
    public static final String turnoverReportMenuItemText = "Отчет по оборотам";

    private static class ImageLoader {

        private static final String[] imageNamesList = {
                "logo",
                "open",
                "add",
                "remove",
                "edit",
                "report"
        };

        private HashMap<String, Image> imageMap = new HashMap<>();

        public ImageLoader() {
            ClassLoader classLoader = getClass().getClassLoader();
            Image image;
            for (String name : imageNamesList) {
                try {
                    image = ImageIO.read(classLoader.getResourceAsStream("images/" + name + ".png"));
                } catch (IOException e) {
                    image = null;
                }
                imageMap.put(name, image);
            }
        }

        public Image getImage(String name) {
            return imageMap.get(name);
        }

        public ImageIcon getImageIcon(String name) {
            Image image;
            image = imageMap.get(name);
            if (image == null) return null;
            return new ImageIcon(image);
        }

    }

}

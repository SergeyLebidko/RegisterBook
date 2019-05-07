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

    //Параметры табличного компонента
    public static final int rowHeight = 20;
    public static final Color gridColor = Color.LIGHT_GRAY;

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

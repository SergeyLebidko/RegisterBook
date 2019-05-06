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

    //Графические ресурсы
    private static ImageLoader imageLoader = new ImageLoader();
    public static final Image logoImage = imageLoader.getImage("logo");
    public static final ImageIcon addImage = imageLoader.getImageIcon("add");
    public static final ImageIcon removeImage = imageLoader.getImageIcon("remove");

    //Размеры главного окна
    public static final int FRM_WIDTH = 1000;
    public static final int FRM_HEIGHT = 800;
    public static final int MIN_FRM_WIDTH = 600;
    public static final int MIN_FRM_HEIGHT = 400;

    private static class ImageLoader {

        private static final String[] imageNamesList = {
                "logo",
                "add",
                "remove"
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

import StartMenu.StartView;
public class Main {
    public static void main(String[] args) {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            // 或者在这里处理异常，例如给出错误提示，或者采取其他适当的操作
        }

        System.setProperty("sun.java2d.noddraw", "true");
        new StartView();
    }
}
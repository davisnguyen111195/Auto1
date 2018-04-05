package gmail.auto.send;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class main extends Application {
    private static Stage pStage;

    public static void main(String[] args) {
        launch(args);
    }


    public static String username = System.getProperty("user.name");
    @Override
    public void start(Stage primaryStage) {
        try {
            System.out.println("user-data-dir=/home/" + main.username +"/.config/google-chrome/Profile ");

            Parent root = FXMLLoader.load(getClass()
                    .getResource("/gmail/auto/send/GUI.fxml"));
            primaryStage.setTitle("Gmail Auto Send");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Stage getPrimaryStage() {
        return pStage;
    }

    public static void setpStage(Stage pStage) {
        main.pStage = pStage;
    }
}

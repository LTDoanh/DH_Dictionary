package dictionary;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.scene.Parent;

import java.io.IOException;
import javafx.stage.WindowEvent;

public class Main extends Application {

  @Override
  public void start(Stage stage) throws IOException {
    try {
      Parent root = FXMLLoader.load(this.getClass().getResource("fxml/dictionary.fxml"));
      Scene translateWord = new Scene(root);
      stage.setScene(translateWord);
      stage.getIcons().add(new Image(getClass().getResource("media/image/logo.png").toExternalForm()));
      stage.setTitle("DH Dictionary");
      stage.show();

      // Thiết lập sự kiện khi nhấn nút thoát
      stage.setOnCloseRequest((WindowEvent event) -> {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit");
        ButtonType buttonTypeExit = new ButtonType("Exit", ButtonType.YES.getButtonData());
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonType.CANCEL.getButtonData());
        alert.getButtonTypes().setAll(buttonTypeExit, buttonTypeCancel);

        // Hiển thị thông báo thoát
        alert.showAndWait().ifPresent(result -> {
          if (result == buttonTypeExit){
            // Thoát chương trình
            System.exit(0);
          }
        });
        event.consume();
      });

    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  public static void main(String[] args) {
    launch();
  }
}
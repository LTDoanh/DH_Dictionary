package dictionary;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class TranslateController extends AppController {

  private String languageFrom = "";
  private String languageTo = "vi";
  private String nameFrom;
  private String speakFrom;
  private String nameTo;
  private String speakTo;

  private MediaPlayer mediaPlayer;

  @FXML
  private TextArea area1 = new TextArea();
  @FXML
  private TextArea area2 = new TextArea();

  @FXML
  private TextField text1;
  @FXML
  private TextField text2;

  @FXML
  private Button speakButton;

  @FXML
  private Button translateButton;

  @FXML
  private Button langFromFirst;
  @FXML
  private Button langFromSecond;
  @FXML
  private Button langFromThird;

  @FXML
  private Button langToFirst;
  @FXML
  private Button langToSecond;

  private void resetStyleLangFrom() {
    langFromFirst.getStyleClass().removeAll("active");
    langFromSecond.getStyleClass().removeAll("active");
    langFromThird.getStyleClass().removeAll("active");
  }

  public void detect() {
    resetStyleLangFrom();
    langFromFirst.getStyleClass().add("active");
    languageFrom = "";
    text1.setText("Phát hiện n.ngữ");
    nameFrom = "Linda";
    speakFrom = "en-gb";
  }

  @FXML
  private void eng() {
    resetStyleLangFrom();
    langFromSecond.getStyleClass().add("active");
    languageFrom = "en";
    text1.setText("Tiếng Anh");
    nameFrom = "Linda";
    speakFrom = "en-gb";
  }

  @FXML
  private void vie1() {
    resetStyleLangFrom();
    langFromThird.getStyleClass().add("active");
    text1.setText("Tiếng Việt");
    languageFrom = "vi";
    nameFrom = "Chi";
    speakFrom = "vi-vn";
  }

  private void resetStyleLangTo() {
    langToFirst.getStyleClass().removeAll("active");
    langToSecond.getStyleClass().removeAll("active");
  }

  @FXML
  private void vie2() throws IOException {
    resetStyleLangTo();
    langToFirst.getStyleClass().add("active");
    text2.setText("Tiếng Việt");
    languageTo = "vi";
    nameTo = "Chi";
    speakTo = "vi-vn";
    if (!Objects.equals(area1.getText(), "")) {
      TranslateAPI t = new TranslateAPI();
      area2.setText(t.useAPI(area1.getText()));
    }
  }

  @FXML
  private void eng2() throws IOException {
    resetStyleLangTo();
    langToSecond.getStyleClass().add("active");
    text2.setText("Tiếng Anh");
    languageTo = "en";
    nameTo = "Linda";
    speakTo = "en-gb";
    if (!Objects.equals(area1.getText(), "")) {
      TranslateAPI t = new TranslateAPI();
      area2.setText(t.useAPI(area1.getText()));
    }
  }

  @FXML
  private void handleTranslate() throws IOException {
    if (!Objects.equals(area1.getText(), "")) {
      if (area1.getText().length() <= 2000) {
        translateButton.setDisable(false);
        TranslateAPI t = new TranslateAPI();
        area2.setText(t.useAPI(area1.getText()));
      } else {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Imformation");
        alert.setHeaderText("Đoạn văn cần dịch quá dài");
        alert.setContentText("Hãy rút ngắn lại");
        area1.setText("");
        alert.showAndWait();
      }

    }
  }

  @FXML
  private void handlePlay() {
    VoiceAPI v = new VoiceAPI();
    v.useAPI(area1.getText());
    Media media = new Media(new File(VoiceAPI.audioFilePath).toURI().toString());
    mediaPlayer = new MediaPlayer(media);
    mediaPlayer.play();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    langFromFirst.getStyleClass().add("active");
    langToFirst.getStyleClass().add("active");

    text1.setText("Phát hiện n.ngữ");
    area1.setText("");
    nameFrom = "Linda";
    speakFrom = "en-gb";
    languageFrom = "";

    text2.setText("Tiếng Việt");
    nameTo = "Chi";
    speakTo = "vi-vn";
    languageTo = "vi";

    area1.textProperty().addListener((observableValue, oldValue, newValue) -> {
      if (!newValue.isEmpty()) {
        translateButton.setDisable(false);
        area2.clear();
      } else {
        translateButton.setDisable(true);
      }
    });
  }
}
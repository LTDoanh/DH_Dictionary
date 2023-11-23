package dictionary;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class WordSearchController extends GameController {

  @FXML
  private Button backButton;
  @FXML
  private Label showPoint;
  @FXML
  private Label showWord;
  @FXML
  private Button restart;
  @FXML
  private Button next;
  @FXML
  private Button prev;
  @FXML
  private TextArea showHint;
  @FXML
  private GridPane gridPane = new GridPane();
  WordSearchManager gameManager = new WordSearchManager();
  List<Button> list = new ArrayList<>();
  Button firstButton;
  int fx = -1;
  int fy = -1;
  Button secondButton;
  int sx = -1;
  int sy = -1;
  int point = 0;
  int currHint = 0;

  public void initialize(URL url, ResourceBundle resourceBundle) {
    gameManager.initMap();
    System.out.println(gameManager.getWords());
    gridPane.setHgap(0);
    gridPane.setVgap(0);
    showHint.setText(gameManager.getHints().get(currHint));
    showPoint.setText("Point: " + point);
    showWord.setText("Word: " + (currHint + 1) + "/" + gameManager.getWords().size() + "");
    System.out.println("Word: " + (currHint + 1) + "/" + gameManager.getWords().size() + "");
    for (int row = 0; row < 10; row++) {
      for (int col = 0; col < 10; col++) {
        Button button = new Button(String.valueOf(gameManager.getCharPos(row, col)));
        button.setMinSize(40, 40);
        button.setMaxSize(40, 40);
        button.setStyle("-fx-background-color: transparent");

        int curX = row;
        int curY = col;
        button.setOnMousePressed(e -> {
          if (firstButton == null) {
            firstButton = button;
            button.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
            setPos1(button);
          } else {
            secondButton = button;
            button.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
            setPos2(button);
            System.out.println(fx + " " + fy + " " + sx + " " + sy);
            if (sx == fx && sy - fy >= 1) {
              for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                  Button b = (Button) node;
                  int r = GridPane.getRowIndex(b);
                  int c = GridPane.getColumnIndex(b);
                  if (r == fx && c >= fy && c <= sy) {
                    b.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
                    list.add(b);
                  }
                }
              }
            } else if (sx == fx && fy - sy >= 1) {
              for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                  Button b = (Button) node;
                  int r = GridPane.getRowIndex(b);
                  int c = GridPane.getColumnIndex(b);
                  if (r == fx && c <= fy && c >= sy) {
                    b.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
                    list.add(b);
                  }
                }
              }
            } else if (sy == fy && sx - fx >= 1) {
              for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                  Button b = (Button) node;
                  int r = GridPane.getRowIndex(b);
                  int c = GridPane.getColumnIndex(b);
                  if (c == fy && r >= fx && r <= sx) {
                    b.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
                    list.add(b);
                  }
                }
              }
            } else if (sy == fy && fx - sx >= 1) {
              for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                  Button b = (Button) node;
                  int r = GridPane.getRowIndex(b);
                  int c = GridPane.getColumnIndex(b);
                  if (c == fy && r >= sx && r <= fx) {
                    b.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
                    list.add(b);
                  }
                }
              }
            } else if (sx >= fx && sy >= fy && sx - sy == fx - fy) {
              for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                  Button b = (Button) node;
                  int r = GridPane.getRowIndex(b);
                  int c = GridPane.getColumnIndex(b);
                  if (r >= fx && r <= sx && c >= fy && c <= sy && r - fx == c - fy) {
                    b.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
                    list.add(b);
                  }
                }
              }
            } else if (sx <= fx && sy <= fy && sx - sy == fx - fy) {
              for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                  Button b = (Button) node;
                  int r = GridPane.getRowIndex(b);
                  int c = GridPane.getColumnIndex(b);
                  if (r <= fx && r >= sx && c <= fy && c >= sy && r - fx == c - fy) {
                    b.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
                    list.add(b);
                  }
                }
              }
            } else if (sx >= fx && sy <= fy && sx - fx == -sy + fy) {
              for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                  Button b = (Button) node;
                  int r = GridPane.getRowIndex(b);
                  int c = GridPane.getColumnIndex(b);
                  if (r >= fx && r <= sx && c <= fy && c >= sy && r - fx == -c + fy) {
                    b.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
                    list.add(b);
                  }
                }
              }
            } else if (sx <= fx && sy >= fy && sx - fx == -sy + fy) {
              for (Node node : gridPane.getChildren()) {
                if (node instanceof Button) {
                  Button b = (Button) node;
                  int r = GridPane.getRowIndex(b);
                  int c = GridPane.getColumnIndex(b);
                  if (r <= fx && r >= sx && c >= fy && c <= sy && r - fx == -c + fy) {
                    b.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
                    list.add(b);
                  }
                }
              }
            } else {
              list.add(firstButton);
              list.add(secondButton);
            }
            reset();
          }
        });
        gridPane.add(button, col, row);
      }
    }
  }

  @Override
  @FXML
  public void handleBackScene() throws Exception {
    Parent root = FXMLLoader.load(this.getClass().getResource("fxml/game.fxml"));
    Stage window = (Stage) backButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }

  public void setPos1(Button button) {
    for (Node node : gridPane.getChildren()) {
      if (node instanceof Button) {
        Button b = (Button) node;
        if (b == button) {
          fx = GridPane.getRowIndex(button);
          fy = GridPane.getColumnIndex(button);
        }
      }
    }
  }

  public void setPos2(Button button) {
    for (Node node : gridPane.getChildren()) {
      if (node instanceof Button) {
        Button b = (Button) node;
        if (b == button) {
          sx = GridPane.getRowIndex(button);
          sy = GridPane.getColumnIndex(button);
        }
      }
    }
  }

  public void reset() {
    String str1 = "";
    String str2 = "";
    for (Button b : list) {
      str1 = str1 + b.getText();
      str2 = b.getText() + str2;
    }
    if (gameManager.getWords().contains(str1) || gameManager.getWords().contains(str2)) {
      gameManager.getWords().remove(str1);
      gameManager.getWords().remove(str2);
      point += 100;
      showPoint.setText("Point: " + point);
    }
    for (Button b : list) {
      Timeline timeline = new Timeline();
      KeyFrame keyFrame1 = new KeyFrame(Duration.seconds(0), event -> {
        b.setStyle("-fx-background-color: rgba(184, 147, 228, 0.5)");
      });
      KeyFrame keyFrame2 = new KeyFrame(Duration.seconds(0.5), event -> {
        b.setStyle("-fx-background-color: transparent");
      });
      timeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
      timeline.play();
    }
    firstButton = null;
    secondButton = null;
    System.out.println(list.size());
    list.clear();
    System.out.println(gameManager.getWords());
    if (gameManager.getWords().size() == 0) {
      Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
      alert.setTitle("You win");
      alert.setContentText("Choose your option");
      ButtonType buttonTypeYes = new ButtonType("Yes", ButtonBar.ButtonData.YES);
      ButtonType buttonTypeNo = new ButtonType("No", ButtonBar.ButtonData.NO);
      ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
      List<ButtonType> buttonTypes = alert.getButtonTypes();
      buttonTypes.clear();
      buttonTypes.add(buttonTypeYes);
      buttonTypes.add(buttonTypeNo);
      buttonTypes.add(buttonTypeCancel);
      Optional<ButtonType> result = alert.showAndWait();
    }
  }

  public void goNext() {
    currHint++;
    if (currHint >= gameManager.getWords().size()) {
      currHint = 0;
    }
    showHint.setText(gameManager.getHints().get(currHint));
    showWord.setText("Word " + (currHint + 1) + "/" + gameManager.getWords().size());
    System.out.println("Word: " + (currHint + 1) + "/" + gameManager.getWords().size() + "");
  }

  public void goPrev() {
    currHint--;
    if (currHint <= 0) {
      currHint = gameManager.getWords().size() - 1;
    }
    showHint.setText(gameManager.getHints().get(currHint));
    showWord.setText("Word " + (currHint + 1) + "/" + gameManager.getWords().size());
    System.out.println("Word: " + (currHint + 1) + "/" + gameManager.getWords().size() + "");
  }

  @Override
  public void restart() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/wordsearch.fxml"));
    Stage window = (Stage) restart.getScene().getWindow();
    window.setScene(new Scene(root));
  }

}
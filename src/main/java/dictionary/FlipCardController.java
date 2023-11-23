package dictionary;

import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.*;

public class FlipCardController extends GameController {

  @FXML
  private Button backButton;
  @FXML
  private GridPane gridPane;
  @FXML
  private Label showTries;
  @FXML
  private Button restartButton;

  @FXML
  private Label showPoint;
  private Button[][] buttons = new Button[4][4];
  private int[][] indexes = new int[4][4];
  private int[][] buttonId = new int[4][4];
  List<Word> randomWords;
  private Button firstButton;
  private Button secondButton;
  private int firstButtonId;
  private int secondButtonId;
  private int matched = 0;
  private int tries = 25;
  private int point = 0;
  private boolean isShowing;

  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    for (int i = 0; i < 4; i++) {
      for (int j = 0; j < 4; j++) {
        buttons[i][j] = new Button();
        buttons[i][j].setPrefSize(123, 83);
        buttons[i][j].setStyle("-fx-background-color: lightblue");
        buttons[i][j].setWrapText(true);
        buttons[i][j].textAlignmentProperty();
        buttons[i][j].setOnAction(e -> handleButton(e));
        gridPane.add(buttons[i][j], j, i);
      }
    }
    indexes[0][0] = 0;
    indexes[0][1] = 1;
    indexes[0][2] = 2;
    indexes[0][3] = 3;
    indexes[1][0] = 4;
    indexes[1][1] = 5;
    indexes[1][2] = 6;
    indexes[1][3] = 7;
    indexes[2][0] = 8;
    indexes[2][1] = 9;
    indexes[2][2] = 10;
    indexes[2][3] = 11;
    indexes[3][0] = 12;
    indexes[3][1] = 13;
    indexes[3][2] = 14;
    indexes[3][3] = 15;
    shuffle2DArray(indexes);
    randomWords = DatabaseConnection.selectRandom();
    showTries.setText("You have " + tries + " plays");
    showPoint.setText("Point:  " + point);
  }

  @Override
  @FXML
  public void handleBackScene() throws Exception {
    Parent root = FXMLLoader.load(this.getClass().getResource("fxml/game.fxml"));
    Stage window = (Stage) backButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }

  private void handleButton(ActionEvent e) {
    if (firstButton != null && secondButton != null) {
      return;
    }
    Button button = (Button) e.getSource();
    for (int i = 0; i < buttons.length; i++) {
      for (int j = 0; j < buttons[i].length; j++) {
        if (buttons[i][j] == button && indexes[i][j] >= 0 && matched < 8
                && buttons[i][j] != firstButton) {
          boolean pass = true;
          if (indexes[i][j] % 2 == 1) {
            isShowing = true;
            String tmp = randomWords.get(indexes[i][j] / 2).getDefinition();
            String[] tmps = tmp.split("\n");
            String text = "";
            for (String result : tmps) {
              if (result.charAt(0) == '-') {
                text = result.substring(2);
                break;
              }
            }
            RotateTransition rotator = createRotator(button);
            PauseTransition ptChangeCardFace = changeCardFace(button, text);
            ParallelTransition parallelTransition = new ParallelTransition(rotator,
                    ptChangeCardFace);
            parallelTransition.play();
            isShowing = false;
          } else {
            isShowing = true;
            String text = randomWords.get(indexes[i][j] / 2).getTarget();
            RotateTransition rotator = createRotator(button);
            PauseTransition ptChangeCardFace = changeCardFace(button, text);
            ParallelTransition parallelTransition = new ParallelTransition(rotator,
                    ptChangeCardFace);
            parallelTransition.play();
            isShowing = false;
          }

          if (firstButton == null) {
            firstButton = button;
            firstButtonId = randomWords.get(indexes[i][j] / 2).getId();
          } else {
            secondButton = button;
            secondButtonId = randomWords.get(indexes[i][j] / 2).getId();
            if (firstButtonId == secondButtonId) {
              indexes[i][j] = -1;
              for (int m = 0; m < 4; m++) {
                for (int n = 0; n < 4; n++) {
                  if (buttons[m][n] == firstButton) {
                    indexes[m][n] = -1;
                  }
                }
              }
              matched++;
              point += 100;
              Random random = new Random();
              Color randomColor = Color.rgb(random.nextInt(256), random.nextInt(256),
                      random.nextInt(256));
              firstButton.setStyle(
                      "-fx-background-color: " + randomColor.toString().replace("0x", "#") + ";");
              secondButton.setStyle(
                      "-fx-background-color: " + randomColor.toString().replace("0x", "#") + ";");
              showPoint.setText("Point:  " + point);
              pass = false;
              firstButton = null;
              secondButton = null;
            } else {
              Thread thread = new Thread(() -> {
                try {
                  Thread.sleep(1000);
                } catch (InterruptedException ex) {
                  ex.printStackTrace();
                }
                javafx.application.Platform.runLater(() -> {
                  firstButton.setStyle("-fx-background-color: lightblue");
                  String text = "";
                  RotateTransition rotator = createRotator(firstButton);
                  PauseTransition ptChangeCardFace = changeCardFace(firstButton, text);
                  ParallelTransition parallelTransition = new ParallelTransition(rotator,
                          ptChangeCardFace);
                  parallelTransition.play();

                  secondButton.setStyle("-fx-background-color: lightblue");
                  rotator = createRotator(secondButton);
                  ptChangeCardFace = changeCardFace(secondButton, text);
                  parallelTransition = new ParallelTransition(rotator, ptChangeCardFace);
                  parallelTransition.play();

                  firstButton = null;
                  secondButton = null;
                });
              });
              thread.start();
            }
            tries--;
            if (tries == 1) {
              showTries.setText("You have " + tries + " play");
            } else {
              showTries.setText("You have " + tries + " plays");
            }
            if (matched == 8) {
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Confirmation Dialog");
              alert.setHeaderText("Look, You Won and got " + point);
              alert.setContentText("You will restart if choose Ok");
              Optional<ButtonType> result = alert.showAndWait();
              if (result.get() == ButtonType.OK) {
                try {
                  restart();
                } catch (Exception ex) {
                  ex.printStackTrace();
                }

              }
            }
            if (tries == 0) {
              Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
              alert.setTitle("Confirmation Dialog");
              alert.setHeaderText("Look, You losed @@");
              alert.setContentText("You will restart if choose Ok");
              Optional<ButtonType> result = alert.showAndWait();
              if (result.get() == ButtonType.OK) {
                try {
                  restart();
                } catch (Exception ex) {
                  ex.printStackTrace();
                }
              }
            }
          }
          if (pass) {
            button.setStyle("-fx-background-color: white");
          }
          break;
        }
      }
    }
  }

  private RotateTransition createRotator(Button button) {
    RotateTransition rotator = new RotateTransition(Duration.millis(200), button);

    // sử dụng trục X và góc quay âm để lật dọc
    rotator.setAxis(Rotate.X_AXIS); // lật dọc
    if (isShowing) {
      rotator.setFromAngle(0);
      rotator.setToAngle(-180);
    } else {
      rotator.setFromAngle(-180);
      rotator.setToAngle(-360);
    }

    rotator.setInterpolator(Interpolator.LINEAR);
    rotator.setCycleCount(1);

    return rotator;
  }

  private PauseTransition changeCardFace(Button button, String text) {
    PauseTransition pause = new PauseTransition(Duration.millis(200));

    if (isShowing) {
      button.setText("");
      pause.setOnFinished(
              e -> {
                button.setText(text);

                button.setStyle("-fx-scale-y: -1; -fx-background-color: white; -fx-font-size: 15px;");
              });
    } else {
      button.setText("");
      button.setStyle("-fx-background-color: lightblue");
    }

    return pause;
  }

  public static void shuffle2DArray(int[][] array) {
    Random rand = new Random();

    for (int i = array.length - 1; i > 0; i--) {
      for (int j = array[i].length - 1; j > 0; j--) {
        int m = rand.nextInt(i + 1);
        int n = rand.nextInt(j + 1);

        int temp = array[i][j];
        array[i][j] = array[m][n];
        array[m][n] = temp;
      }
    }
  }

  public List<Word> generateWord() {
    List<Word> result = new ArrayList<>();
    List<Map.Entry<String, String>> entries = new ArrayList<>(DatabaseConnection.treeMap.entrySet());
    Collections.shuffle(entries);
    List<Map.Entry<String, String>> randomEntries = entries.subList(0, 8);
    for (Map.Entry<String, String> entry : randomEntries) {
      result.add(new Word(result.size() + 1, entry.getKey(), entry.getValue()));
    }
    return result;
  }

  @Override
  public void restart() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/flipcard.fxml"));
    Stage window = (Stage) restartButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }
}
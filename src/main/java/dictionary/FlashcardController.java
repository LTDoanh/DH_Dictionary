package dictionary;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.RotateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.transform.Rotate;
import javafx.stage.Stage;
import javafx.util.Duration;

public class FlashcardController {

  @FXML
  private Button changeDicSceneButton;
  @FXML
  private Button changeTransSceneButton;
  @FXML
  private Button changePracSceneButton;

  @FXML
  private Button deleteButton;

  @FXML
  private TextArea card;

  @FXML
  private Button nextButton;
  @FXML
  private Button prevButton;
  @FXML
  private Label indexLabel;

  private List<Word> words;
  private boolean isFrontShowing;
  private boolean isHorizontal;
  private String fileName;
  private int index;

  public FlashcardController() {
    words = new ArrayList<>();
    isFrontShowing = true;
    isHorizontal = false;
    index = 0;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }

  @FXML
  public void init() {
    readDataFromFile(fileName);
    if (!words.isEmpty()) {
      Word word = words.get(index);
      card.setText(word.getTarget());
      updateIndexLabel();
    } else {
      card.setText("");
    }
  }

  @FXML
  public void handleAddButton(ActionEvent event) {
    TextInputDialog dialog = new TextInputDialog();
    dialog.setTitle("Thêm một từ");
    dialog.setHeaderText("Nhập một từ tiếng Anh và nghĩa của nó");
    dialog.setContentText("Từ:Nghĩa");

    dialog.showAndWait().ifPresent(input -> {
      String[] parts = input.split(":");
      if (parts.length == 2) {
        String target = parts[0];
        target.trim();
        String definition = parts[1];
        definition.trim();
        if (!containsTarget(target)) {
          Word word = new Word(target, definition);
          words.add(word);
          writeDataToFile(fileName, target + ":" + definition + "\n", true); // append to file
          updateIndexLabel();
        }
      }
    });
  }

  @FXML
  public void handleDeleteButton(ActionEvent event) {
    if (!words.isEmpty()) {
      deleteButton.setDisable(false);
      writeDataToFile(fileName,
          words.get(index).getTarget() + ":" + words.get(index).getDefinition() + "\n",
          false); // remove from file
      words.remove(index);
      if (words.isEmpty()) {
        card.setText("");
        deleteButton.setDisable(true);
      } else {
        if (index >= words.size()) { // check if index is out of bounds
          index = 0; // reset index to zero
        }
        card.setText(words.get(index).getTarget());
      }
      updateIndexLabel();
    }
  }

  @FXML
  public void handleEditButton(ActionEvent event) {
    if (!words.isEmpty()) {
      String target = words.get(index).getTarget();
      String definition = words.get(index).getDefinition();
      TextInputDialog inputDialog = new TextInputDialog(definition);
      inputDialog.setTitle("Sửa một từ");
      inputDialog.setHeaderText("Nhập nghĩa mới cho từ " + target);
      inputDialog.setContentText("Nghĩa:");
      inputDialog.showAndWait().ifPresent(newDefinition -> {
        if (!newDefinition.isEmpty()) {
          words.get(index).setDefinition(newDefinition);
          writeDataToFile(fileName, target + ":" + definition + "\n",
              false); // remove old meaning from file
          writeDataToFile(fileName, target + ":" + newDefinition + "\n",
              true); // append new meaning to file
          card.setText(newDefinition);
          isFrontShowing = false;
        }
      });
    }
  }

  @FXML
  public void handleNextButton(ActionEvent event) {
    if (!words.isEmpty()) {
      index++; // increase index by one
      if (index >= words.size()) { // check if index is out of bounds
        index = 0; // reset index to zero
      }
      Word word = words.get(index);
      card.setText(word.getTarget());
      isFrontShowing = true;
      updateIndexLabel();
    }
  }

  @FXML
  public void handlePrevButton(ActionEvent event) {
    if (!words.isEmpty() && index > 0) {
      index--; // decrease index by one
      Word word = words.get(index);
      card.setText(word.getTarget());
      isFrontShowing = true;
      updateIndexLabel();
    }
  }

  @FXML
  public void handleCardClick() {
    RotateTransition rotator = createRotator(card);
    PauseTransition ptChangeCardFace = changeCardFace(card);

    ParallelTransition parallelTransition = new ParallelTransition(rotator, ptChangeCardFace);
    parallelTransition.play();

    isFrontShowing = !isFrontShowing;
  }

  private RotateTransition createRotator(TextArea card) {
    RotateTransition rotator = new RotateTransition(Duration.millis(200), card);

    // sử dụng trục X và góc quay âm để lật dọc
    rotator.setAxis(Rotate.X_AXIS); // lật dọc
    if (isFrontShowing) {
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

  private PauseTransition changeCardFace(TextArea card) {
    PauseTransition pause = new PauseTransition(Duration.millis(200));

    if (isFrontShowing) {
      card.clear();
      pause.setOnFinished(
          e -> {
            if (!words.isEmpty()) {
              card.setText(words.get(index).getDefinition());
            } else {
              card.setText("");
            }
            card.setStyle("-fx-scale-y: -1");
          });
    } else {
      card.clear();
      pause.setOnFinished(
          e -> {
            if (!words.isEmpty()) {
              card.setText(words.get(index).getTarget());
            } else {
              card.setText("");
            }
            card.setStyle("-fx-scale-y: 1");
          });
    }

    return pause;
  }

  private void readDataFromFile(String fileName) {
    try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] parts = line.split(":");
        line.trim();
        if (parts.length == 2) {
          Word word = new Word(parts[0], parts[1]);
          words.add(word);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void writeDataToFile(String fileName, String data, boolean append) {
    try (FileWriter fw = new FileWriter(fileName, append)) {
      fw.write(data);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private void updateIndexLabel() {
    if (words.isEmpty()) {
      indexLabel.setText("0 / 0");
    } else {
      indexLabel.setText((index + 1) + " / " + words.size());
    }
    indexLabel.setAlignment(Pos.CENTER);
  }

  private boolean containsTarget(String target) {
    for (Word word : words) {
      if (word.getTarget().equals(target)) {
        return true;
      }
    }
    return false;
  }

  public void handleBackButton() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/practice.fxml"));
    Stage window = (Stage) changePracSceneButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }

  public void handleChangeDicScene() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/dictionary.fxml"));
    Stage window = (Stage) changeDicSceneButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }

  public void handleChangeTransScene() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/translate.fxml"));
    Stage window = (Stage) changeTransSceneButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }

  public void handleChangePracScene() throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("fxml/practice.fxml"));
    Stage window = (Stage) changePracSceneButton.getScene().getWindow();
    window.setScene(new Scene(root));
  }
}

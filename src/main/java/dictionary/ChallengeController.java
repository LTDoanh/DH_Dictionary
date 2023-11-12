package dictionary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class ChallengeController implements Initializable {

    @FXML
    private Button changeWordSceneButton;
    @FXML
    private Button changeParaSceneButton;
    @FXML
    private GridPane gridPane;
    private Button[][] buttons = new Button[4][4];
    private int[][] indexes = new int[4][4];
    private int[][] buttonId = new int[4][4];
    List<Word> randomWords;

    private Button firstButton;

    private Button secondButton;

    private int firstButtonId;
    private int secondButtonId;
    private int matched = 0;

    private DatabaseConnection connect;
    public ChallengeController() {
        connect = new DatabaseConnection();
        connect.getConnection();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttons[i][j] = new Button();
                buttons[i][j].setPrefSize(123, 83);
                buttons[i][j].setStyle("-fx-background-color: lightblue");
                buttons[i][j].setWrapText(true);
                buttons[i][j].textAlignmentProperty( );
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
        randomWords = connect.selectRandom();
    }

    private void handleButton(javafx.event.ActionEvent e) {
        if (firstButton != null && secondButton != null) {
            return;
        }
        Button button = (Button) e.getSource();
        for (int i = 0; i < buttons.length; i++) {
            for (int j = 0; j < buttons[i].length; j++) {
                if (buttons[i][j] == button && indexes[i][j] >= 0 && matched < 8 && buttons[i][j] != firstButton) {
                    boolean pass = true;
                    if (indexes[i][j] % 2 == 1) {
                        String tmp = randomWords.get(indexes[i][j] / 2).getDefinition();
                        String[] tmps = tmp.split("\n");
                        for (String result : tmps) {
                            if (result.charAt(0) == '-') {
                                button.setText(result.substring(2));
                                break;
                            }
                        }
                    }
                    else {
                        button.setText(randomWords.get(indexes[i][j] / 2).getTarget());
                    }
                    if (firstButton == null) {
                        firstButton = button;
                        firstButtonId = randomWords.get(indexes[i][j] / 2).getId();
                    } else {
                        secondButton = button;
                        secondButtonId = randomWords.get(indexes[i][j] / 2).getId();
                        if (firstButtonId == secondButtonId) {
                            indexes[i][j] = -1;
                            for (int m = 0; m < 4; m ++) {
                                for (int n = 0; n < 4; n ++) {
                                    if (buttons[m][n] == firstButton) {
                                        indexes[m][n] = -1;
                                    }
                                }
                            }
                            matched++;
                            //firstButton.setStyle("-fx-background-color: orange");
                            //secondButton.setStyle("-fx-background-color: orange");
                            Random random = new Random();
                            Color randomColor = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                            firstButton.setStyle("-fx-background-color: " + randomColor.toString().replace("0x", "#") + ";");
                            secondButton.setStyle("-fx-background-color: " + randomColor.toString().replace("0x", "#") + ";");
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
                                    firstButton.setText("");
                                    firstButton.setStyle("-fx-background-color: lightblue");
                                    secondButton.setText("");
                                    secondButton.setStyle("-fx-background-color: lightblue");
                                    firstButton = null;
                                    secondButton = null;
                                });
                            });
                            thread.start();
                        }
                    }
                    if (pass) button.setStyle("-fx-background-color: white");
                    break;
                }
            }
        }
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
    public void handleChangeWordScene() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/dictionary.fxml"));
        Stage window = (Stage) changeWordSceneButton.getScene().getWindow();
        window.setScene(new Scene(root));
    }

    public void handleChangeParaScene() throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("fxml/translate.fxml"));
        Stage window = (Stage) changeParaSceneButton.getScene().getWindow();
        window.setScene(new Scene(root));
    }

}
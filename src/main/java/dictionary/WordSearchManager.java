package dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static dictionary.DatabaseConnection.treeMap;

public class WordSearchManager {

  private final ArrayList<String> words = new ArrayList<>(); // Hold the list of words that the user still needs to find
  private final ArrayList<String> definitions = new ArrayList<>();
  private final ArrayList<String> hints = new ArrayList<>();
  private final ArrayList<Character> wordSelect = new ArrayList<>(); // Holds a list of selected characters
  private char[][] map; // Becomes the matrix, and is initialized with a size from WordSearch.java
  private int matrixSize = 10; // Holds the size of the board
  private String input = ""; // Stores the word that the user selects
  private int oldRow = -1; // Set to the row of the first letter of wordSelect
  private int oldCol = -1; // Set to the column of the first letter of wordSelect
  private int direction = -1;

  //Start the game  with the size depending on difficulty selected.

  public void initMap() {
    int size = 10;
    int word = 4;
    matrixSize = size;
    generateWords(word);
    startMatrix(matrixSize);
    generateMatrix();
    fillBlanks();
  }


  //Initializes the matrix
  public void startMatrix(int size) {
    map = new char[size][size];
  }


  /**
   * Places the words in wordList in the board. Runs checks so that all positions
   * are valid and not overwriting another word
   */
  public void generateMatrix() {
    Random rand = new Random();
    int modifier;
    int orientation, randCol, randRow;
    boolean added;
    for (String word : words) {
      do {

        added = false;
        orientation = rand.nextInt(8);
        randRow = rand.nextInt(matrixSize);
        randCol = rand.nextInt(matrixSize);
        if (preventOutOfBound(orientation, randRow, randCol, word.length())) {
          if (preventOverlap(map, orientation, randRow, randCol, word.length())) {
            for (int j = 0; j < word.length(); j++) {
              modifier = j; // Increments the position that is currently being written to
              switch (orientation) {
                case 0: // If orientation is verical up
                  map[randRow - modifier][randCol] = word.charAt(modifier);
                  break;
                case 1: // If orientation is cross right up ?
                  map[randRow - modifier][randCol + modifier] = word.charAt(modifier);
                  break;
                case 2: // If orientation is horizontal right
                  map[randRow][randCol + modifier] = word.charAt(modifier);
                  break;
                case 3: // If orientation is cross right down ?
                  map[randRow + modifier][randCol + modifier] = word.charAt(modifier);
                  break;
                case 4: // If orientation is verical down
                  map[randRow + modifier][randCol] = word.charAt(modifier);
                  break;
                case 5: // If orientation is cross left down ?
                  map[randRow + modifier][randCol - modifier] = word.charAt(modifier);
                  break;
                case 6: // If orientation is horizontal left
                  map[randRow][randCol - modifier] = word.charAt(modifier);
                  break;
                case 7: // If orientation is cross left up ?
                  map[randRow - modifier][randCol - modifier] = word.charAt(modifier);
                  break;
              }
              added = true;
            }
          }
        }
      } while (!added); // Loop until word is added
    }
  }

  public char getCharPos(int row, int col) {
    return map[row][col];
  }

  public void generateWords(int count) {
    words.clear(); // Clears the word list
    definitions.clear();
    int wordsAdded = 0; // Tracks how many words have been added already
    while (wordsAdded < count) {
      Random random = new Random();
      List<String> keys = new ArrayList<>(treeMap.keySet());
      String randomKey = keys.get(random.nextInt(keys.size()));
      String randomValue = treeMap.get(randomKey);
      if (randomKey.length() >= 3 && randomKey.length() <= 7 && !randomKey.contains("-") && randomValue.contains("*")) {
        words.add(randomKey.toUpperCase());
        randomValue = randomValue.substring(randomValue.indexOf('*'));
        definitions.add(randomValue);
        wordsAdded++;
        String hint = "Gợi ý:    " + generateForm(randomKey.toUpperCase()) + "\n"
            + "Nghĩa: " + randomValue;
        hints.add(hint);
      }
    }

  }

  public String generateForm(String str) {
    String result = str.replaceAll(".", "-");
    Random random = new Random();
    int pos1 = random.nextInt(str.length());
    int pos2 = random.nextInt(str.length());
    while (pos2 == pos1) {
      pos2 = random.nextInt(str.length());
    }
    result = result.substring(0, pos1) + str.charAt(pos1) + result.substring(pos1 + 1);
    result = result.substring(0, pos2) + str.charAt(pos2) + result.substring(pos2 + 1);
    return result;
  }
  //prevent the word was positioned out of the matrix array
  public boolean preventOutOfBound(int orientation, int row, int col, int length) {
    boolean inbounds = false;
    switch (orientation) {
      case 0:
        if ((row + 1) - length >= 0) {
          inbounds = true;
        }
        break;
      case 1:
        if ((row + 1) - length >= 0 && (col - 1) + length < matrixSize) {
          inbounds = true;
        }
        break;
      case 2:
        if ((col - 1) + length < matrixSize) {
          inbounds = true;
        }
        break;
      case 3:
        if ((row - 1) + length < matrixSize && (col - 1) + length < matrixSize) {
          inbounds = true;
        }
        break;
      case 4:
        if ((row - 1) + length < matrixSize) {
          inbounds = true;
        }
        break;
      case 5:
        if ((row - 1) + length < matrixSize && (col + 1) - length >= 0) {
          inbounds = true;
        }
        break;
      case 6:
        if ((col + 1) - length >= 0) {
          inbounds = true;
        }
        break;
      case 7:
        if ((col + 1) - length >= 0 && (row + 1) - length >= 0) {
          inbounds = true;
        }
        break;
    }
    return inbounds;
  }

  //prevent word positioning in the already-written words
  public boolean preventOverlap(char[][] matrix, int orientation, int row, int col, int length) {
    boolean checker = true;
    int modifier;
    for (int i = 0; i < length; i++) {
      modifier = i;
      switch (orientation) {
        case 0:
          if (matrix[row - modifier][col] != 0) {
            checker = false;
          }
          break;
        case 1:
          if (matrix[row - modifier][col + modifier] != 0) {
            checker = false;
          }
          break;
        case 2:
          if (matrix[row][col + modifier] != 0) {
            checker = false;
          }
          break;
        case 3:
          if (matrix[row + modifier][col + modifier] != 0) {
            checker = false;
          }
          break;
        case 4:
          if (matrix[row + modifier][col] != 0) {
            checker = false;
          }
          break;
        case 5:
          if (matrix[row + modifier][col - modifier] != 0) {
            checker = false;
          }
          break;
        case 6:
          if (matrix[row][col - modifier] != 0) {
            checker = false;
          }
          break;
        case 7:
          if (matrix[row - modifier][col - modifier] != 0) {
            checker = false;
          }
          break;
      }
    }
    return checker;
  }

  //generate random letters for left cells
  public void fillBlanks() {
    Random randChar = new Random();
    for (int i = 0; i < matrixSize; i++) {
      for (int j = 0; j < matrixSize; j++) {
        if (map[i][j] == 0) {
          // Conatins all possible random letters
          String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
          map[i][j] = ALPHABET.charAt(randChar.nextInt(ALPHABET.length()));
        }
      }
    }
  }

  public String getInput() {
    return input;
  }

  public int getWordListSize() {
    return words.size();
  }

  public void deleteWordListVal(int pos) {
    words.remove(pos);
  }

  //get letter at certain position
  public String getWordListValue(int pos) {
    return words.get(pos);
  }

  public void checkSamePos(int rowSelection, int colSelection) {
    if (input.length() == 0) {
      oldCol = colSelection;
      oldRow = rowSelection;
    } else if (direction == -1) {
      if (!(oldRow == rowSelection && Math.abs(oldCol - colSelection) == 1)
          && !(oldCol == colSelection && Math.abs(oldRow - rowSelection) == 1)) {
        clearWordSelect();
      } else {
        if (rowSelection - oldRow == 1) {
          direction = 0;
        }
        if (rowSelection - oldRow == -1) {
          direction = 2;
        }
        if (colSelection - oldCol == 1) {
          direction = 1;
        }
        if (colSelection - oldCol == -1) {
          direction = 3;
        }
        oldCol = colSelection;
        oldRow = rowSelection;
      }
    } else {
      if (direction == 0 && oldCol == colSelection && rowSelection - oldRow == 1) {
        oldCol = colSelection;
        oldRow = rowSelection;
      } else if (direction == 1 && oldRow == rowSelection && colSelection - oldCol == 1) {
        oldCol = colSelection;
        oldRow = rowSelection;
      } else if (direction == 2 && oldCol == colSelection && rowSelection - oldRow == -1) {
        oldCol = colSelection;
        oldRow = rowSelection;
      } else if (direction == 3 && oldRow == rowSelection && colSelection - oldCol == -1) {
        oldCol = colSelection;
        oldRow = rowSelection;
      } else {
        direction = -1;
        clearWordSelect();
      }
    }
  }

  public void clearWordSelect() {
    wordSelect.clear();
  }

  //Convert wordSelect ArrayList to a string
  public void wordSelectedToString() {
    input = "";
    for (int i = 0; i < getWordSelectSize(); i++) {
      input = input.concat(String.valueOf(getWordSelectValue(i)));
    }
  }

  public void deleteLastLetter() {
    if (wordSelect.size() > 0) {
      wordSelect.remove(wordSelect.size() - 1);
    }
  }

  public void addLetter(char val) {
    wordSelect.add(val);
  }

  public int getWordSelectSize() {
    return wordSelect.size();
  }

  public char getWordSelectValue(int pos) {
    return wordSelect.get(pos);
  }

  public int getMatrixSize() {
    return matrixSize;
  }

  //add letter to the list and check if it matches
  public boolean checkCurrentSelect(int rowSelection, int colSelection) {
    addLetter(getCharPos(rowSelection, colSelection));
    boolean check = checkForFinishedWord(rowSelection, colSelection);
    if (check == true) {
      return true;
    }
    return false;
  }

  public boolean checkForFinishedWord(int rowSelection, int colSelection) {
    checkSamePos(rowSelection, colSelection);
    wordSelectedToString();
    for (int i = 0; i < getWordListSize(); i++) {
      if (input.equals(getWordListValue(i))) {
        deleteWordListVal(i);
        clearWordSelect();
        input = "";
        return true;
      }
    }
    return false;
  }

  public ArrayList<String> getDefinitions() {
    return definitions;
  }

  public ArrayList<String> getHints() {
    return hints;
  }

  public ArrayList<String> getWords() {
    return words;
  }


}

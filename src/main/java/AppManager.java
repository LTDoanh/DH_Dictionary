import java.io.*;
import java.util.Scanner;

public class AppManager {

  public static final String path = "resources/Dictionary.html";
  private static final Scanner scn = new Scanner(System.in);

  /**
   * Phương thức thêm từ và nghĩa vào từ điển
   */
  public static void insertFromCommandline() {
    System.out.print("Input number of words: ");
    int n = scn.nextInt();
    scn.nextLine();
    System.out.println("Insert words: ");
    for (int i = 0; i < n; i++) {
      Dictionary.map.put(scn.nextLine(), scn.nextLine());
    }
    scn.close();
  }

  /**
   * Phương thức tìm kiếm từ trong từ điển và đưa nghĩa của nó ra màn hình
   */
  public static void insertFromFile() throws IOException {
    //Đọc nội dung file
    try (FileReader fileReader = new FileReader(path);
        BufferedReader bufferedReader = new BufferedReader(fileReader)) {
      String line;
      //Đọc từng dòng của file
      while ((line = bufferedReader.readLine()) != null) {
        String[] parts = line.split("<html>");
        String word = parts[0];
        String definition = "<html>" + parts[1];
        Dictionary.map.put(word, definition);
      }
    }
  }

  /**
   * Phương thức tìm kiếm nghĩa của từ
   */
  public static void dictionaryLookup(String wordTarget) {
    if (Dictionary.map.containsKey(wordTarget)) {
      System.out.println(
          "The meaning of " + wordTarget + " is: " + Dictionary.map.get(wordTarget));
    } else {
      System.out.println("Can't find");
    }
  }


  public static void dictionaryAdd() {
    System.out.print("Type word target: ");
    String wordTarget = scn.nextLine();
    if (Dictionary.map.containsKey(wordTarget)) {
      System.out.println(wordTarget + " was existed");
      return;
    }
    System.out.print("Type word explain: ");
    String wordExplain = scn.nextLine();
    wordExplain = "<html>" + wordExplain + "</html>";
    Dictionary.map.put(wordTarget, wordExplain);
  }

  public static void dictionaryDelete() {
    System.out.print("Delete word target: ");
    String wordTarget = scn.nextLine();
    if (Dictionary.map.containsKey(wordTarget)) {
      Dictionary.map.remove(wordTarget);
    } else {
      System.out.println("The word doesn't exist yet!");
    }
  }

  public static void dictionaryModify() {
    System.out.print("Modify word target: ");
    String wordTarget = scn.nextLine();
    System.out.print("With word explain: ");
    String wordExplain = scn.nextLine();
    for (String word : Dictionary.map.keySet()) {
      if (word.equals(wordTarget)) {
        Dictionary.map.put(wordTarget, "<html>" + wordExplain + "</html>");
        return;
      }
    }
    System.out.println("The word doesn't exist yet!");
  }

  public static void dictionaryExportToFile() throws IOException {
    //Đọc nội dung file
    FileWriter fileWriter = new FileWriter(path);
    BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
    for (String wordTarget : Dictionary.map.keySet()) {
      String res = wordTarget + Dictionary.map.get(wordTarget) + "\n";
      bufferedWriter.write(res);
    }
    bufferedWriter.flush();
    bufferedWriter.close();
    fileWriter.close();
  }
}

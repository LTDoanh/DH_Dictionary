import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class SearchWords {
  static int min(int a, int b) {
    return Math.min(a, b);
  }

  static int compare(String keyword, String word_target) {
    int i = 0;
    while (i < min(keyword.length(), word_target.length()) && (int) (keyword.charAt(i)) == (int) (word_target.charAt(i)))
      i++;
    if (i == min(keyword.length(), word_target.length())) {
      if (i == keyword.length()) return 0;
      return -1;
    }
    if ((int) (keyword.charAt(i)) > (int) (word_target.charAt(i))) return -1;
    return 1;
  }

  static int BinarySearchWords(List<String> list, String keyword, int x, int y) {
    int mid = (y - x) / 2 + x;
    if (compare(keyword, list.get(mid)) == 0) return mid;
    if (mid > x && compare(keyword, list.get(mid)) == 1) return BinarySearchWords(list, keyword, x, mid - 1);
    if (mid < y && compare(keyword, list.get(mid)) == -1) return BinarySearchWords(list, keyword, mid + 1, y);
    return -1;
  }

  static boolean condition(List<String> list, String keyword) {
    return (BinarySearchWords(list, keyword, 0, list.size() - 1) != -1);
  }

  static int start(List<String> list, String keyword, int x, int y) {
    int mid = (y - x) / 2 + x;
    if (mid > x && compare(keyword, list.get(mid)) == 0) return start(list, keyword, x, mid - 1);
    if (mid < y && compare(keyword, list.get(mid)) == -1) return start(list, keyword, mid + 1, y);
    if (compare(keyword, list.get(mid)) == 0) return mid;
    return mid + 1;
  }

  static int finish(List<String> list, String keyword, int x, int y) {
    int mid = (y - x) / 2 + x;
    if (mid > x && compare(keyword, list.get(mid)) == 1) return finish(list, keyword, x, mid - 1);
    if (mid < y && compare(keyword, list.get(mid)) == 0) return finish(list, keyword, mid + 1, y);
    if (compare(keyword, list.get(mid)) == 0) return mid;
    return mid - 1;

  }
}

public class AppCommandline {

  public static void showAllWorlds() {
    System.out.println("No\t|English\t\t\t|Vietnamese");
    int i = 1;
    for (String word_target : Dictionary.map.keySet()) {
      System.out.println((i) + "\t|" + word_target + "\t\t\t\t|" + Dictionary.map.get(word_target));
      i++;
    }
  }

  public static void dictionaryBasic() {
    AppManager.insertFromCommandline();
    showAllWorlds();
  }

  public static void dictionaryAdvanced(String word_target) throws IOException {
    AppManager.insertFromFile();
    showAllWorlds();
    AppManager.dictionaryLookup(word_target);
  }

  public static List<String> dictionarySearcher(String word) {
    if (word.equals("")) return new ArrayList<>();
    List<String> list = new ArrayList<>(Dictionary.map.keySet());

    if (SearchWords.condition(list, word)) {

      int t = SearchWords.BinarySearchWords(list, word, 0, list.size() - 1);
      int st = SearchWords.start(list, word, 0, t);
      int fi = SearchWords.finish(list, word, t, list.size() - 1);
      return list.subList(st, fi + 1);

    }
    return new ArrayList<>();
  }
}

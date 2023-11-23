package dictionary;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class DatabaseConnection {
    public static  TreeMap<String, String> treeMap = new TreeMap<>();

    static {
        String fileName = "src/main/resources/dictionary/dataFromSql.txt"; // Tên file cần đọc
        try {
            String str = Files.readString(Paths.get(fileName));
            String[] strs = str.split("\\|");
            //System.out.println(strs.length / 2);
            for (int i = 0; i < strs.length / 2; i ++) {
                treeMap.put(strs[2*i], strs[2*i + 1]);
            }
            System.out.println(treeMap.size());
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    public static List<Word> select(String newvalue) {
        List<Word> list = new ArrayList<>();
        for (Map.Entry<String, String> entry : treeMap.entrySet()) {
            if (entry.getKey().startsWith(newvalue)) {
                Word word = new Word(entry.getKey(), entry.getValue());
                list.add(word);
            }
        }
        return list;
    }
    // Phương thức để truy vấn 5 dữ liệu từ điển một cách ngẫu nhiên
    public static List<Word> selectRandom() {
        List<Word> result = new ArrayList<>();
        List<Map.Entry<String, String>> entries = new ArrayList<>(treeMap.entrySet());
        Collections.shuffle(entries);
        List<Map.Entry<String, String>> randomEntries = entries.subList(0, 8);
        for (Map.Entry<String, String> entry : randomEntries) {
            result.add(new Word(result.size() + 1, entry.getKey(),entry.getValue()));
        }
        return result;
    }


    // Phương thức để thêm dữ liệu từ điển vào file SQL
    public static void add(Word word) {
        treeMap.put(word.getTarget(), word.getDefinition());
    }

    // Phương thức để cập nhật dữ liệu từ điển trong file SQL
    public static void update(Word word) {
        treeMap.remove(word.getTarget());
        treeMap.put(word.getTarget(), word.getDefinition());
    }

    // Phương thức để xóa dữ liệu từ điển trong file SQL
    public static void delete(Word word) {
        treeMap.remove(word.getTarget());
    }
}

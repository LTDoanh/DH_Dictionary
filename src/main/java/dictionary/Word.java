package dictionary;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Word {

  private String type;
  private int id; // Mã số của từ
  private String target; // Từ tiếng Anh hoặc tiếng Việt
  private String definition; // Nghĩa của từ

  public String getType() {
    return type;
  }

  public Word() {
  }

  public Word(String target, String definition) {
    this.target = target;
    this.definition = definition;
  }

  public Word(int id, String target, String definition) {
    this.id = id;
    this.target = target;
    this.definition = definition;
  }

  public Word(String type, Word word) {
    this.target = word.target;
    this.definition = word.definition;
    this.type = type;
  }

  private String HTMLtoString(String HTMLstr) {
    Document doc = Jsoup.parse(HTMLstr);
    Element body = doc.body();
    String text = body.wholeText();
    return text;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getTarget() {
    return target;
  }

  public void setTarget(String target) {
    this.target = target;
  }

  public String getDefinition() {
    return HTMLtoString(definition);
  }

  public void setDefinition(String definition) {
    this.definition = definition;
  }

  @Override
  public String toString() {
    return target;
  }
}

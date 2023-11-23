package dictionary;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class VoiceAPI extends API {

  // Khai báo các hằng số
  private static final String apiKey = "5bd2a9430ad24a83b91d3b7fa83d3fef";
  public static final String audioFilePath = "C:\\Users\\HUY\\Downloads\\demo2 (1) (2)\\src\\main\\resources\\dictionary\\media\\audio\\voice.wav";

  @Override
  public String useAPI(String text) {
    try {
      String urlText = URLEncoder.encode(text, "UTF-8");
      URL url = new URL("http://api.voicerss.org/?key=" + apiKey + "&hl=en-us&src=" + urlText);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("GET");
      InputStream audioStream = connection.getInputStream();
      FileOutputStream fileOutputStream = new FileOutputStream(audioFilePath);
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = audioStream.read(buffer)) != -1) {
        fileOutputStream.write(buffer, 0, bytesRead);
      }
      fileOutputStream.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return "1";
  }

}
package dictionary;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class TranslateAPI extends API{

  @Override
  public String useAPI(String text) throws IOException {
    String urlStr =
        "https://script.google.com/macros/s/AKfycbxl1laF3m9YjV0mQ5cfUkSKouKg6uHQ80ezLFt4TrYlrDSDsBaj-_3XfX5ytyvdfBvC/exec"
            +
            "?q=" + URLEncoder.encode(text, "UTF-8") +
            "&target=" + "vi" +
            "&source=" + "en";
    URL url = new URL(urlStr);
    StringBuilder response = new StringBuilder();
    HttpURLConnection con = (HttpURLConnection) url.openConnection();
    con.setRequestProperty("User-Agent", "Mozilla/5.0");
    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
    String inputLine;
    while ((inputLine = in.readLine()) != null) {
      response.append(inputLine);
    }
    in.close();
    return response.toString();
  }
}
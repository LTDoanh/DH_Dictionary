package dictionary;

import java.io.FileOutputStream;
import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.Languages;
import com.voicerss.tts.SpeechDataEvent;
import com.voicerss.tts.SpeechDataEventListener;
import com.voicerss.tts.SpeechErrorEvent;
import com.voicerss.tts.SpeechErrorEventListener;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;

public class Voice {

  // Khai báo các hằng số
  private static final String apiKey = "5bd2a9430ad24a83b91d3b7fa83d3fef";
  private static final String voiceFile = "C:\\Users\\DELL\\Downloads\\demo2\\src\\main\\resources\\dictionary\\media\\audio\\voice.mp3";

  public static String TextToVoice(String text) throws Exception {
    VoiceProvider tts = new VoiceProvider(apiKey);

    VoiceParameters params = new VoiceParameters(text, Languages.English_UnitedStates);
    params.setCodec(AudioCodec.WAV);
    params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
    params.setBase64(false);
    params.setSSML(false);
    params.setRate(0);

    tts.addSpeechErrorEventListener(new SpeechErrorEventListener() {
      @Override
      public void handleSpeechErrorEvent(SpeechErrorEvent e) {
        System.out.print(e.getException().getMessage());
      }
    });

    tts.addSpeechDataEventListener(new SpeechDataEventListener() {
      @Override
      public void handleSpeechDataEvent(SpeechDataEvent e) {
        try {
          byte[] voice = (byte[]) e.getData();

          FileOutputStream fos = new FileOutputStream(voiceFile);
          fos.write(voice, 0, voice.length);
          fos.flush();
          fos.close();
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
    });
    tts.speechAsync(params);
    return voiceFile;
  }
}
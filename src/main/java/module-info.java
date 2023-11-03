module dictionary {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;
    requires javafx.media;
    requires java.sql;
    requires org.jsoup;
    requires voicerss.tts;

    opens dictionary to javafx.fxml;
    exports dictionary;
}
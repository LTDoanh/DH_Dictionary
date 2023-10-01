module com.example.dh_dictionary {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.dh_dictionary to javafx.fxml;
    exports com.example.dh_dictionary;
  exports;
  opens to
}
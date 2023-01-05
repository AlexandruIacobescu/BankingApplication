module com.uvt.bankingapplication {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.uvt.bankingapplication to javafx.fxml;
    exports com.uvt.bankingapplication;
}
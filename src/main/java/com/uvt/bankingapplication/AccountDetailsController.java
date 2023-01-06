package com.uvt.bankingapplication;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.skin.NestedTableColumnHeader;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class AccountDetailsController implements Initializable {
    private final String url = "jdbc:mysql://localhost:3306/bank";
    private final String uname = "root";
    private final String password = "Zeppelin2001";
    @FXML
    public Label clientLabel;
    @FXML
    public Button backButton, exitButton;
    @FXML
    public TextArea detailsTextArea;
    @FXML
    public ComboBox<String> accountsComboBox;

    public void exitButtonCLick(){
        Platform.exit();
    }

    public void backButtonClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("client-operations-chooser-view.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Client Operations Center");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void accountsComboBoxIndexChanged() throws SQLException {
        StringBuilder detailsSb = new StringBuilder();
        detailsTextArea.clear();
        Connection conn = DriverManager.getConnection(url, uname, password);
        PreparedStatement stmt = conn.prepareStatement("SELECT account_number,currency,amount,IBAN FROM accounts WHERE IBAN = ?");
        stmt.setString(1, accountsComboBox.getValue());
        ResultSet set = stmt.executeQuery();
        while(set.next()){
            detailsSb.append("Account number: " + set.getString("account_number")).append("\n");
            detailsSb.append("Currency: " + set.getString("currency")).append("\n");
            detailsSb.append("Balance: " + set.getString("amount")).append("\n");
            detailsSb.append("IBAN: " + set.getString("IBAN")).append("\n");
        }
        detailsTextArea.setText(detailsSb.toString());
        conn.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            Connection conn = DriverManager.getConnection(this.url, uname, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT IBAN FROM accounts WHERE client_id = (SELECT client_id FROM clients WHERE name = ?);");
            stmt.setString(1, HelloApplication.clientName);
            ResultSet set = stmt.executeQuery();
            while(set.next()){
                accountsComboBox.getItems().add(set.getString(1));
            }
            conn.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        clientLabel.setText("Hello, " + HelloApplication.clientName);
    }
}

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
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientDetailsController implements Initializable {
    private final String url = "jdbc:mysql://localhost:3306/bank";
    private final String uname = "root";
    private final String password = "Zeppelin2001";
    @FXML
    public Button logoutButton, backButton;
    @FXML
    public TextArea detailsTextArea;

    public void backButtonClick(ActionEvent e) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("client-operations-chooser-view.fxml")));
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Client Operations Center");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    public void logoutButtonClick(){
        Platform.exit();
    }

    public String getClientAddress() {
        try{
            Connection conn = DriverManager.getConnection(url, uname, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT address FROM clients WHERE name = ?;");
            stmt.setString(1, HelloApplication.clientName);
            ResultSet set = stmt.executeQuery();
            set.next();
            return set.getString(1);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    public String numberOfActiveAccounts(){
        try{
            Connection conn = DriverManager.getConnection(url, uname, password);
            PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(account_number) FROM accounts WHERE client_id = (SELECT client_id FROM clients WHERE name = ?);");
            stmt.setString(1, HelloApplication.clientName);
            ResultSet set = stmt.executeQuery();
            set.next();
            return set.getString(1);
        }
        catch (SQLException e){
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        StringBuilder sb = new StringBuilder();
        sb.append("Client Name: ").append(HelloApplication.clientName).append("\n");
        sb.append("Client Address: ").append(getClientAddress()).append("\n");
        sb.append("Active Accounts: ").append(numberOfActiveAccounts()).append("\n");

        detailsTextArea.setText(sb.toString());
    }
}

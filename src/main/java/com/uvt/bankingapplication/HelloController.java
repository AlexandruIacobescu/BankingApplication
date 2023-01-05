package com.uvt.bankingapplication;

import com.uvt.bankingapplication.classes.Message;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.*;
import java.util.Objects;

public class HelloController {
    private final String url = "jdbc:mysql://localhost:3306/bank";
    private final String uname = "root";
    private final String password = "Zeppelin2001";
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    public Button quitButton;
    @FXML
    public Button loginButton;
    @FXML
    public TextField clientCodeTextField, clientPasswordTextField;
    @FXML
    public Label errorLabel;

    private void setClientName() throws SQLException {
        Connection conn = DriverManager.getConnection(url, uname, password);
        String queryString = "SELECT name FROM clients WHERE client_code = ? AND client_password = ?;";
        PreparedStatement stmt = conn.prepareStatement(queryString);
        stmt.setString(1, clientCodeTextField.getText());
        stmt.setString(2, clientPasswordTextField.getText());
        ResultSet res = stmt.executeQuery();
        res.next();
        HelloApplication.clientName = res.getString(1);
        conn.close();
    }

    public void loginButtonClick(ActionEvent e) throws SQLException, IOException {
        Connection conn = DriverManager.getConnection(url, uname, password);
        String queryStr = "SELECT COUNT(client_code) FROM clients WHERE client_code = ? AND client_password = ?;";
        PreparedStatement stmt = conn.prepareStatement(queryStr);
        stmt.setString(1, clientCodeTextField.getText());
        stmt.setString(2, clientPasswordTextField.getText());
        ResultSet res = stmt.executeQuery();
        res.next();
        if(res.getString(1).equals("1")) {
            setClientName();
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("client-operations-chooser-view.fxml")));
            stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setTitle("Client Operations Center");
            stage.setScene(scene);
            stage.centerOnScreen();
            stage.show();
            conn.close();
        }
        else{
            clientCodeTextField.setText("");
            clientPasswordTextField.setText("");
            Message msg = new Message(errorLabel, 4000);
            msg.start();
        }
    }

    public void quitButtonClick(){
        Platform.exit();
    }
}
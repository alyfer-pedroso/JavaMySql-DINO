package com.alyfer.register;

import com.alyfer.login.Login;
import com.alyfer.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class Register extends JDialog {
    private JPanel registerPanel;
    private JButton btnRegister;
    private JButton btnLogin;
    private JTextField tfUser;
    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JPasswordField pfConfirmPassword;

    public Register(JFrame parent) {
        super(parent);
        setTitle("DINO - Register");
        setContentPane(registerPanel);
        setMinimumSize(new Dimension(1024, 768));
        setSize(1280, 720);
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(2);

        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerNewUser();
            }
        });
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login(null);
            }
        });

        setVisible(true);
    }

    private void registerNewUser() {
        String name = tfUser.getText();
        String email = tfEmail.getText();
        String password = String.valueOf(pfPassword.getText());
        String confirmPassword = String.valueOf(pfConfirmPassword.getText());

        if(name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Por favor complete todas as áreas",
                    "Dino - ERROR",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        user =addUserToDatabase(name, email, password);

        if(user != null) {
            JOptionPane.showMessageDialog(this,
                    "Novo usuário cadastrado: " + user.name,
                    "Sucesso no Cadastro",
                    JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this,
                    "Falha ao cadastrar um novo usuário",
                    "Tente denovo",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    public User user;

    public User addUserToDatabase(String name, String email, String password) {
        User user = null;

        final String MYSQL_SERVER_URL = "jdbc:mysql://localhost/";
        final String DB_URL = "jdbc:mysql://localhost/DINO?serverTimezone=UTC";
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {
            //Criar a Database se não existir
            Connection conn = DriverManager.getConnection(MYSQL_SERVER_URL, USERNAME, PASSWORD);
            Statement statement = conn.createStatement();
            statement.executeUpdate("CREATE DATABASE IF NOT EXISTS DINO");
            statement.close();
            conn.close();

            //Conectar-se na Database e criar uma table "users" se ela não existir
            conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            statement = conn.createStatement();
            String SQL = "CREATE TABLE IF NOT EXISTS users " +
                    "(id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    " name VARCHAR(200) NOT NULL," +
                    " email VARCHAR(200) NOT NULL UNIQUE," +
                    " password VARCHAR(200) NOT NULL) ";
            statement.executeUpdate(SQL);

            //Inserindo os dados na Database
            statement = conn.createStatement();
            SQL = "INSERT INTO users (name, email, password)" + "VALUE (?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(SQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, email);
            preparedStatement.setString(3, password);

            int addedRows = preparedStatement.executeUpdate();
            if(addedRows > 0) {
                user = new User();
                user.name = name;
                user.email = email;
                user.password = password;
            }

            statement.close();
            conn.close();


        } catch (Exception err) {
            err.printStackTrace();
        }

        return user;
    }

    public static void main(String[] args) {
        Register register = new Register(null);
        User user = register.user;

        if(user != null) {
            System.out.println(user.name + ", cadastrado com sucesso");
        } else {
            System.out.println("Cadastro cancelado");
        }
    }

}

package com.alyfer.login;

import com.alyfer.main.Main;
import com.alyfer.register.Register;
import com.alyfer.user.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.sql.*;

public class Login extends JDialog {
    private JPanel loginPanel;
    private JTextField tfUser;
    private JButton btnRegister;
    private JButton btnLogin;
    private JPasswordField pfPassword;

    public Login(JFrame parent) {
        super(parent);
        setTitle("DINO - Login");
        setContentPane(loginPanel);
        setMinimumSize(new Dimension(1024, 768));
        setSize(1280, 720);
        setModal(true);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(2);



        btnRegister.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Register register = new Register(null);
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = tfUser.getText();
                String password = String.valueOf(pfPassword.getText());

                user = getAuthenticatedUsers(name, password);

                if(user != null) {
                    JOptionPane.showMessageDialog(Login.this,
                            "Autenticação de  " + user.name + "  realizada com sucesso!" +
                                    "\nEmail:  " + user.email,
                            "Sucesso no Cadastro",
                            JOptionPane.INFORMATION_MESSAGE);
                    dispose();

                    Main main = new Main();
                    main.lbUsername.setText(user.name);
                    main.lbEmail.setText(user.email);

                } else {
                    JOptionPane.showMessageDialog(Login.this,
                            "Email ou Senha incorreto!",
                            "Tente de novo",
                            JOptionPane.ERROR_MESSAGE);
                }

            }
        });

        tfUser.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if(tfUser.getText().equals("USUÁRIO")) {
                    tfUser.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if(tfUser.getText().equals("")) {
                    tfUser.setText("USUÁRIO");
                }
            }
        });

        pfPassword.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                super.focusGained(e);
                if(String.valueOf(pfPassword.getText()).equals("123456")) {
                    pfPassword.setText("");
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                super.focusLost(e);
                if(String.valueOf(pfPassword.getText()).equals("")) {
                    pfPassword.setText("123456");
                }
            }
        });

        setVisible(true);
    }

    public User user;

    public User getAuthenticatedUsers(String name, String password) {
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

            //Pegando dados da Database
            statement = conn.createStatement();
            SQL = "SELECT * FROM users WHERE name=? AND password=?";
            PreparedStatement preparedStatement = conn.prepareStatement(SQL);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, password);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("name");
                user.email = resultSet.getString("email");
                user.password = resultSet.getString("password");
            }

            statement.close();
            conn.close();

        } catch (Exception err) {
            err.printStackTrace();
        }

        return user;
    }


    public static void main(String[] args) {
        Login login = new Login(null);
        User user = login.user;
        if(user != null) {
            System.out.println("Autenticação de: " + user.name + " realizada com sucesso!");
            System.out.println("             Email: " + user.email);
        } else {
            System.out.println("Autenticação cancelada!");
        }


    }

}

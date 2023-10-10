package com.alyfer.main;

import com.alyfer.login.Login;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Main extends JFrame {
    private JPanel mainPanel;
    public JLabel lbUsername;
    public JLabel lbEmail;
    private JButton btnLogout;

    public Main() {
        setTitle("DINO - Perfil");
        setContentPane(mainPanel);
        setMinimumSize(new Dimension(1024, 768));
        setSize(1280, 720);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);



        btnLogout.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Login login = new Login(null);
            }
        });

        setVisible(true);
    }


    public static void main(String[] args) {
        Main main = new Main();
    }


}

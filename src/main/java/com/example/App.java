package com.example;

import com.example.models.Database;
import com.example.views.LoginView;

public class App {
    public static void main(String[] args) {
        Database.initialize();
        LoginView loginView = new LoginView();
        loginView.setVisible(true);
    }
}
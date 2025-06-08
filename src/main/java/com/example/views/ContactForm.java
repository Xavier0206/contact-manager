package com.example.views;

import com.example.models.Contact;
import com.example.models.Database;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ContactForm extends JDialog {
    private Contact contact;
    private JTextField phoneField, nameField, emailField, businessField, countryField;
    
    public ContactForm(JFrame parent, Contact contact) {
        super(parent, "Formulario de Contacto", true);
        this.contact = contact;
        
        setSize(400, 300);
        setLocationRelativeTo(parent);
        
        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(new JLabel("Teléfono*:"));
        phoneField = new JTextField(contact.getPhone());
        panel.add(phoneField);
        
        panel.add(new JLabel("Nombre*:"));
        nameField = new JTextField(contact.getName());
        panel.add(nameField);
        
        panel.add(new JLabel("Email:"));
        emailField = new JTextField(contact.getEmail());
        panel.add(emailField);
        
        panel.add(new JLabel("Negocio:"));
        businessField = new JTextField(contact.getBusiness());
        panel.add(businessField);
        
        panel.add(new JLabel("País:"));
        countryField = new JTextField(contact.getCountry());
        panel.add(countryField);
        
        JButton saveButton = new JButton("Guardar");
        saveButton.addActionListener(this::saveContact);
        
        JButton cancelButton = new JButton("Cancelar");
        cancelButton.addActionListener(e -> dispose());
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        
        add(panel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private void saveContact(ActionEvent e) {
        // Validación básica
        if (phoneField.getText().isEmpty() || nameField.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Teléfono y Nombre son obligatorios", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        contact.setPhone(phoneField.getText());
        contact.setName(nameField.getText());
        contact.setEmail(emailField.getText());
        contact.setBusiness(businessField.getText());
        contact.setCountry(countryField.getText());
        
        Database.saveContact(contact);
        dispose();
    }
}
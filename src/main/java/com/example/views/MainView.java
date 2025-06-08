package com.example.views;

import com.example.models.Contact;
import com.example.models.Database;
import com.example.controllers.ContactController;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class MainView extends JFrame {
    private JTable contactTable;
    private DefaultTableModel tableModel;
    private JTextField nameFilterField;
    private JTextField countryFilterField;
    
    public MainView() {
        setTitle("Gestión de Contactos");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel de filtros
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        filterPanel.add(new JLabel("Filtrar por nombre:"));
        nameFilterField = new JTextField(20);
        filterPanel.add(nameFilterField);
        
        filterPanel.add(new JLabel("Filtrar por país:"));
        countryFilterField = new JTextField(20);
        filterPanel.add(countryFilterField);
        
        // Botones
        JButton addButton = new JButton("Agregar");
        addButton.addActionListener(this::addContact);
        
        JButton editButton = new JButton("Editar");
        editButton.addActionListener(this::editContact);
        
        JButton deleteButton = new JButton("Eliminar");
        deleteButton.addActionListener(this::deleteContact);
        
        JButton exportButton = new JButton("Exportar PDF");
        exportButton.addActionListener(this::exportPDF);
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(exportButton);
        
        // Tabla
        tableModel = new DefaultTableModel(
            new Object[]{"ID", "Teléfono", "Nombre", "Email", "Negocio/Venta", "País"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        contactTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(contactTable);
        
        // Layout principal
        setLayout(new BorderLayout());
        add(filterPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
        
        // Cargar datos
        loadContacts();
        
        // Listeners para filtros en tiempo real
        nameFilterField.getDocument().addDocumentListener(new FilterListener());
        countryFilterField.getDocument().addDocumentListener(new FilterListener());
    }
    
    private void loadContacts() {
        tableModel.setRowCount(0);
        List<Contact> contacts = Database.getContacts(
            nameFilterField.getText(), 
            countryFilterField.getText()
        );
        
        for (Contact contact : contacts) {
            tableModel.addRow(new Object[]{
                contact.getId(),
                contact.getPhone(),
                contact.getName(),
                contact.getEmail(),
                contact.getBusiness(),
                contact.getCountry()
            });
        }
    }
    
    private void addContact(ActionEvent e) {
        ContactForm form = new ContactForm(this, new Contact());
        form.setVisible(true);
        loadContacts();
    }
    
    private void editContact(ActionEvent e) {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow >= 0) {
            int id = (int) tableModel.getValueAt(selectedRow, 0);
            Contact contact = new Contact();
            contact.setId(id);
            contact.setPhone((String) tableModel.getValueAt(selectedRow, 1));
            contact.setName((String) tableModel.getValueAt(selectedRow, 2));
            contact.setEmail((String) tableModel.getValueAt(selectedRow, 3));
            contact.setBusiness((String) tableModel.getValueAt(selectedRow, 4));
            contact.setCountry((String) tableModel.getValueAt(selectedRow, 5));
            
            ContactForm form = new ContactForm(this, contact);
            form.setVisible(true);
            loadContacts();
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void deleteContact(ActionEvent e) {
        int selectedRow = contactTable.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(
                this, 
                "¿Eliminar contacto?", 
                "Confirmar", 
                JOptionPane.YES_NO_OPTION
            );
            
            if (confirm == JOptionPane.YES_OPTION) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                Database.deleteContact(id);
                loadContacts();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione un contacto", "Error", JOptionPane.WARNING_MESSAGE);
        }
    }
    
    private void exportPDF(ActionEvent e) {
        // Se implementa en el controlador
        ContactController.exportToPDF(tableModel);
    }
    
    private class FilterListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) { loadContacts(); }
        @Override
        public void removeUpdate(DocumentEvent e) { loadContacts(); }
        @Override
        public void changedUpdate(DocumentEvent e) { loadContacts(); }
    }
}
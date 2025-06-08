package com.example.models;

public class Contact {
    private int id;
    private String phone;
    private String name;
    private String email;
    private String business;
    private String country;
    
    public Contact() {}
    
    public Contact(int id, String phone, String name, String email, String business, String country) {
        this.id = id;
        this.phone = phone;
        this.name = name;
        this.email = email;
        this.business = business;
        this.country = country;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getBusiness() { return business; }
    public void setBusiness(String business) { this.business = business; }
    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }
}
package com.example.back.controller.dto;

import java.time.LocalDate;

public class Api {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String birthday;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    @Override
    public String toString() {
        return "Usuários [id=" + id + ", Nome=" + firstname + ", Sobrenome=" + lastname + ", Email=" + email + ", Telefone=" + phone + ", Data Nascimento=" + birthday+ "]";
    }
}

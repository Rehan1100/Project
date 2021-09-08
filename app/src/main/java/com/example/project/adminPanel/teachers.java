package com.example.project.adminPanel;

public class teachers {
    String name;
    String email;
    String pass;
    String gender;
    String role;
    String profession;

    public teachers(String name, String email, String pass, String gender, String role, String semester) {
        this.name = name;
        this.email = email;
        this.pass = pass;
        this.gender = gender;
        this.role = role;
        this.profession = semester;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSemester() {
        return profession;
    }

    public void setSemester(String semester) {
        this.profession = semester;
    }
}

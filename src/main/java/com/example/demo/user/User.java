package com.example.demo.user;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    private Long companyId;
    private String companyName;
    private String role;

    @Column(length = 100000)
    private String authorizations;

    public User(Long id, String name, String surname, Long companyId, String companyName, String role, String authorizations) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.companyId = companyId;
        this.companyName = companyName;
        this.role = role;
        this.authorizations = authorizations;
    }

    public User(String name, String surname, Long companyId, String companyName, String role, String authorizations) {
        this.name = name;
        this.surname = surname;
        this.companyId = companyId;
        this.companyName = companyName;
        this.role = role;
        this.authorizations = authorizations;
    }

    public User() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getAuthorizations() {
        return  authorizations;
    }

    public void setAuthorizations(String authorizations) {
        this.authorizations = authorizations;
    }
}
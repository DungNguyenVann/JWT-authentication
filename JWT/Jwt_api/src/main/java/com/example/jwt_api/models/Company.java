package com.example.jwt_api.models;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "company")
public class Company {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(max=100)
    private String name;
    @NotBlank
    @Size(max = 50)
    private String email;

    @Size(min= 10,max=500)
    private String description;

    @Size(max=100)
    private String address;

    @Size(max=20)
    private String phoneNum;
    private String logoCompany;

    @Transient
    public String getLogoPath(){
        if (logoCompany == null || id==null) return null;

        return "/images_company/" +id +"/"+logoCompany;
    }


    public Company(){}
    public Company(String name, String email, String description, String address, String phoneNum, String logoCompany) {
        this.name = name;
        this.email = email;
        this.description = description;
        this.address = address;
        this.phoneNum = phoneNum;
        this.logoCompany = logoCompany;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getLogoCompany() {
        return logoCompany;
    }

    public void setLogoCompany(String logoCompany) {
        this.logoCompany = logoCompany;
    }
}

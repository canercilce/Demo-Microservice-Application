package com.example.demo.vehicle;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String plate;
    private String chassisNumber;
    private String label;
    private String brand;
    private String model;
    private Long modelYear;
    private Long companyId;

    public Vehicle(Long id, String plate, String chassisNumber, String label, String brand, String model, Long modelYear, Long companyId) {
        this.id = id;
        this.plate = plate;
        this. chassisNumber = chassisNumber;
        this.label = label;
        this.brand = brand;
        this.model = model;
        this.modelYear = modelYear;
        this.companyId = companyId;
    }

    public Vehicle(String plate, String chassisNumber, String label, String brand, String model, Long modelYear, Long companyId) {
        this.plate = plate;
        this. chassisNumber = chassisNumber;
        this.label = label;
        this.brand = brand;
        this.model = model;
        this.modelYear = modelYear;
        this.companyId = companyId;
    }

    public Vehicle() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getChassisNumber() {
        return chassisNumber;
    }

    public void setChassisNumber(String chassisNumber) {
        this.chassisNumber = chassisNumber;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Long getModelYear() {
        return modelYear;
    }

    public void setModelYear(Long modelYear) {
        this.modelYear = modelYear;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}

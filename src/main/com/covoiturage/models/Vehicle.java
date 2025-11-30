package com.covoiturage.models;

import java.util.Objects;

public class Vehicle {
    private Long id;
    private Long driverId; // User id
    private String model;
    private int capacity;
    private String constraints; // JSON string (e.g., baggage size, accessibility)

    public Vehicle() {}

    public Vehicle(Long id, Long driverId, String model, int capacity, String constraints) {
        this.id = id;
        this.driverId = driverId;
        this.model = model;
        this.capacity = capacity;
        this.constraints = constraints;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getConstraints() { return constraints; }
    public void setConstraints(String constraints) { this.constraints = constraints; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof Vehicle)) return false; Vehicle v = (Vehicle) o; return Objects.equals(id, v.id);}    
    @Override public int hashCode() { return Objects.hash(id);}    
}
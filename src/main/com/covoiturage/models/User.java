package com.covoiturage.models;

import java.time.LocalTime;
import java.util.Objects;

public class User {
    private Long id;
    private String name;
    private String startAddress;
    private String endAddress;
    private String preferences; // JSON string for simplicity
    private String availability; // e.g., "Mon-Fri 08:00-10:00,17:00-19:00"

    public User() {}

    public User(Long id, String name, String startAddress, String endAddress, String preferences, String availability) {
        this.id = id;
        this.name = name;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.preferences = preferences;
        this.availability = availability;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }

    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }

    public String getPreferences() { return preferences; }
    public void setPreferences(String preferences) { this.preferences = preferences; }

    public String getAvailability() { return availability; }
    public void setAvailability(String availability) { this.availability = availability; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof User)) return false; User user = (User) o; return Objects.equals(id, user.id);}    
    @Override public int hashCode() { return Objects.hash(id);}    
}
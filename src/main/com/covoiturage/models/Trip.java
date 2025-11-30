package com.covoiturage.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Trip {
    private Long id;
    private Long driverId; // User id
    private Long vehicleId;
    private String startAddress;
    private String endAddress;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private double totalDistanceKm;
    private double totalDurationMin;
    private List<Long> passengerIds = new ArrayList<>();

    public Trip() {}

    public Trip(Long id, Long driverId, Long vehicleId, String startAddress, String endAddress, LocalDateTime departureTime, LocalDateTime arrivalTime) {
        this.id = id;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDriverId() { return driverId; }
    public void setDriverId(Long driverId) { this.driverId = driverId; }

    public Long getVehicleId() { return vehicleId; }
    public void setVehicleId(Long vehicleId) { this.vehicleId = vehicleId; }

    public String getStartAddress() { return startAddress; }
    public void setStartAddress(String startAddress) { this.startAddress = startAddress; }

    public String getEndAddress() { return endAddress; }
    public void setEndAddress(String endAddress) { this.endAddress = endAddress; }

    public LocalDateTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalDateTime departureTime) { this.departureTime = departureTime; }

    public LocalDateTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalDateTime arrivalTime) { this.arrivalTime = arrivalTime; }

    public double getTotalDistanceKm() { return totalDistanceKm; }
    public void setTotalDistanceKm(double totalDistanceKm) { this.totalDistanceKm = totalDistanceKm; }

    public double getTotalDurationMin() { return totalDurationMin; }
    public void setTotalDurationMin(double totalDurationMin) { this.totalDurationMin = totalDurationMin; }

    public List<Long> getPassengerIds() { return passengerIds; }
    public void setPassengerIds(List<Long> passengerIds) { this.passengerIds = passengerIds; }

    @Override public boolean equals(Object o) { if (this == o) return true; if (!(o instanceof Trip)) return false; Trip t = (Trip) o; return Objects.equals(id, t.id);}    
    @Override public int hashCode() { return Objects.hash(id);}    
}
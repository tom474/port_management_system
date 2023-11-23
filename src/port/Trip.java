package port;

import vehicle.Vehicle;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;

public class Trip implements Serializable {
    private String tripID;
    private String vehicle;
    private double fuelConsuming;
    private LocalDate departureDate;
    private LocalDate arrivalDate;
    private String departurePort;
    private String arrivalPort;
    private String status;

    public Trip() {
        this.tripID = "Default";
        this.vehicle = null;
        this.fuelConsuming = 0;
        this.departureDate = null;
        this.arrivalDate = null;
        this.departurePort = null;
        this.arrivalPort = null;
        this.status = "Default";
    }

    public Trip(String tripID, String vehicle, double fuelConsuming, LocalDate departureDate, LocalDate arrivalDate, String departurePort, String arrivalPort, String status) {
        this.tripID = tripID;
        this.vehicle = vehicle;
        this.fuelConsuming = fuelConsuming;
        this.departureDate = departureDate;
        this.arrivalDate = arrivalDate;
        this.departurePort = departurePort;
        this.arrivalPort = arrivalPort;
        this.status = status;
    }

    public String getTripID() {
        return tripID;
    }

    public void setTripID(String tripID) {
        this.tripID = tripID;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public double getFuelConsuming() {
        return fuelConsuming;
    }

    public void setFuelConsuming(double fuelConsuming) {
        this.fuelConsuming = fuelConsuming;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDate departureDate) {
        this.departureDate = departureDate;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public void setArrivalDate(LocalDate arrivalDate) {
        this.arrivalDate = arrivalDate;
    }

    public String getDeparturePort() {
        return departurePort;
    }

    public void setDeparturePort(String departurePort) {
        this.departurePort = departurePort;
    }

    public String getArrivalPort() {
        return arrivalPort;
    }

    public void setArrivalPort(String arrivalPort) {
        this.arrivalPort = arrivalPort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String formattedDepartureDate = departureDate.format(dateFormatter);
        String formattedArrivalDate = arrivalDate.format(dateFormatter);
        Formatter formatter = new Formatter();
        return String.valueOf(formatter.format("| %-8s | %-18s | %-20s | %-20s | %-16s | %-16s | %-16s |", tripID, vehicle, departurePort, arrivalPort, formattedDepartureDate, formattedArrivalDate, status));
    }
}

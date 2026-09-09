package src.main.java.model;

public record Drone(
    String id,
    String model,
    int battery,
    boolean available,
    String ubication
){
}

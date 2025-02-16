package org.example.assignment.starwars.dtos.responses;

import lombok.Data;
import java.util.List;

@Data
public class VehicleResponse extends BaseResponse {
    private String name;
    private String model;
    private String vehicleClass;
    private String manufacturer;
    private String costInCredits;
    private String length;
    private String crew;
    private String passengers;
    private String maxAtmospheringSpeed;
    private String cargoCapacity;
    private String consumables;
    private List<String> pilots;
    private List<PeopleResponse> pilotList;
}

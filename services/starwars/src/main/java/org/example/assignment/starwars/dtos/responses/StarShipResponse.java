package org.example.assignment.starwars.dtos.responses;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class StarShipResponse extends VehicleResponse {
    private String starshipClass;
    private String hyperDriveRating;
    private String mglt;
}

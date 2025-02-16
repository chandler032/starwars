package org.example.assignment.starwars.dtos.responses;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BaseResponse {
    private List<String> species;
    private List<String> starships;
    private List<String> vehicles;
    private List<String> planets;
    private List<String> films;
    private List<String> people;

    private String url;
    private LocalDateTime created;
    private LocalDateTime edited;

    private List<SpeciesResponse> speciesList;
    private List<StarShipResponse> starshipList;
    private List<VehicleResponse> vehicleList;
    private List<PlanetResponse> planetList;
    private List<FilmResponse> filmList;
    private List<PeopleResponse> peopleList;
}

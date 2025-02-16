package org.example.assignment.starwars.dtos.responses;

import lombok.Data;

import java.util.List;

@Data
public class PlanetResponse extends BaseResponse {
    private String name;
    private String diameter;
    private String rotationPeriod;
    private String orbitalPeriod;
    private String gravity;
    private String population;
    private String climate;
    private String terrain;
    private String surfaceWater;
    private List<String> residents;
}

package org.example.assignment.starwars.dtos.responses;

import lombok.Data;

@Data
public class SpeciesResponse extends BaseResponse {
    private String name;
    private String classification;
    private String designation;
    private String averageHeight;
    private String averageLifespan;
    private String eyeColors;
    private String hairColors;
    private String skinColors;
    private String language;
    private String homeworld;
}

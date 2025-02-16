package org.example.assignment.starwars.dtos.responses;

import lombok.Data;

@Data
public class PeopleResponse extends BaseResponse {
    private String name;
    private String birthYear;
    private String eyeColor;
    private String gender;
    private String hairColor;
    private String height;
    private String mass;
    private String skinColor;
    private String homeworld;
}

package org.example.assignment.data.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Planet implements Serializable {
    private static final long serialVersionUId = 1L;

    private String name;
    private String diameter;
    @JsonProperty("rotation_period")
    private String rotationPeriod;
    @JsonProperty("orbital_period")
    private String orbitalPeriod;
    private String gravity;
    private String population;
    private String climate;
    private String terrain;
    @JsonProperty("surface_water")
    private String surfaceWater;
    private List<String> residents;
    private List<String> films;
    private String url;
    private LocalDateTime created;
    private LocalDateTime edited;
}

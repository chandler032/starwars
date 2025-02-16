package org.example.assignment.data.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Starship extends Vehicle implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("starship_class")
    private String starshipClass;
    @JsonProperty("hyperdrive_rating")
    private String hyperDriveRating;
    @JsonProperty("MGLT")
    private String mglt;
}

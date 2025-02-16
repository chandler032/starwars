package org.example.assignment.data.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Species implements Serializable {
    private static final long serialVersionUId = 1L;

    private String name;
    private String classification;
    private String designation;
    @JsonProperty("average_height")
    private String averageHeight;
    @JsonProperty("average_lifespan")
    private String averageLifespan;
    @JsonProperty("eye_colors")
    private String eyeColors;
    @JsonProperty("hair_colors")
    private String hairColors;
    @JsonProperty("skin_colors")
    private String skinColors;
    private String language;
    private String homeworld;
    private List<String> films;
    private List<String> people;
    private String url;
    private LocalDateTime created;
    private LocalDateTime edited;
}

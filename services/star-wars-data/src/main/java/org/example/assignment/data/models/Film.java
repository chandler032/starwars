package org.example.assignment.data.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Film implements Serializable {
    private static final long serialVersionUId = 1L;

    private String title;
    private String director;
    private String producer;

    @JsonProperty("episode_id")
    private int episodeId;
    @JsonProperty("opening_crawl")
    private String openingCrawl;
    @JsonProperty("release_date")
    private LocalDate releaseDate;

    private List<String> species;
    private List<String> starships;
    private List<String> vehicles;
    private List<String> planets;
    private List<String> characters;

    private String url;
    private LocalDateTime created;
    private LocalDateTime edited;
}

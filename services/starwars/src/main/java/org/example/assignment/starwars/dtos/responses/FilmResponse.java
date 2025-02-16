package org.example.assignment.starwars.dtos.responses;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class FilmResponse extends BaseResponse {
    private String title;
    private String director;
    private String producer;
    private int episodeId;
    private String openingCrawl;
    private LocalDate releaseDate;
    private List<String> characters;
}

package org.example.assignment.starwars.controllers;

import io.swagger.v3.oas.annotations.Operation;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.services.StarWarsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api")
public class StarWarsController {
    @Autowired
    private StarWarsService starWarsService;

    @GetMapping("/greetings")
    @Operation(summary = "Greetings API", description = "Just a simple greet from service")
    public Map<String, String> getGreetings() {
        Map<String, String> map = new HashMap<>();
        map.put("hello", "world");
        return map;
    }

    @GetMapping("/search")
    @Operation(summary = "Search API", description = "Search for Star Wars data. Takes 2 params and 1 optional param: type, value and page url")
    public ResponseEntity<ResponseWrapper<?>> search(@RequestParam("type") String type,
                                                     @RequestParam("value") String searchValue,
                                                     @RequestParam("pageUrl") Optional<String> pageUrl)
            throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(starWarsService.search(type, searchValue, pageUrl), HttpStatus.OK);
    }

    @PutMapping("/offline")
    @Operation(summary = "Offline Enabling API", description = "Put the data service in offline mode")
    public ResponseEntity<?> enableOfflineMode(@RequestParam("enable") boolean enable) {
        starWarsService.enableOfflineMode(enable);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}

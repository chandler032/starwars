package org.example.assignment.starwars.services;

import org.example.assignment.starwars.dtos.responses.BaseResponse;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public interface StarWarsService {
    ResponseWrapper<? extends List<? extends BaseResponse>> search(
            String type, String searchValue, Optional<String> pageUrl)
            throws ExecutionException, InterruptedException;

    void enableOfflineMode(boolean enable);
}

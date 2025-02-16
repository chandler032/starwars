package org.example.assignment.starwars.services.strategy;

import org.example.assignment.starwars.dtos.responses.BaseResponse;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.exceptions.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

public interface DataServiceStrategy<T extends BaseResponse> {
    ResponseWrapper<List<T>> search(String searchValue, Optional<String> pageUrl) throws ResourceNotFoundException;
}

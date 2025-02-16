package org.example.assignment.starwars.services.strategy.components;

import lombok.extern.slf4j.Slf4j;
import org.example.assignment.data.models.Vehicle;
import org.example.assignment.data.responses.DataResponseWrapper;
import org.example.assignment.rest.CustomRestClient;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.dtos.responses.VehicleResponse;
import org.example.assignment.starwars.exceptions.ResourceNotFoundException;
import org.example.assignment.starwars.services.external.DataSourceService;
import org.example.assignment.starwars.services.strategy.DataServiceStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service("vehicle")
@Slf4j
public class VehicleService implements DataServiceStrategy<VehicleResponse> {

    @Autowired
    private DataSourceService dataSourceService;

    @Autowired
    @Qualifier("vehicleRestClient")
    private CustomRestClient restClient;

    @Override
    public ResponseWrapper<List<VehicleResponse>> search(String searchValue, Optional<String> pageUrl) {
        try {
            ResponseWrapper<List<VehicleResponse>> vehicleResponseWrapper = new ResponseWrapper<>();

            DataResponseWrapper<List<Vehicle>> vehicleDataResponseWrapper = dataSourceService
                    .getData(restClient, "/?search=" + searchValue,
                            new ParameterizedTypeReference<>() {
                            });

            if (Objects.nonNull(vehicleDataResponseWrapper)) {
                if (vehicleDataResponseWrapper.getResults().isEmpty()) {
                    throw new ResourceNotFoundException("Vehicle not found with search value: " + searchValue);
                }
                List<Vehicle> vehicleList = vehicleDataResponseWrapper.getResults();
                List<VehicleResponse> vehicleResponses = transferVehicleData(vehicleList);
                vehicleResponseWrapper.setResult(vehicleResponses);

                if (Objects.nonNull(vehicleDataResponseWrapper.getNext())) {
                    vehicleResponseWrapper.setNext(vehicleDataResponseWrapper.getNext());
                }

                if (Objects.nonNull(vehicleDataResponseWrapper.getPrevious())) {
                    vehicleResponseWrapper.setPrevious(vehicleDataResponseWrapper.getPrevious());
                }

                if (vehicleDataResponseWrapper.getCount() > 0) {
                    vehicleResponseWrapper.setCount(vehicleDataResponseWrapper.getCount());
                }
            }

            return vehicleResponseWrapper;
        } catch (Exception e) {
            log.error("Error occurred while searching for vehicle: {}", e.getMessage());
            throw e;
        }
    }

    private List<VehicleResponse> transferVehicleData(List<Vehicle> vehicleList) {
        return vehicleList.stream().map(vehicle -> {
            VehicleResponse vehicleResponse = new VehicleResponse();
            vehicleResponse.setName(vehicle.getName());
            vehicleResponse.setModel(vehicle.getModel());
            vehicleResponse.setManufacturer(vehicle.getManufacturer());
            vehicleResponse.setCostInCredits(vehicle.getCostInCredits());
            vehicleResponse.setLength(vehicle.getLength());
            vehicleResponse.setMaxAtmospheringSpeed(vehicle.getMaxAtmospheringSpeed());
            vehicleResponse.setCrew(vehicle.getCrew());
            vehicleResponse.setPassengers(vehicle.getPassengers());
            vehicleResponse.setCargoCapacity(vehicle.getCargoCapacity());
            vehicleResponse.setConsumables(vehicle.getConsumables());
            vehicleResponse.setVehicleClass(vehicle.getVehicleClass());
            vehicleResponse.setPilots(vehicle.getPilots());
            vehicleResponse.setFilms(vehicle.getFilms());
            vehicleResponse.setCreated(vehicle.getCreated());
            vehicleResponse.setEdited(vehicle.getEdited());
            vehicleResponse.setUrl(vehicle.getUrl());
            return vehicleResponse;
        }).toList();
    }
}

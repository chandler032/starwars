package org.example.assignment.starwars.services.impls;

import lombok.extern.slf4j.Slf4j;
import org.example.assignment.mapcache.components.OfflineMode;
import org.example.assignment.starwars.dtos.responses.BaseResponse;
import org.example.assignment.starwars.dtos.responses.FilmResponse;
import org.example.assignment.starwars.dtos.responses.PeopleResponse;
import org.example.assignment.starwars.dtos.responses.ResponseWrapper;
import org.example.assignment.starwars.exceptions.InvalidSearchRequestException;
import org.example.assignment.starwars.exceptions.ResourceNotFoundException;
import org.example.assignment.starwars.services.StarWarsService;
import org.example.assignment.starwars.services.external.CacheService;
import org.example.assignment.starwars.services.strategy.DataServiceStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
@Slf4j
public class StarWarsServiceImpl implements StarWarsService {
    @Autowired
    private DataServiceStrategyFactory strategyFactory;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private CacheService cacheService;

    @Override
    public ResponseWrapper<? extends List<? extends BaseResponse>> search(
            String type, String searchValue, Optional<String> pageUrl)
            throws ExecutionException, InterruptedException {
        try {
            log.info("Searching for {} with search value: {}", type, searchValue);
            if (Objects.isNull(type) || Objects.isNull(searchValue)
                    || type.isEmpty() || searchValue.isEmpty()) {
                throw new InvalidSearchRequestException("Search type or keyword shouldn't be empty");
            }
            if (searchValue.length() < 3) {
                throw new InvalidSearchRequestException("Search keyword should be at least 3 characters long");
            }

            if (OfflineMode.getInstance().isOfflineModeEnabled()) {
                List<BaseResponse> baseResponseList = fetchFromCache(type, searchValue);
                if (CollectionUtils.isEmpty(baseResponseList)) {
                    throw new ResourceNotFoundException("No offline data found in cache for search key: " + searchValue);
                }
                return new ResponseWrapper<>(null, null, baseResponseList.size(), baseResponseList);
            }

            ResponseWrapper<? extends List<? extends BaseResponse>> response = strategyFactory
                    .getSearchStrategy(type)
                    .search(searchValue, pageUrl);

            Optional<String> responseNextUrlOptional = Optional.ofNullable(response.getNext());
            if (responseNextUrlOptional.isPresent()) {
                String[] nextUrlParts = responseNextUrlOptional.get().split("\\?");
                response.setNext("?" + nextUrlParts[1]);
            }

            Optional<String> responsePrevUrlOptional = Optional.ofNullable(response.getPrevious());
            if (responsePrevUrlOptional.isPresent()) {
                String[] prevUrlParts = responsePrevUrlOptional.get().split("\\?");
                response.setNext("?" + prevUrlParts[1]);
            }

            if (!CollectionUtils.isEmpty(response.getResult())) {

                if (type.equalsIgnoreCase("film")) {
                    List<FilmResponse> filmResponseList = response.getResult()
                            .stream()
                            .map(FilmResponse.class::cast)
                            .toList();
                    for (FilmResponse baseResponse : filmResponseList) {
                        List<Future<Optional<PeopleResponse>>> peopleFutureList = new ArrayList<>();
                        List<String> peopleUrlList = baseResponse.getCharacters();
                        for (String peopleUrl : peopleUrlList) {
                            peopleFutureList.add(asyncService.getPeopleData(peopleUrl));
                        }
                        List<PeopleResponse> peopleResponseList = new ArrayList<>();
                        for (Future<Optional<PeopleResponse>> future : peopleFutureList) {
                            Optional<PeopleResponse> peopleResponseOptional = future.get();
                            peopleResponseOptional.ifPresent(peopleResponseList::add);
                        }
                        baseResponse.setPeopleList(peopleResponseList);
                    }
                } else {
                    List<? extends BaseResponse> baseResponseList = response.getResult();
                    for (BaseResponse baseResponse : baseResponseList) {
                        List<Future<Optional<FilmResponse>>> filmFututeList = new ArrayList<>();
                        List<String> filmUrlList = baseResponse.getFilms();
                        for (String filmUrl : filmUrlList) {
                            filmFututeList.add(asyncService.getFilmData(filmUrl));
                        }
                        List<FilmResponse> filmResponseList = new ArrayList<>();
                        for (Future<Optional<FilmResponse>> future : filmFututeList) {
                            Optional<FilmResponse> filmResponseOptional = future.get();
                            filmResponseOptional.ifPresent(filmResponseList::add);
                        }
                        baseResponse.setFilmList(filmResponseList);
                    }
                }
            }

            asyncService.addToCache(type, response.getResult(), cacheService);

            return response;
        } catch (Exception e) {
            log.error("Error occurred while searching for {}: {}", type, e.getMessage());
            throw e;
        }
    }

    @Override
    public void enableOfflineMode(boolean enable) {
        try {
            log.info("Setting offline mode to: {}", enable);
            OfflineMode.getInstance().setOfflineModeEnabled(enable);
        } catch (Exception e) {
            log.error("Error occurred while enabling offline mode: {}", e.getMessage());
            throw e;
        }
    }

    private List<BaseResponse> fetchFromCache(String type, String searchKey) {
        List<Object> cacheDataList = cacheService.getFromCache(type, searchKey);
        if (!CollectionUtils.isEmpty(cacheDataList)) {
            return cacheDataList
                    .stream()
                    .filter(BaseResponse.class::isInstance)
                    .map(BaseResponse.class::cast)
                    .toList();
        }

        return new ArrayList<>();
    }
}

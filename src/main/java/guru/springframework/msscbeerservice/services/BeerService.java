package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPageList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import javassist.NotFoundException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface BeerService {
    BeerDto getById(UUID beerId, Boolean showInventory) throws NotFoundException;

    BeerDto saveNewBeer(BeerDto beerDto);

    List<BeerDto> getAllBeers(Boolean showInventory);

    BeerPageList listBeers(String beerName, BeerStyleEnum beerStyle, Boolean showInventory, PageRequest pageRequest);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto) throws NotFoundException;

    boolean deleteBeerById(UUID beerId);

    BeerDto getByUpc(String upc, Boolean showInventory) throws NotFoundException;
}

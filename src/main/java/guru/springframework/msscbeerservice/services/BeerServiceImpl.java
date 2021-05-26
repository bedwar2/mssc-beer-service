package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import guru.springframework.msscbeerservice.web.mappers.BeerMapper;
import sfg.brewery.model.BeerDto;
import sfg.brewery.model.BeerPageList;
import sfg.brewery.model.BeerStyleEnum;
import io.micrometer.core.instrument.util.StringUtils;
import javassist.NotFoundException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    public BeerServiceImpl(BeerRepository beerRepository, BeerMapper beerMapper) {
        this.beerRepository = beerRepository;
        this.beerMapper = beerMapper;
    }

    @Override
    @Cacheable(cacheNames = "beerCache", key = "#beerId", condition = "#showInventory == false")
    public BeerDto getById(UUID beerId, Boolean showInventory) throws NotFoundException {
        if (showInventory) {
            return beerMapper.BeerToBeerDtoWithInventory(beerRepository.findById(beerId).orElseThrow(() -> new NotFoundException("Beer was not found")));
        } else
            return beerMapper.BeerToBeerDto(beerRepository.findById(beerId).orElseThrow(() -> new NotFoundException("Beer was not found")));
    }

    @Override
    @Cacheable(cacheNames = "beerUpcCache", key = "#upc", condition = "#showInventory == false")
    public BeerDto getByUpc(String upc, Boolean showInventory) throws NotFoundException {
        if (showInventory) {
            return beerMapper.BeerToBeerDtoWithInventory(beerRepository.findBeerByUpc(upc).orElseThrow(() -> new NotFoundException("Beer not found for upc " + upc)));
        } else {
            return beerMapper.BeerToBeerDto(beerRepository.findBeerByUpc(upc).orElseThrow(() -> new NotFoundException("Beer not found for upc " + upc)));
        }
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {
        return beerMapper.BeerToBeerDto(beerRepository.save(beerMapper.BeerDtoToBeer(beerDto)));
    }

    @Override
    public List<BeerDto> getAllBeers(Boolean showInventory) {
        List<BeerDto> beerDtos;
        if (showInventory) {
            beerDtos =
                    StreamSupport.stream(beerRepository.findAll().spliterator(), false)
                            .map(beerMapper::BeerToBeerDtoWithInventory)
                            .collect(Collectors.toList());

        } else
            beerDtos =
                StreamSupport.stream(beerRepository.findAll().spliterator(), false)
                .map(beerMapper::BeerToBeerDto)
                .collect(Collectors.toList());
        //List<BeerDto> beerDtos = new ArrayList<>();
        //beers.forEach(beer -> beerDtos.add(beerMapper.BeerToBeerDto(beer)));

        return beerDtos;
    }

    @Override
    @Cacheable(cacheNames = "beerListCache", condition="#showInventory == false")
    public BeerPageList listBeers(String beerName, BeerStyleEnum beerStyle, Boolean showInventory, PageRequest pageRequest) {
        BeerPageList beerPageList;
        Page<Beer> beerPage;

        if (!StringUtils.isEmpty(beerName) && beerStyle !=null && !StringUtils.isEmpty(beerStyle.toString())) {
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle.toString(), pageRequest);
        } else if (!StringUtils.isEmpty(beerName)) {
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (beerStyle!=null && !StringUtils.isEmpty(beerStyle.toString())) {
            beerPage = beerRepository.findAllByBeerStyle(beerStyle.toString(), pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        if (showInventory)
            beerPageList = new BeerPageList(beerPage
                .getContent()
                .stream()
                .map(beerMapper::BeerToBeerDtoWithInventory)
                .collect(Collectors.toList()), PageRequest
                                                .of(beerPage.getPageable().getPageNumber(),
                                                        beerPage.getPageable().getPageSize()),
                                                        beerPage.getTotalElements());
        else
            beerPageList = new BeerPageList(beerPage
                    .getContent()
                    .stream()
                    .map(beerMapper::BeerToBeerDto)
                    .collect(Collectors.toList()), PageRequest
                    .of(beerPage.getPageable().getPageNumber(),
                            beerPage.getPageable().getPageSize()),
                    beerPage.getTotalElements());

        return beerPageList;

    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) throws NotFoundException {
        Beer beer = beerRepository.findById(beerId).orElseThrow(() -> new NotFoundException("Not Found"));
        BeerDto savedBeer = null;
        if (beer !=null) {
            beer.setBeerName(beerDto.getBeerName());
            beer.setBeerStyle(beerDto.getBeerStyle().toString());
            beer.setMinOnHand(beerDto.getQuantityOnHand());
            beer.setPrice(beerDto.getPrice());
            beer.setUpc(beerDto.getUpc());
            beer.setVersion(beer.getVersion() + 1);
            beer.setLastModifiedDate(Timestamp.valueOf(LocalDateTime.now()));

            savedBeer = beerMapper.BeerToBeerDto(beerRepository.save(beerMapper.BeerDtoToBeer(beerDto)));
        }
        return savedBeer;
    }

    @Override
    public boolean deleteBeerById(UUID beerId) {
        boolean exists = beerRepository.existsById(beerId);
        beerRepository.deleteById(beerId);
        return exists && (beerRepository.existsById(beerId) == false);
    }


}

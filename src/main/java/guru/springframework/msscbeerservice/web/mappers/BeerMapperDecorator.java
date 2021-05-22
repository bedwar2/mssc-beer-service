package guru.springframework.msscbeerservice.web.mappers;

import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BeerMapperDecorator implements BeerMapper {
    private BeerInventoryService beerInventoryService;
    private BeerMapper mapper;

    @Autowired
    public void setBeerInventoryService(BeerInventoryService beerInventoryService) {
        this.beerInventoryService = beerInventoryService;
    }

    @Autowired
    public void setMapper(BeerMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public BeerDto BeerToBeerDtoWithInventory(Beer beer) {
        BeerDto dto = mapper.BeerToBeerDto(beer);

        dto.setQuantityOnHand(beerInventoryService.getOnhandInventory(beer.getId()));
        System.out.println("Mapper Decorator add inventory " + dto);

        return dto;
    }

    @Override
    public BeerDto BeerToBeerDto(Beer beer) {
        return mapper.BeerToBeerDto(beer);
    }

    @Override
    public Beer BeerDtoToBeer(BeerDto beerDto) {
        return mapper.BeerDtoToBeer(beerDto);
    }
}

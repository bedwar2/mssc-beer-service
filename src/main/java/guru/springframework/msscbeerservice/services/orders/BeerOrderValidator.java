package guru.springframework.msscbeerservice.services.orders;

import guru.springframework.msscbeerservice.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sfg.brewery.model.BeerDto;
import sfg.brewery.model.BeerOrderDto;
import sfg.brewery.model.BeerOrderLineDto;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class BeerOrderValidator {

    private final BeerService beerService;

    public Boolean validateOrder(BeerOrderDto beerOrderDto) {
        List<BeerDto> beerDtoList = beerService.getAllBeers(false);

        AtomicBoolean allUPCsValid = new AtomicBoolean(true);
        beerOrderDto.getBeerOrderLines().forEach((BeerOrderLineDto orderLineDto) -> {
            if (beerDtoList.stream().filter(o -> o.getUpc().equalsIgnoreCase(orderLineDto.getUpc())).count() == 0) {
                allUPCsValid.set(false);
                return;
            }
        });

        return allUPCsValid.get();
    }

}

package guru.springframework.msscbeerservice.bootstrap;


import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class BeerLoader implements CommandLineRunner {

    private final BeerRepository beerRepository;

    public BeerLoader(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBeerObjects();
        
    }

    private void loadBeerObjects() {
        if (beerRepository.count() == 0) {
            beerRepository.save(Beer.builder().beerName("Mango Bob's").beerStyle("Lager").quantityToBrew(200).upc(45464564665L).price(new BigDecimal(12.96)).build());
            beerRepository.save(Beer.builder().beerName("Galaxy Cat").beerStyle("Pale Ale").quantityToBrew(300).upc(4332464646L).price(new BigDecimal(7.96)).build());

        }

        System.out.println("Loaded Beers: " + beerRepository.count());
    }
}

package guru.springframework.msscbeerservice.bootstrap;


import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

//@Component
public class BeerLoader implements CommandLineRunner {

    private final BeerRepository beerRepository;
    public final static String BEER_1_UPC = "0631234200036";
    public final static String BEER_2_UPC = "0631234300019";
    public final static String BEER_3_UPC = "0083783375213";

    public BeerLoader(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadBeerObjects();
        
    }

    private void loadBeerObjects() {
        if (beerRepository.count() == 0) {
            beerRepository.save(Beer.builder().beerName("Mango Bob's").beerStyle("Lager").quantityToBrew(200).upc(BEER_1_UPC).price(new BigDecimal(12.96)).build());
            beerRepository.save(Beer.builder().beerName("Galaxy Cat").beerStyle("Pale Ale").quantityToBrew(300).upc(BEER_2_UPC).price(new BigDecimal(7.96)).build());
            beerRepository.save(Beer.builder().beerName("No Hammers on the Bar").beerStyle("Pale Ale").quantityToBrew(300).upc(BEER_3_UPC).price(new BigDecimal(8.98)).build());

        }

        System.out.println("Loaded Beers: " + beerRepository.count());
    }
}

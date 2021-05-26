package guru.springframework.msscbeerservice.services.brewing;

import guru.springframework.msscbeerservice.config.JmsConfig;
import guru.springframework.msscbeerservice.domain.Beer;
import guru.springframework.msscbeerservice.repositories.BeerRepository;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import sfg.brewery.model.BeerDto;
import sfg.brewery.model.events.BeerBrewEvent;
import sfg.brewery.model.events.NewInventoryEvent;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class BrewBeerListener {

    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;

   // @Transactional  //super important, gives a lazy initialization error - since setting the QOH
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(@Payload BeerBrewEvent beerBrewEvent) throws NotFoundException {
        BeerDto beerDto = beerBrewEvent.getBeerDto();

        //Beer beer = beerRepository.getOne(beerDto.getId());
        Beer beer = beerRepository.findById(beerDto.getId()).orElseThrow(() -> new NotFoundException("Beer Id not found"));

        beerDto.setQuantityOnHand(beer.getQuantityToBrew());

        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);

        log.debug("Brewed beer " + beer.getBeerName() + " - Min on Hand: " + beer.getMinOnHand() + " - QOH: "  + beerDto.getQuantityOnHand());

        jmsTemplate.convertAndSend(JmsConfig.BEER_INVENTORY_QUEUE, newInventoryEvent);

    }
}

package guru.springframework.msscbeerservice.services.orders;

import guru.springframework.msscbeerservice.config.JmsConfig;
import guru.springframework.msscbeerservice.services.BeerService;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sfg.brewery.model.BeerDto;
import sfg.brewery.model.BeerOrderDto;
import sfg.brewery.model.BeerOrderLineDto;
import sfg.brewery.model.events.ValidateBeerOrderRequest;
import sfg.brewery.model.events.ValidateOrderResult;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@RequiredArgsConstructor
public class ValidateOrderListener {


    private final BeerOrderValidator beerOrderValidator;
    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JmsConfig.VALIDATE_ORDER)
    public void listen(@Payload ValidateBeerOrderRequest validateBeerOrderRequest) {
        BeerOrderDto beerOrderDto = validateBeerOrderRequest.getBeerOrderDto();
        Boolean isValid = beerOrderValidator.validateOrder(beerOrderDto);

        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESULT,
                ValidateOrderResult.builder().beerOrderId(beerOrderDto.getId()).isValid(isValid).build());

    }
}

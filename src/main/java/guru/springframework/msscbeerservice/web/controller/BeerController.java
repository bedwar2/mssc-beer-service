package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/beer")
public class BeerController {

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId) {

        return new ResponseEntity<>(BeerDto.builder().beerName("Coors Light").id(beerId).upc(123L).beerStyle(BeerStyleEnum.LAGER).build(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity saveNewBeer(@RequestBody @Validated BeerDto beerDto) {
        HttpHeaders headers = new HttpHeaders();
        beerDto.setId(UUID.randomUUID());
        //to do get hostname to url
        headers.add("Location", "http://localhost:8080/api/v1/beer/" + beerDto.getId().toString());

        return new ResponseEntity(headers, HttpStatus.CREATED);
    }

    @PutMapping("/{beerId}")
    public ResponseEntity updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto) {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/{beerId}")
    public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID beerId)
    {
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


    //Example returns BadRequest for Testing with Test Client
    /*
    @DeleteMapping("/{beerId}")
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public void deleteBeerById(@PathVariable("beerId") UUID beerId)
    {
        //return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    */
}

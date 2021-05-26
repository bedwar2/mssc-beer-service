package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.services.BeerService;
import sfg.brewery.model.BeerDto;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/beerUpc")
public class BeerUpcController {

    private final BeerService beerService;

    @GetMapping("/{upc}")
    public ResponseEntity<BeerDto> getBeerByUpc(@PathVariable("upc") String upc, @RequestParam(value = "showInventory", required = false) Boolean showInventory) throws NotFoundException {
        if (showInventory==null)
            showInventory = false;

        BeerDto beerDto = beerService.getByUpc(upc, showInventory);
        return new ResponseEntity<>(beerDto, HttpStatus.OK);
    }
}

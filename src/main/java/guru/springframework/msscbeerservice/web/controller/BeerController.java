package guru.springframework.msscbeerservice.web.controller;

import guru.springframework.msscbeerservice.services.BeerService;
import guru.springframework.msscbeerservice.web.model.BeerDto;
import guru.springframework.msscbeerservice.web.model.BeerPageList;
import guru.springframework.msscbeerservice.web.model.BeerStyleEnum;
import javassist.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/beer")
public class BeerController {

    private static final Integer DEFAULT_PAGE_NUMBER = 0;
    private static final Integer DEFAULT_PAGE_SIZE = 25;

    //@RequiredArgsConstructor causes the default constructor to be created for the
    //private final - pretty cool!
    private final BeerService beerService;

    @GetMapping("/{beerId}")
    public ResponseEntity<BeerDto> getBeerById(@PathVariable("beerId") UUID beerId, @RequestParam(value = "showInventory", required = false) Boolean showInventory) throws NotFoundException {
        if (showInventory==null)
            showInventory = false;

        BeerDto beerDto = beerService.getById(beerId, showInventory);
        return new ResponseEntity<>(beerDto, HttpStatus.OK);
    }

    @GetMapping("/fullList")
    public ResponseEntity<List<BeerDto>> getFullList(@RequestParam(value = "showInventory", required = false) Boolean showInventory) {
        if (showInventory == null) {
            showInventory =false;
        }
        return new ResponseEntity<>(beerService.getAllBeers(showInventory), HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<BeerPageList> getBeers(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize,
            @RequestParam(value = "beerName", required = false) String beerName,
            @RequestParam(value = "beerStyle", required = false) BeerStyleEnum beerStyle,
            @RequestParam(value = "showInventory", required = false) Boolean showInventory) {

        if (pageNumber == null || pageNumber < 0) {
            pageNumber = DEFAULT_PAGE_NUMBER;
        }

        if (pageSize == null || pageSize < 1) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (showInventory == null) {
            showInventory = false;
        }


        return new ResponseEntity<>(beerService.listBeers(beerName, beerStyle, showInventory, PageRequest.of(pageNumber, pageSize)), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<BeerDto> saveNewBeer(@RequestBody @Validated BeerDto beerDto) {
        HttpHeaders headers = new HttpHeaders();
        BeerDto savedBeerDto = beerService.saveNewBeer(beerDto);
        //to do get hostname to url
        if (savedBeerDto.getId() != null)
            headers.add("Location", "http://localhost:8080/api/v1/beer/" + savedBeerDto.getId().toString());

        return new ResponseEntity(savedBeerDto, headers, HttpStatus.CREATED);

    }

    @PutMapping("/{beerId}")
    public ResponseEntity<BeerDto> updateBeerById(@PathVariable("beerId") UUID beerId, @Validated @RequestBody BeerDto beerDto) throws NotFoundException {
        //return new ResponseEntity(HttpStatus.NO_CONTENT);
        BeerDto updatedBeerDto =  beerService.updateBeer(beerId, beerDto);
        return new ResponseEntity<>(updatedBeerDto, HttpStatus.NO_CONTENT);
    }


    @DeleteMapping("/{beerId}")
    public ResponseEntity deleteBeerById(@PathVariable("beerId") UUID beerId)
    {
        boolean success = beerService.deleteBeerById(beerId);
        if (success)
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        else
            return new ResponseEntity(HttpStatus.NOT_FOUND);
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

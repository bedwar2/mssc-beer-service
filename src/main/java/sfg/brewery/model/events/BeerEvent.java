package sfg.brewery.model.events;

import sfg.brewery.model.BeerDto;
import lombok.*;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BeerEvent implements Serializable {

    private static final long serialVersionUID = -6796312894411711414L;
    private BeerDto beerDto;



}

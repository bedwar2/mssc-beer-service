package sfg.brewery.model.events;

import sfg.brewery.model.BeerDto;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class NewInventoryEvent extends BeerEvent {
    private static final long serialVersionUID = -7384621931450834485L;

    public NewInventoryEvent(BeerDto beerDto) {
        super(beerDto);
    }
}

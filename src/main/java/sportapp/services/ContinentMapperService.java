package sportapp.services;

import lombok.extern.java.Log;

import java.util.HashMap;
import java.util.Map;

@Log
public class ContinentMapperService {

    private final Map<String, String> countryToContinentMap = new HashMap<>();

    public ContinentMapperService() {
        countryToContinentMap.putIfAbsent("MEX", "NA");
        countryToContinentMap.putIfAbsent("POL", "EUR");
        countryToContinentMap.putIfAbsent("GER", "EUR");
        countryToContinentMap.putIfAbsent("USA", "NA");
        countryToContinentMap.putIfAbsent("RPA", "AFR");
        countryToContinentMap.putIfAbsent("URU", "AFR");
    }

    public String getContinentByCountry(String country) {
        if (!countryToContinentMap.containsKey(country)) {
            throw new IllegalStateException("Mapping for %s does not exists!".formatted(country));
        }
        return countryToContinentMap.get(country);
    }
}

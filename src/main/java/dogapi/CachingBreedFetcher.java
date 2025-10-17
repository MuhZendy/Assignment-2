package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {
    private int callsMade = 0;
    BreedFetcher fetcher;
    private HashMap<String, List<String>> subBreeds = new HashMap<>();

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
    }

    @Override
    public List<String> getSubBreeds(String breed) {
        if (subBreeds.containsKey(breed)) {
            return subBreeds.get(breed);
        }
        this.callsMade++;
        try {
            subBreeds.put(breed, this.fetcher.getSubBreeds(breed));
        } catch (BreedNotFoundException e) {
            throw new BreedNotFoundException(breed);
        }
        // return statement included so that the starter code can compile and run.
        return subBreeds.get(breed);
    }

    public int getCallsMade() {
        return callsMade;
    }
}
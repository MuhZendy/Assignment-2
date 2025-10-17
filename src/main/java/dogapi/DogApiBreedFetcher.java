package dogapi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

/**
 * BreedFetcher implementation that relies on the dog.ceo API.
 * Note that all failures get reported as BreedNotFoundException
 * exceptions to align with the requirements of the BreedFetcher interface.
 */
public class DogApiBreedFetcher implements BreedFetcher {

    private static final String API_URL = "https://dog.ceo/api/breeds/list/all";
    private final OkHttpClient client = new OkHttpClient();

    /**
     * Fetch the list of sub breeds for the given breed from the dog.ceo API.
     * @param breed the breed to fetch sub breeds for
     * @return list of sub breeds for the given breed
     */
    @Override
    public List<String> getSubBreeds(String breed) {
        Request request = new Request.Builder().url(API_URL).build();
        List<String> subBreeds = new ArrayList<>();

        try {
            final Response response = client.newCall(request).execute();
            final JSONObject jsonObject = new JSONObject(response.body().string());
            final JSONObject breeds = jsonObject.getJSONObject("message");
            if (!breeds.has(breed)) {
                throw new BreedNotFoundException(breed);
            }
            final JSONArray subbreeds = breeds.getJSONArray(breed);
            for (int i = 0; i < subbreeds.length(); i++) {
                subBreeds.add(subbreeds.getString(i));
            }
        } catch (BreedNotFoundException | IOException e) {
            throw new RuntimeException(e);
        }
        return subBreeds;
    }
}
package data_access.quotes;

import entity.Quote;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import use_case.quote.data_access_interface.QuoteDataAccessInterface;
import use_case.quote.data_access_interface.QuoteRetrievalException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;

public class ZenQuotesApiDataAccess implements QuoteDataAccessInterface {

    private static final String CACHE_FILE = "quote_cache.json";
    private final HttpClient httpClient;

    public ZenQuotesApiDataAccess() {
        this.httpClient = HttpClient.newHttpClient();
    }

    // Constructor for testing injection
    public ZenQuotesApiDataAccess(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public Quote fetchQuote(boolean forceUpdate) throws QuoteRetrievalException {
        // If forceUpdate is false, you technically could check the cache first,
        // but your requirement is "pulls from zenquotes API... cache as backup".
        // So we always try API first.
        try {
            return fetchFromApiAndCache();
        } catch (Exception e) {
            // API failed (Rate limit, Network error, etc.)
            System.err.println("API unreachable. Switching to cache: " + e.getMessage());
            try {
                return readFromCache();
            } catch (IOException cacheError) {
                throw new QuoteRetrievalException("Failed to fetch quote from API and cache is empty/unavailable", e);
            }
        }
    }

    private Quote fetchFromApiAndCache() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://zenquotes.io/api/random"))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Non-200 status: " + response.statusCode());
        }

        // Parse response from API (It comes as a JSONArray with 1 item)
        JSONArray responseArray = new JSONArray(response.body());
        JSONObject quoteJson = responseArray.getJSONObject(0);

        String text = quoteJson.getString("q");
        String author = quoteJson.getString("a");

        Quote quote = new Quote(text, author);

        // Add this specific quote object to our accumulated cache
        saveToCache(quoteJson);

        return quote;
    }

    private void saveToCache(JSONObject newQuoteJson) {
        try {
            File file = new File(CACHE_FILE);
            JSONArray cacheArray;

            // 1. Read existing cache if it exists
            if (file.exists()) {
                String content = Files.readString(file.toPath());
                try {
                    cacheArray = new JSONArray(content);
                } catch (JSONException e) {
                    // If file is corrupt, start fresh
                    cacheArray = new JSONArray();
                }
            } else {
                cacheArray = new JSONArray();
            }

            // 2. Check for duplicates (Prevent adding the same quote twice)
            boolean exists = false;
            String newQuoteText = newQuoteJson.getString("q");

            for (int i = 0; i < cacheArray.length(); i++) {
                JSONObject existing = cacheArray.getJSONObject(i);
                if (existing.getString("q").equals(newQuoteText)) {
                    exists = true;
                    break;
                }
            }

            // 3. Append if new
            if (!exists) {
                cacheArray.put(newQuoteJson);
                // Write back to file with indentation for readability
                Files.writeString(Paths.get(CACHE_FILE), cacheArray.toString(4));
            }

        } catch (IOException e) {
            System.err.println("Warning: Could not save quote to cache: " + e.getMessage());
        }
    }

    private Quote readFromCache() throws IOException {
        File file = new File(CACHE_FILE);
        if (!file.exists()) {
            throw new IOException("Cache file not found");
        }

        String content = Files.readString(file.toPath());
        JSONArray cacheArray = new JSONArray(content);

        if (cacheArray.isEmpty()) {
            throw new IOException("Cache file is empty");
        }

        // 4. Pick a RANDOM quote from the history
        SecureRandom random = new SecureRandom();
        int randomIndex = random.nextInt(cacheArray.length());

        JSONObject obj = cacheArray.getJSONObject(randomIndex);

        return new Quote(obj.getString("q"), obj.getString("a"));
    }
}
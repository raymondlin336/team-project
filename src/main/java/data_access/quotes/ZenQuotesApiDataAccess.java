package data_access.quotes;

import entity.Quote;
import org.json.JSONArray;
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

        try {
            return fetchFromApiAndCache();
        } catch (Exception e) {
            // API failed (Rate limit, Network error, etc.)
            // Attempt to read from backup file
            try {
                return readFromCache();
            } catch (IOException cacheError) {
                // If both fail, throw the original error or a combined one
                throw new QuoteRetrievalException("Failed to fetch quote and no cache available", e);
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

        // Parse
        JSONArray arr = new JSONArray(response.body());
        JSONObject obj = arr.getJSONObject(0);
        String text = obj.getString("q");
        String author = obj.getString("a");

        Quote quote = new Quote(text, author);

        // Save to file (Backup)
        saveToCache(quote, response.body());

        return quote;
    }

    private void saveToCache(Quote quote, String rawJson) {
        try {
            // Write the raw JSON to a local file
            Files.writeString(Paths.get(CACHE_FILE), rawJson);
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
        JSONArray arr = new JSONArray(content);
        JSONObject obj = arr.getJSONObject(0);

        return new Quote(obj.getString("q"), obj.getString("a"));
    }
}
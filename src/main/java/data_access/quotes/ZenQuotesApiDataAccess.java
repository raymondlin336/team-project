package data_access.quotes;

// layer: data_access

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

// Using some JSON library you already use in your project (e.g., Gson / org.json)
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import entity.Quote;
import use_case.quote.data_access_interface.QuoteDataAccessInterface;
import use_case.quote.data_access_interface.QuoteRetrievalException;

public class ZenQuotesApiDataAccess implements QuoteDataAccessInterface {

    private final HttpClient httpClient;

    public ZenQuotesApiDataAccess() {
        this.httpClient = HttpClient.newHttpClient();
    }

    @Override
    public Quote fetchQuote() throws QuoteRetrievalException {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://zenquotes.io/api/random"))
                    .GET()
                    .build();

            HttpResponse<String> response =
                    httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                throw new IOException("Non-200 status: " + response.statusCode());
            }

            // ZenQuotes returns something like: [{ "q": "...", "a": "..." }]
            JSONArray arr = new JSONArray(response.body());
            JSONObject obj = arr.getJSONObject(0);

            String text = obj.getString("q");
            String author = obj.getString("a");

            return new Quote(text, author);

        } catch (IOException | InterruptedException | JSONException e) {
            throw new QuoteRetrievalException("Failed to fetch quote", e);
        }
    }
}


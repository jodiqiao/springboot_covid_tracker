package jodiqiao.coronavirustracker.services;


// This will service the app with data
// When application loads, will make a call to the URL, fetches data
// Can update periodically

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service // makes spring service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://github.com/CSSEGISandData/COVID-19/blob/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    @PostConstruct // spring construct instance of service, after it's done, execute this method
    // Get data and parse it
    public void fetchVirusData() throws IOException, InterruptedException {
        // make https call to URL with data
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(httpResponse.body());
    }
}

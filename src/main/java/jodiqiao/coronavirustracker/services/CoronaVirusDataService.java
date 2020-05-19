package jodiqiao.coronavirustracker.services;


// This will service the app with data
// When application loads, will make a call to the URL, fetches data
// Can update periodically

import jodiqiao.coronavirustracker.models.LocationsStats;
import org.apache.commons.csv.CSVRecord;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;

@Service // makes spring service
public class CoronaVirusDataService {

    private static String VIRUS_DATA_URL = "https://raw.githubusercontent.com/CSSEGISandData/COVID-19/master/csse_covid_19_data/csse_covid_19_time_series/time_series_covid19_confirmed_global.csv";

    private List<LocationsStats> allStats = new ArrayList<>();
    @PostConstruct // spring construct instance of service, after it's done, execute this method
    // Get data and parse it
    @Scheduled(cron = "* * 1 * * *") // runs this at a set time
    public void fetchVirusData() throws IOException, InterruptedException {
        List<LocationsStats> newStats = new ArrayList<>(); // this is needed because this doesn't inhibit others
        // make https call to URL with data
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(VIRUS_DATA_URL))
                .build();
        HttpResponse<String> httpResponse = client.send(request, HttpResponse.BodyHandlers.ofString());
        // System.out.println(httpResponse.body());

        StringReader csvBodyReader = new StringReader(httpResponse.body());
        // apache commons csv header automatic detector
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);
        for (CSVRecord record : records) {
            LocationsStats locationsStat = new LocationsStats();
            locationsStat.setProvince(record.get(0)); // different from the tutorial: get by header name isn't working
            locationsStat.setCountry(record.get(1)); // but get column number works, with csv from html
            locationsStat.setLatestTotalCases(Integer.parseInt(record.get(record.size() - 1)));
            System.out.println(locationsStat);
            newStats.add(locationsStat);
        }
        this.allStats = newStats; // concurrency?
    }
}

package ciber.calendar.integration;

import ciber.calendar.model.backend.BackendLocation;
import ciber.calendar.model.backend.BackendUser;
import ciber.calendar.model.frontend.FrontendLocation;
import ciber.calendar.util.PropertyHolder;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import spark.Route;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static ciber.calendar.util.RouteParams.withParams;

public class Locations {

    public static Route allRoute() {
        return (request, response) -> {
            GetRequest getRequest = withParams(Unirest.get(locationserviceEndpoint()), request);

            String json = getRequest.asString().getBody();

            List<BackendLocation> backendLocations = Arrays.asList(new Gson().fromJson(json, BackendLocation[].class));

            List<FrontendLocation> frontendLocations = convert(backendLocations);

            return frontendLocations;
        };
    }

    public static Route singleRoute() {
        return (request, response) -> {
            String idStr = request.params(":id");
            String json = Unirest.get(locationserviceEndpoint()+"/"+idStr).asString().getBody();

            BackendLocation backendLocation = new Gson().fromJson(json, BackendLocation.class);

            if (backendLocation != null) {
                return convert(Arrays.asList(backendLocation)).get(0);
            }

            return null;
        };
    }

    public static List<FrontendLocation> fetch(Collection<Integer> locationIds) throws UnirestException {
        // For now.
        String json =  Unirest.get(locationserviceEndpoint()).asString().getBody();
        List<BackendLocation> backendLocations = Arrays.asList(new Gson().fromJson(json, BackendLocation[].class));
        return convert(backendLocations);
    }

    private static List<FrontendLocation> convert(List<BackendLocation> backendLocations) {
        return backendLocations.stream().map(FrontendLocation::new).collect(Collectors.toList());
    }

    private static String locationserviceEndpoint() {
        return PropertyHolder.getProperty("locationservice.endpoint");
    }

}

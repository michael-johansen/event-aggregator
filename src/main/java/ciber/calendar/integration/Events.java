package ciber.calendar.integration;

import ciber.calendar.model.backend.BackendEvent;
import ciber.calendar.model.frontend.FrontendEvent;
import ciber.calendar.model.frontend.FrontendLocation;
import ciber.calendar.model.frontend.FrontendUser;
import ciber.calendar.util.PropertyHolder;
import com.google.gson.*;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mashape.unirest.request.body.RequestBodyEntity;
import spark.Route;

import java.util.*;

import static ciber.calendar.util.RouteParams.*;

public class Events {

    public static Route allRoute() {
        return (request, response) -> {
            GetRequest getRequest = withParams(Unirest.get(eventserviceEndpoint()), request);

            String json = getRequest.asString().getBody();

            List<BackendEvent> backendEvents = Arrays.asList(new Gson().fromJson(json, BackendEvent[].class));

            List<FrontendEvent> frontendEvents = convert(backendEvents);

            return frontendEvents;
        };
    }

    public static Route singleRoute() {
        return (request, response) -> {
            String idStr = request.params(":id");
            String json = Unirest.get(eventserviceEndpoint()+"/"+idStr).asString().getBody();

            BackendEvent backendEvent = new Gson().fromJson(json, BackendEvent.class);

            if (backendEvent != null) {
                return convert(Arrays.asList(backendEvent)).get(0);
            }

            return null; // Errormessage-object? Exception?
        };
    }

    public static Route newRoute() {
        return (request, response) -> {
            String postedJson = request.body();
            FrontendEvent frontendEvent = new Gson().fromJson(postedJson, FrontendEvent.class);

            BackendEvent backendEvent = new BackendEvent(frontendEvent);
            RequestBodyEntity requestEntity = withJsonContent(Unirest.post(eventserviceEndpoint()))
                    .body(gson().toJson(backendEvent));

            response.status(requestEntity.asString().getStatus());
            return "{}";
        };
    }

    public static Route updateRoute() {
        return (request, response) -> {
            String idStr = request.params(":id");
            String putJson = request.body();
            FrontendEvent frontendEvent = new Gson().fromJson(putJson, FrontendEvent.class);

            BackendEvent backendEvent = new BackendEvent(frontendEvent);
            RequestBodyEntity requestEntity = withJsonContent(Unirest.put(eventserviceEndpoint() + "/" + idStr))
                    .body(gson().toJson(backendEvent));

            response.status(requestEntity.asString().getStatus());
            return "{}";
        };
    }

    public static Route deleteRoute() {
        return (request, response) -> {
            String idStr = request.params(":id");
            HttpRequestWithBody requestEntity = Unirest.delete(eventserviceEndpoint() + "/" + idStr);
            response.status(requestEntity.asString().getStatus());
            return "{}";
        };
    }

    private static List<FrontendEvent> convert(List<BackendEvent> backendEvents) throws UnirestException {
        if (backendEvents.isEmpty()) return new ArrayList<>();
        Map<Integer, FrontendEvent> frontendEvents = new HashMap<>();
        Map<Integer, List<Integer>> eventsForUsers = new HashMap<>();
        Map<Integer, List<Integer>> eventsForLocation = new HashMap<>();

        backendEvents.forEach(backendEvent -> {
            frontendEvents.put(backendEvent.id, new FrontendEvent(backendEvent));
            backendEvent.users.forEach(userId -> {
                if (!eventsForUsers.containsKey(userId)) {
                    eventsForUsers.put(userId, new ArrayList<>());
                }
                eventsForUsers.get(userId).add(backendEvent.id);
            });
            if (backendEvent.location != null) {
                if (!eventsForLocation.containsKey(backendEvent.location)) {
                    eventsForLocation.put(backendEvent.location, new ArrayList<>());
                }
                eventsForLocation.get(backendEvent.location).add(backendEvent.id);
            }
        });

        List<FrontendUser> connectedUsers = Users.fetch(eventsForUsers.keySet());
        connectedUsers.forEach(connectedUser -> {
            if (!eventsForUsers.containsKey(connectedUser.id)) return;
            eventsForUsers.get(connectedUser.id).forEach(eventId -> {
                frontendEvents.get(eventId).users.add(connectedUser);
            });
        });
        List<FrontendLocation> connectedLocations = Locations.fetch(eventsForLocation.keySet());
        connectedLocations.forEach(connectedLocation -> {
            if (!eventsForLocation.containsKey(connectedLocation.id)) return;
            eventsForLocation.get(connectedLocation.id).forEach(eventId -> {
                frontendEvents.get(eventId).location = connectedLocation;
            });
        });

        return new ArrayList<>(frontendEvents.values());
    }

    private static Gson gson() {
        return new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssX").create();
    }

    private static HttpRequestWithBody withJsonContent(HttpRequestWithBody request) {
        return request.header("Content-Type", "application/json");
    }

    private static String eventserviceEndpoint() {
        return PropertyHolder.getProperty("eventservice.endpoint");
    }

}

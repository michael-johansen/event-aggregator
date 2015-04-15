package ciber.calendar;

import ciber.calendar.integration.Events;
import ciber.calendar.integration.Locations;
import ciber.calendar.integration.Users;
import ciber.calendar.util.JsonTransformer;
import ciber.calendar.util.PropertyHolder;

import java.util.TimeZone;

import static spark.Spark.*;

public class Router {

    public static void main(String[] args) {
        PropertyHolder.setup();
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));

        JsonTransformer defaultJsonTransformer = new JsonTransformer();

        before((request, response) -> response.type("application/json"));
        get("/events", Events.allRoute(), defaultJsonTransformer);
        get("/events/:id", Events.singleRoute(), defaultJsonTransformer);
        post("/events", Events.newRoute());

        get("/locations", Locations.allRoute(), defaultJsonTransformer);

        get("/users", Users.allRoute(), defaultJsonTransformer);
        get("/users/:id", Users.singleRoute(), defaultJsonTransformer);

        exception(Exception.class, (e, request, response) -> {
            e.printStackTrace();
            response.status(500);
            response.body("Bugs, bugs everywhere!");
        });

    }

}


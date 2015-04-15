package ciber.calendar.integration;

import ciber.calendar.model.backend.BackendUser;
import ciber.calendar.model.backend.UserWrapper;
import ciber.calendar.model.frontend.FrontendUser;
import ciber.calendar.util.PropertyHolder;
import com.google.gson.Gson;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import spark.Route;

import java.util.*;
import java.util.stream.Collectors;

import static ciber.calendar.util.RouteParams.withParams;

public class Users {

    public static Route allRoute() {
        return (request, response) -> {
            GetRequest getRequest = withParams(Unirest.get(userserviceEndpoint()), request);

            String json = getRequest.asString().getBody();

            List<BackendUser> backendUsers = new Gson().fromJson(json, UserWrapper.class)._embedded.appUsers;

            List<FrontendUser> frontendUsers = convert(backendUsers);

            return frontendUsers;
        };
    }

    public static Route singleRoute() {
        return (request, response) -> {
            String idStr = request.params(":id");
            String json = Unirest.get(userserviceEndpoint()+"/"+idStr).asString().getBody();

            BackendUser backendUser = new Gson().fromJson(json, BackendUser.class);

            if (backendUser != null) {
                return convert(Arrays.asList(backendUser)).get(0);
            }

            return null;
        };
    }

    public static List<FrontendUser> fetch(Collection<Integer> userIds) throws UnirestException {
        // For now.
        String json = Unirest.get(userserviceEndpoint()).asString().getBody();
        List<BackendUser> backendUsers = new Gson().fromJson(json, UserWrapper.class)._embedded.appUsers;
        return convert(backendUsers);
    }

    private static List<FrontendUser> convert(List<BackendUser> backendUsers) {
        return backendUsers.stream().map(FrontendUser::new).collect(Collectors.toList());
    }

    private static String userserviceEndpoint() {
        return PropertyHolder.getProperty("userservice.endpoint");
    }

}

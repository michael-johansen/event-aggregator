package ciber.calendar.util;

import com.mashape.unirest.request.GetRequest;
import spark.Request;

import java.util.Map;

public class RouteParams {

    public static GetRequest withParams(GetRequest getRequest, Request origin) {
        for (Map.Entry<String, String[]> entry : origin.queryMap().toMap().entrySet()) {
            getRequest.queryString(entry.getKey(), entry.getValue()[0]);
        }
        return getRequest;
    }


}

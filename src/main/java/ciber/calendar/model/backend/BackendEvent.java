package ciber.calendar.model.backend;

import ciber.calendar.model.frontend.FrontendEvent;
import ciber.calendar.model.frontend.FrontendUser;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BackendEvent {

    @SerializedName("class")
    public final String clazz = "no.ciber.service.Event";

    public Integer id;
    public Date createdDate;
    public Date startDate;
    public Date endDate;
    public String name;
    public String description;

    public List<Integer> users;

    public Integer location;

    public BackendEvent(FrontendEvent frontendEvent) {
        this.id = frontendEvent.id;
        this.createdDate = frontendEvent.createdDate;
        this.startDate = frontendEvent.startDate;
        this.endDate = frontendEvent.startDate;
        this.name = frontendEvent.name;
        this.description = frontendEvent.description;
        this.users = frontendEvent.users.stream().map(user -> user.id).collect(Collectors.toList());
        this.location = frontendEvent.location != null ? frontendEvent.location.id : null;
    }

}

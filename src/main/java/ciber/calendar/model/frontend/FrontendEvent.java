package ciber.calendar.model.frontend;

import ciber.calendar.model.backend.BackendEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FrontendEvent {

    public Integer id;
    public Date createdDate;
    public Date startDate;
    public Date endDate;
    public String name;
    public String description;

    public List<FrontendUser> users = new ArrayList<>();

    public FrontendLocation location;

    public FrontendEvent(BackendEvent backendEvent) {
        this.id = backendEvent.id;
        this.createdDate = backendEvent.createdDate;
        this.startDate = backendEvent.startDate;
        this.endDate = backendEvent.endDate;
        this.name = backendEvent.name;
        this.description = backendEvent.description;
    }

}

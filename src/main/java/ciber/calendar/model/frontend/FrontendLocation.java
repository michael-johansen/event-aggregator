package ciber.calendar.model.frontend;

import ciber.calendar.model.backend.BackendLocation;

public class FrontendLocation {

    public Integer id;
    public String description;
    public String location;
    public Double latitude;
    public Double longitude;

    public FrontendLocation(BackendLocation backendLocation) {
        this.id = backendLocation.id;
        this.description = backendLocation.description;
        this.location = backendLocation.location;
        this.latitude = backendLocation.latitude;
        this.longitude = backendLocation.longitude;
    }

}


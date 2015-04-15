package ciber.calendar.model.frontend;


import ciber.calendar.model.backend.BackendUser;

public class FrontendUser {

    public Integer id;
    public String email;
    public String firstname;
    public String lastname;

    public FrontendUser(BackendUser backendUser) {
        this.id = backendUser.id;
        this.email = backendUser.email;
        this.firstname = backendUser.firstname;
        this.lastname = backendUser.lastname;
    }

}

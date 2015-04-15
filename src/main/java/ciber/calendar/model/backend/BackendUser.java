package ciber.calendar.model.backend;

public class BackendUser {

    public Integer id;
    public String email;
    public String firstname;
    public String lastname;

    public BackendUser(Integer id, String email, String firstname, String lastname) {
        this.id = id;
        this.email = email;
        this.firstname = firstname;
        this.lastname = lastname;
    }

}

package bloop.honk.Model;

import java.util.Date;

public class User extends Account {

    private Date dateCreated;

    public User(String username, String password) {
        super(username, password);
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }
}

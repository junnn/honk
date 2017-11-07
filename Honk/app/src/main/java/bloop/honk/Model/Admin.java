package bloop.honk.Model;

public class Admin extends Account {

    private String staffId;

    public Admin(String username, String password) {
        super(username, password);
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

}

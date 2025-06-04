package api;

public class NewRegisterSPojo {
    public String email;
    public String password;

    public NewRegisterSPojo(){

    }

    public NewRegisterSPojo(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}

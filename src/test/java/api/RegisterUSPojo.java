package api;

public class RegisterUSPojo {
    private String error;

    public RegisterUSPojo(){

    }
    public RegisterUSPojo(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }
}

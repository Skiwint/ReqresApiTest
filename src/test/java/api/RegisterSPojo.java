package api;

public class RegisterSPojo {
    public String id;
    public String token;

    public RegisterSPojo(){

    }

    public RegisterSPojo(String id, String token) {
        this.id = id;
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public String getToken() {
        return token;
    }
}

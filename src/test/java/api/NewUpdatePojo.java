package api;

public class NewUpdatePojo {
    public String name;
    public String job;

    public NewUpdatePojo(){

    }

    public NewUpdatePojo(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}

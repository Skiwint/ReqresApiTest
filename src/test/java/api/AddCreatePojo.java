package api;

public class AddCreatePojo {
    public String name;
    public String job;

    public AddCreatePojo(String name, String job) {
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

package builder;

import domain.Joblisting;

import java.util.Map;

public class JoblistingBuilder {
    private int id;
    private String jobTitle;
    private String company;
    private String sphere;
    private boolean active;

    public static JoblistingBuilder fromMap(Map<String, String> obj) {
        return new JoblistingBuilder()
                .id(Integer.parseInt(obj.get("id")))
                .jobTitle(obj.get("jobTitle"))
                .company(obj.get("company"))
                .sphere(obj.get("sphere"))
                .active(Boolean.parseBoolean(obj.get("active")));
    }

    public JoblistingBuilder id(int id) { this.id = id; return this; }
    public JoblistingBuilder jobTitle(String jobTitle) { this.jobTitle = jobTitle; return this; }
    public JoblistingBuilder company(String company) { this.company = company; return this; }
    public JoblistingBuilder sphere(String sphere) { this.sphere = sphere; return this; }
    public JoblistingBuilder active(boolean active) { this.active = active; return this; }

    public Joblisting build() {
        return new Joblisting(id, jobTitle, company, sphere, active);
    }
}
package sg.edu.nus.springcontroller.tutorial.model;

public class Owner {

    private Long id;
    private String name;

    public Owner() {
    }

    public Owner(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
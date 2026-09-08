package sg.iss.nus.spring.tutorial.jpa.demo;

import jakarta.persistence.*;

@Entity
public class Teaching {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "lecturer_id", nullable = false)
    private Lecturer2 lecturer;

    @ManyToOne
    @JoinColumn(name = "module_id", nullable = false)
    private Module2 module;

    public Teaching() {
    }

    public Teaching(Lecturer2 lecturer, Module2 module) {
        this.lecturer = lecturer;
        this.module = module;
    }

    public int getId() {
        return id;
    }

    public Lecturer2 getLecturer() {
        return lecturer;
    }

    public Module2 getModule() {
        return module;
    }

    public void setLecturer(Lecturer2 lecturer) {
        this.lecturer = lecturer;
    }

    public void setModule(Module2 module) {
        this.module = module;
    }
}

package sg.iss.nus.spring.tutorial.jpa.demo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Lecturer2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @OneToMany(mappedBy = "lecturer")
    private List<Teaching> teachings = new ArrayList<>();

    public Lecturer2() {
    }

    public Lecturer2(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Teaching> getTeachings() {
        return teachings;
    }

    public void setName(String name) {
        this.name = name;
    }
}
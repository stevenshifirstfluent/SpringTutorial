package sg.iss.nus.spring.tutorial.jpa.demo;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Module2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;

    private String name;

    @OneToMany(mappedBy = "module")
    private List<Teaching> teachings = new ArrayList<>();

    public Module2() {
    }

    public Module2(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public List<Teaching> getTeachings() {
        return teachings;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setName(String name) {
        this.name = name;
    }
}

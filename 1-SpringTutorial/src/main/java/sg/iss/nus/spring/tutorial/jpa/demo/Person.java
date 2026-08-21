package sg.iss.nus.spring.tutorial.jpa.demo;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;

@Entity
public class Person {
   @Id
   @GeneratedValue
   private int id;
   private String name;
   
   @ManyToMany
   @JoinTable(name="person_dog", 
      joinColumns = @JoinColumn(name = "person_id"), 
      inverseJoinColumns = @JoinColumn(name="dog_id"))
   private List<Dog> dogs;
   // Setters and Getters
}

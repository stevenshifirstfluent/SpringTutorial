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
public class Dog {
   @Id
   @GeneratedValue
   private int id;
   private String name;
   
   @ManyToMany(mappedBy="dogs")
   private List<Person> persons;
   // Setters and Getters
}


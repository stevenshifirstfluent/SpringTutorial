package sg.iss.nus.spring.tutorial.data.mysql;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userRepository.save(user);
    }
    
    @GetMapping("/createAUser")
    public String createAUser() {
    	User myUser = new User();
    	myUser.setLastName("Taylor");
    	myUser.setEmail("taylor@nus.edu.sg");
    	myUser.setUsername("taylor");
    	userRepository.save(myUser);
    	
        return "User created :- " + myUser.getLastName();        
    }
    
    @GetMapping("findByUsername/{username}")
    public String findUserByUsername(@PathVariable("username") String username) {
    	// using List is more flexible
        List<User> users = userRepository.findByUsername(username);
        if (!users.isEmpty()) {
          users.forEach(user -> { 
       	System.out.println(user.getUsername());
            	System.out.println(user.getLastName()); });          	    
          return "User(s) found and printed in the console.";
        } else {
            return "No users found with username: " + username;
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @RequestBody User updatedUser) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setUsername(updatedUser.getUsername());
                    user.setEmail(updatedUser.getEmail());
                    user.setLastName(updatedUser.getLastName());
                    User savedUser = userRepository.save(user);
                    return ResponseEntity.ok(savedUser);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable("id") Long id) {
        return userRepository.findById(id).map(user -> {
                    userRepository.delete(user);
                    return ResponseEntity.ok().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
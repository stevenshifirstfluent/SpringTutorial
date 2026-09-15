package sg.edu.nus.springcontroller.tutorial.service;

import org.springframework.stereotype.Service;

import sg.edu.nus.springcontroller.tutorial.model.Owner;

@Service
public class OwnerService {

    public Owner findOwner(Long ownerId) {

        // Dummy data for demonstration
        return new Owner(
                ownerId,
                "John Tan"
        );
    }
}
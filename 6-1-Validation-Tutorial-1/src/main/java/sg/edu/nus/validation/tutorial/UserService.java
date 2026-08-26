package sg.edu.nus.validation.tutorial;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;


@Service
public class UserService {


    private final Map<Long, User> users =
            new HashMap<>();


    private final AtomicLong sequence =
            new AtomicLong(0);


    public User save(
            User user) {


        long id =
                sequence.incrementAndGet();


        user.setId(id);


        users.put(
                id,
                user
        );


        return user;
    }


    public User update(
            Long id,
            User user) {


        user.setId(id);


        users.put(
                id,
                user
        );


        return user;
    }


    public User findById(
            Long id) {

        return users.get(id);
    }
}

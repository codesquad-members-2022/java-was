package repository;

import java.util.ArrayList;
import java.util.List;
import model.User;

public class UserRepository {

    private final static UserRepository userRepository = new UserRepository();

    private final List<User> users = new ArrayList<>();

    private UserRepository() {}

    public static UserRepository getInstance() {
        return userRepository;
    }

    public void save(User user) {
        users.add(user);
    }
}

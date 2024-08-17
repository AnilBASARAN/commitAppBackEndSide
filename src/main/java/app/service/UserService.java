package app.service;

import app.dao.UserDao;
import app.entities.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    private final UserDao userDao;

    public UserService(UserDao userDao) {
        this.userDao = userDao;
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User saveOneUser(User newUser) {
        // Ensure that the password is stored as provided, without encoding
        return userDao.save(newUser);
    }

    public User getOneUserById(Long userId) {
        return userDao.findById(userId).orElse(null);
    }

    public User updateOneUser(Long userId, User newUser) {
        Optional<User> user = userDao.findById(userId);
        if (user.isPresent()) {
            User foundUser = user.get();
            foundUser.setUserName(newUser.getUserName());
            // Ensure the password is updated directly without encoding
            foundUser.setPassword(newUser.getPassword());
            userDao.save(foundUser);
            return foundUser;
        } else {
            return null;
        }
    }

    public void deleteById(Long userId) {
        userDao.deleteById(userId);
    }

    public User getOneUserByUserName(String userName) {
        return userDao.findByUserName(userName);
    }
}

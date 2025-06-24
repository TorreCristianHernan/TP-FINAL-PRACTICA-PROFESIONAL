package hallytech.phoenix.service.impl;

import hallytech.phoenix.entity.User;
import hallytech.phoenix.repository.UserRepository;
import hallytech.phoenix.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder encoder;

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmailAndState(email, Constant.State.ACTIVE.toString());
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = getUserByEmail(email);
        return user.map(UserInfoDetails::new).orElseThrow(() -> new UsernameNotFoundException("User not found by " + email));
    }

    public List<User> listUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUser(Long id) {
        return userRepository.findById(id);
    }

    public User addUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setState(Constant.State.ACTIVE.name());
        return userRepository.save(user);
//        return "User Added Successfully";
    }

    public User updateUser(User user) {
        Optional<User> userDB = getUser(user.getId());
        if (userDB.isPresent()){
//            User userUpdate = userDB.get();
//            userUpdate.setRoles(user.getRoles());
//            userUpdate.setRoles(user.getFirstName());
//            userUpdate.setRoles(user.getLastName());
//            userUpdate.setRoles(user.getType());
//            userUpdate.setState(user.getPhone());
//            userUpdate.setState(user.getState());
            return userRepository.save(user);
        }
        return null;
    }

    public User deleteUser(Long id) {
        Optional<User> userDB = getUser(id);
        if (userDB.isPresent()){
            User userUpdate = userDB.get();
            userUpdate.setState(Constant.State.DELETE.name());
            return userRepository.save(userUpdate);
        }
        return null;
    }
}

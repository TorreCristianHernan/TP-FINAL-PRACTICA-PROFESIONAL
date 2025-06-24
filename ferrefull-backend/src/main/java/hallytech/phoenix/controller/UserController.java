package hallytech.phoenix.controller;

import hallytech.phoenix.entity.request.AuthRequest;
import hallytech.phoenix.entity.User;
import hallytech.phoenix.service.impl.JwtService;
import hallytech.phoenix.service.impl.UserServiceImpl;
import hallytech.phoenix.util.Util;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    @Autowired
    private UserServiceImpl userService;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    /*@GetMapping("/welcome")
    public String welcome() {
        return "Welcome this endpoint is not secure";
    }*/

    @GetMapping("/user")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<User>> listUsers(){
        return ResponseEntity.ok(userService.listUsers());
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<User> getUser(@PathVariable("id") Long id){
        return userService.getUser(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/user")
    @PreAuthorize("permitAll()")
    public ResponseEntity<User> addNewUser(@Valid @RequestBody User user, BindingResult result) {
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.addUser(user));
    }

    @PutMapping(value = "/user/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id, @Valid @RequestBody User user, BindingResult result){
        if (result.hasErrors()){
            throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, Util.formatMessage(result));
        }
        user.setId(id);
        User userDB = userService.updateUser(user);
        if (Objects.isNull(userDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDB);
    }

    @DeleteMapping(value = "/user/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<User> updateUser(@PathVariable("id") Long id){
        User userDB = userService.deleteUser(id);
        if (Objects.isNull(userDB)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userDB);
    }

    //@PreAuthorize("hasRole('one') and hasRole('two')")
    //@PreAuthorize("hasAuthority('ADMIN')")

    /*@GetMapping("/user/userProfile")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userProfile(Authentication authentication) {
        return "Welcome to User Profile ::: " + authentication.getAuthorities();
    }*/

    /*@GetMapping("/admin/adminProfile")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String adminProfile() {
        return "Welcome to Admin Profile";
    }*/

    @PostMapping("/auth")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
//            return jwtService.generateToken(authRequest.getEmail());
            Optional<User> user = userService.getUserByEmail(authRequest.getEmail());
            if (user.isPresent()){
                return jwtService.generateToken(user.get());
            }
        }
        throw new UsernameNotFoundException("invalid user request !");
    }
}

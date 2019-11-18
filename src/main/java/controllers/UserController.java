package controllers;

import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.Optional;

@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManagerFactory emf;
    @RequestMapping("/users")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    @RequestMapping("users/add")
    public ResponseEntity addUser(@RequestBody User user){
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        user.setBalance(0);
        em.persist(user);
        em.getTransaction().commit();
        em.close();
        return ResponseEntity.ok(HttpStatus.OK);
    }
    @RequestMapping("users/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id){
        Optional<User> res = userRepository.findById(id);
        return ResponseEntity.of(res);
    }

}

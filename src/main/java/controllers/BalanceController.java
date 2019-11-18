package controllers;

import entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import repositories.UserRepository;
import synchronizers.UserLocks;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@RestController
public class BalanceController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private EntityManagerFactory emf;
    @Autowired
    private UserLocks userSynchronizer;


    @RequestMapping("users/{id}/addbalance/{amount}")
    public ResponseEntity addBalance(@PathVariable Long id, @PathVariable int amount) {
        User user = userRepository.getOne(id);
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();
        userSynchronizer.lockUser(id);
        int balance = user.getBalance() + amount;
        user.setBalance(balance);
        userRepository.save(user);
        em.getTransaction().commit();
        em.close();
        userSynchronizer.unlockUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping("users/transfer")
    public ResponseEntity transferBalance(@RequestParam long fromId, @RequestParam long toId, @RequestParam int amount) {
        if (amount <= 0) return new ResponseEntity("amount should be positive", HttpStatus.BAD_REQUEST);
        ResponseEntity status;
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            userSynchronizer.lockUser(fromId);
            User from = userRepository.getOne(fromId);
            int balance = from.getBalance();
            if (balance < amount) {
                throw new RuntimeException("insufficient balance");
            }
            userSynchronizer.lockUser(toId);
            User to = userRepository.getOne(toId);
            from.setBalance(balance - amount);
            to.setBalance(to.getBalance() + amount);
            userRepository.save(from);
            userRepository.save(to);
            em.getTransaction().commit();
            status = ResponseEntity.ok(HttpStatus.OK);
        }catch ( RuntimeException e) {
            em.getTransaction().rollback();
            status = new ResponseEntity(e.getMessage(), HttpStatus.PRECONDITION_FAILED);
        } finally{
            em.close();
            userSynchronizer.unlockUser(fromId);
            userSynchronizer.unlockUser(toId);
        }
        return status;
    }

}

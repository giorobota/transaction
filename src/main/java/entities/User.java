package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jdk.nashorn.internal.ir.annotations.Ignore;

import javax.persistence.*;
import java.util.concurrent.locks.ReentrantLock;

@Entity
@Table(name = "users")
@SecondaryTable(name = "balances")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String firstName;
    private String lastName;
    @Column(table = "balances")
    private int balance;
    @JsonIgnore
    private ReentrantLock mutex;

    public ReentrantLock getMutex() {
        return mutex;
    }

    public void setMutex(ReentrantLock mutex) {
        this.mutex = mutex;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


}

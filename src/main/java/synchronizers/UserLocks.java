package synchronizers;

import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.locks.ReentrantLock;

@Configuration
public class UserLocks {
    //lock for users, whose transaction is ongoing
    private Map<Long, ReentrantLock> locks;
    private ReentrantLock mapLock;
    private ReentrantLock transactionLock;
    public UserLocks() {
        locks = new TreeMap<Long, ReentrantLock>();
        mapLock = new ReentrantLock();
        transactionLock = new ReentrantLock();
    }

    public void lockUser(long userId) {
        mapLock.lock();
        if (locks.containsKey(userId)) {
            ReentrantLock lock = locks.get(userId);
            mapLock.unlock();
            lock.lock();
        } else {
            ReentrantLock lock = new ReentrantLock();
            locks.put(userId, lock);
            mapLock.unlock();
            lock.lock();
        }
    }
//    public void lockTwoUsers(long user1, long user2){
//        mapLock.lock();
//        ReentrantLock lock = new ReentrantLock();
//        locks.put(user1, lock);
//        locks.put(user2, lock);
//        mapLock.unlock();
//        lock.lock();
//    }
//    public void unlockTwoUsers(long user1, long user2){
//
//    }

    public void unlockUser(long userId) {
        mapLock.lock();
        if (locks.containsKey(userId)) {
            ReentrantLock lock = locks.get(userId);
            lock.unlock();
            locks.remove(userId);
        }
        mapLock.unlock();
    }
}

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

    public UserLocks() {
        locks = new TreeMap<Long, ReentrantLock>();
        mapLock = new ReentrantLock();
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

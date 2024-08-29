package NotificationService.repository.redis_db_repo;

import NotificationService.model.PersonalNotification;
import NotificationService.response.ResponseMessage;
import jakarta.annotation.PostConstruct;
import org.apache.kafka.common.message.LeaderAndIsrRequestData;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Repository;

import javax.swing.plaf.PanelUI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
@EnableCaching
@CacheConfig(cacheManager = "NotificationCacheManager", cacheNames = "currently_active_cache")
public class ActiveUserRepository {

    @CachePut(key = "'ACTIVE::USERS::SET'")
    public Set<Long> updateActiveUsersSet(Set<Long> userIds) {
        return userIds;
    }

    @Cacheable(key = "'ACTIVE::USERS::SET'")
    public Set<Long> getActiveUsersSet() {
        return new HashSet<Long>();
    }

    @CachePut(key = "'USER::NOTIFICATION::' + #username")
    public Integer updateUserNotificationCount(String username, Integer count) {
        return count;
    }

    @Cacheable(key = "'USER::NOTIFICATION::' + #username")
    public Integer getUserNotificationCount(String username) {
        return 0;
    }

    @CachePut(key = "'USER::UNSEEN::' + #username")
    public List<ResponseMessage> setUnseenNotifications(String username, List<ResponseMessage> unseenMsg) {
        return unseenMsg;
    }

    @Cacheable(key = "'USER::UNSEEN::' + #username")
    public List<ResponseMessage> getUnseenNotifications(String username) {
        return new ArrayList<>();
    }

    @CacheEvict(allEntries = true)
    public void clearCache(){}

    @PostConstruct
    public void init() {
        clearCache();
    }
}

package NotificationService.repository.redis_db_repo;

import jakarta.annotation.PostConstruct;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Repository;

import javax.swing.plaf.PanelUI;

@Repository
@EnableCaching
@CacheConfig(cacheManager = "NotificationCacheManager", cacheNames = "currently_active_cache")
public class ActiveUserRepository {

    @CachePut(key = "'ACTIVE::USERS'")
    public Long updateActiveUserCount(Long activeUsers) {
        return activeUsers;
    }

    @Cacheable(key = "'ACTIVE::USERS'")
    public Long getActiveUsersCount() {
        return 0L;
    }

    @Cacheable(key = "'ISFIRSTUSER'")
    public Boolean isFirstUser() {
        return true;
    }

    @CachePut(key = "'ISFIRSTUSER'")
    public Boolean isFirstUser(Boolean isFirst) {
        return isFirst;
    }
}

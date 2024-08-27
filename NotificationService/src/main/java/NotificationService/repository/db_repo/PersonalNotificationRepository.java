package NotificationService.repository.db_repo;

import NotificationService.model.PersonalNotification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalNotificationRepository extends JpaRepository<PersonalNotification, Long> {
    @Transactional
    @Modifying
    @Query(nativeQuery = true, value = """
            UPDATE personal_notification_mapping
            SET user_id = ?2
            WHERE notification_id = ?1
            """)
    void updateNotificationRow(Long notificationId, Long userId);
}

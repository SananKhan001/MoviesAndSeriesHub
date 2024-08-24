package NotificationService.repository;

import NotificationService.model.GlobalNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalNotificationRepository extends JpaRepository<GlobalNotification, Long> {
}

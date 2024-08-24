package NotificationService.repository;

import NotificationService.model.PersonalNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonalNotificationRepository extends JpaRepository<PersonalNotification, Long> {
}

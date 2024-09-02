package NotificationService.repository.db_repo;

import NotificationService.model.PersonalNotification;
import NotificationService.response.ResponseMessage;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonalNotificationRepository extends JpaRepository<PersonalNotification, Long> {
}

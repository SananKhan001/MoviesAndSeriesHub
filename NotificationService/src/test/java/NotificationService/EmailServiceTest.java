package NotificationService;

import NotificationService.service.EmailService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.io.IOException;

@SpringBootTest
public class EmailServiceTest {

    @SpyBean
    private EmailService emailService;

    @Test
    public void sendEmailTest() throws MessagingException, IOException {
        emailService.sendEmail("Final Message !!!", "Hello Sanan Ahmad Khan,", """
                Beneath the ancient oak, whispers of forgotten tales drifted on the breeze. 
                Sunlight dappled the ground, creating a mosaic of light and shadow. 
                Somewhere, a brook babbled softly, its melody a gentle counterpoint to the rustling leaves. 
                Nature's serenity enveloped all, promising solace in its timeless embrace.
                """, "sanan3946@gmail.com");
        Assertions.assertTrue(true);
    }

}

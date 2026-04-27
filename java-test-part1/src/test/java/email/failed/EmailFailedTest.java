package email.failed;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import net.bugreaper.modules.email.Email;
import net.bugreaper.modules.email.EmailMailpit;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;


@Tag("failed")
@Epic("[email-failed] Expected failed email tests")
class EmailFailedTest {

    Email email = Email.getInstance();
    EmailMailpit mailpit = EmailMailpit.getInstance();

    @BeforeEach
    void clean(){
        mailpit.cleanAllMailboxes();
        mailpit.unsetChaos();
    }

    @AfterEach
    void rollback(){
        mailpit.unsetChaos();
    }


    @Test
    @Description("""
            Set Recipient chaos and got error on send""")
    void checkChaosRecipient() {
        mailpit.setChaosRecipient(500);
        email.sendEmail("your_email@gmail.com", "my_email@gmail.com", "my subject_old", "Hello_old");

    }

    @Test
    @Description("""
            Set Authentication chaos and got error on send""")
    void checkChaosAuthentication() {
        mailpit.setChaosAuthentication(401);
        email.sendEmail("your_email@gmail.com", "my_email@gmail.com", "my subject_old", "Hello_old");

    }

    @Test
    @Description("""
            Set Sender chaos and got error on send""")
    void checkChaosSender() {
        mailpit.setChaosSender(404);
        email.sendEmail("your_email@gmail.com", "my_email@gmail.com", "my subject_old", "Hello_old");

    }

    @Test
    @Description("""
           seeMailboxIsNotEmpty catch""")
    void checkAssertionTimeoutMailboxIsNotEmptyCatch() {

        mailpit.seeMailboxIsNotEmpty("my_email@gmail.com");

    }

    @Test
    @Description("""
           seeAllEmailsCountExactly catch""")
    void checkAssertionTimeoutAllEmailsCountCatch() {
        email.sendEmail("your_email@gmail.com", "my_email@gmail.com", "my subject_old", "Hello_old");
        mailpit.seeAllEmailsCountExactly(0);

    }

    @Test
    @Description("""
            Failed assert on FROM check""")
    void checkFromAssertionCatch() {
        email.sendEmail("your_email@gmail.com", "my_email@gmail.com", "my subject_old", "Hello_old");

        mailpit.getLastEmailInMailbox("my_email@gmail.com")
                .seeEmailContainsFrom("wrong@gmail.com");

    }

    @Test
    @Description("""
            Failed assert on Subject Contains check""")
    void checkSubjectContainsAssertionCatch() {
        email.sendEmail("your_email@gmail.com", "my_email@gmail.com", "my subject_old", "Hello_old");

        mailpit.getLastEmailInMailbox("my_email@gmail.com")
                .seeEmailSubjectContains("not_right");

    }

    @Test
    @Description("""
            Failed assert on FROM check""")
    void checkCcAssertionCatch() {
        String recipient = "my_email@gmail.com";

        email.
                build( "my subject", "Hello world")
                .setFrom("your_email@gmail.com")
                .addTo(recipient)
                .addTo("my_email2@gmail.com")
                .addCc("cc1@gmail.com")
                .addCc("cc2@gmail.com")
                .addCc("cc3@gmail.com")
                .send();

        mailpit.getLastEmailInMailbox(recipient)
                .seeEmailContainsTo(recipient)
                .seeEmailCcCountExactly(3)
                .seeEmailContainsCc("cc4@gmail.com");
    }

}

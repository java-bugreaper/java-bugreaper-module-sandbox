package email.positive;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import net.bugreaper.modules.email.Email;
import net.bugreaper.modules.email.EmailMailpit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

@Tag("email")
@Epic("[email] Tests as is")
class EmailTest {

    Email email = Email.getInstance();
    EmailMailpit mailpit = EmailMailpit.getInstance();

    @BeforeEach
    void clean(){
        mailpit.cleanAllMailboxes();
    }


    @Test
    @Description("""
            Send simple email and check""")
    void checkSimple() {
        String recipient = "my_email@gmail.com";

        email.sendEmail("your_email@gmail.com", recipient, "my subject_old", "Hello_old");
        email.sendEmail("your_email_new@gmail.com", recipient, "my subject new", "Hello_new");

        mailpit.seeEmailsInMailboxCountExactly(recipient, 2);


        mailpit.getLatestEmailInMailbox(recipient)
                .seeEmailContainsTo(recipient)
                .seeEmailContainsFrom("your_email_new@gmail.com")
                .seeEmailToCountExactly(1)
                .seeEmailCcCountExactly(0)
                .seeEmailReplyToCountExactly(0)
                .seeEmailBccCountExactly(0)
                .seeEmailAttachmentsCountExactly(0);
    }

    @Test
    @Description("""
            Send and check basic counts""")
    void sendGetCountsAndCleanTest() {

        String recipient1 = "my_email@gmail.com";
        String recipient2 = "second_recipient@gmail.com";

        mailpit.seeAllEmailsCountExactly(0);
        mailpit.seeEmailsInMailboxCountExactly(recipient1, 0);

        email.sendEmail("your_email@gmail.com", recipient1, "my subject", "Hello");
        email.sendEmail("your_email@gmail.com", recipient2, "my subject21", "Hello21");
        email.sendEmail("your_email2@gmail.com", recipient2, "my subject22", "Hello22");

        mailpit.seeAllEmailsCountExactly(3);
        mailpit.seeEmailsInMailboxCountExactly(recipient1, 1);
        mailpit.seeEmailsInMailboxCountExactly(recipient2, 2);

        mailpit.cleanMailbox(recipient2);

        mailpit.seeAllEmailsCountExactly(1);

        mailpit.seeMailboxIsNotEmpty(recipient1);
        mailpit.seeMailboxIsEmpty(recipient2);

        mailpit.seeEmailsInMailboxCountExactly(recipient1, 1);
        mailpit.seeEmailsInMailboxCountExactly(recipient2, 0);


        mailpit.cleanAllMailboxes();
        mailpit.seeAllEmailsCountExactly(0);
    }


    @Test
    @Description("""
            Send with builder and check data""")
    void sendBuildBuilderTest() {

        String recipient = "my_email@gmail.com";

        email.
                build( "my subject", "Hello world")
                .addAttach("text1.txt", "/files/text1.txt")
                .addAttach("text2.csv", "/files/text2.csv")
                .setFrom("your_email@gmail.com")
                .addTo(recipient)
                .addTo("my_email2@gmail.com")
                .addCc("cc1@gmail.com")
                .addCc("cc2@gmail.com")
                .addCc("cc3@gmail.com")
                .addBcc("bcc1@gmail.com")
                .addBcc("bcc2@gmail.com")
                .addBcc("bcc3@gmail.com")
                .addBcc("bcc4@gmail.com")
                .addReplyTo("rep1@gmail.com")
                .addTags("env")
                .addTags("qa")
                .addHeader("test-head1", "value_1")
                .addHeader("test-head2", "value_2")
                .send();

        mailpit.getLatestEmailInMailbox(recipient)
                .seeEmailContainsTo(recipient)
                .seeEmailSubjectContains("my")
                .seeEmailBodyContains("world")
                .seeEmailSubjectEqual("my subject")
                .seeEmailBodyEqual("Hello world")
                .seeEmailContainsTo("my_email2@gmail.com")
                .seeEmailContainsFrom("your_email@gmail.com")
                .seeEmailContainsCc("cc2@gmail.com")
                .seeEmailContainsBcc("bcc3@gmail.com")
                .seeEmailContainsReplyTo("rep1@gmail.com")
                .seeEmailToCountExactly(2)
                .seeEmailCcCountExactly(3)
                .seeEmailReplyToCountExactly(1)
                .seeEmailBccCountExactly(4)
                .seeEmailAttachmentsCountExactly(2);
    }

}

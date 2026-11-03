package artemis.failed;

import artemis.TestArtemisSetup;
import io.qameta.allure.Feature;
import net.bugreaper.modules.artemis.exceptions.ArtemisHelperException;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Test;

import static net.bugreaper.core.allurereporter.AllureReporter.reporter;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Feature("Failed Asserts")
class ArtemisFailedTests extends TestArtemisSetup {

    @Test
    void testNotEmptyQueue() {
        artemis.pushToQueue(TEST_QUEUE, "test_text");

        reporter("With specific await");
        assertThrows(AssertionError.class, () ->
                artemis.withAwaitMs(200).seeQueueIsEmpty(TEST_QUEUE));

        reporter("With global await");
        assertThrows(AssertionError.class, () ->
                artemis.seeMessagesCountInQueueExactly(TEST_QUEUE, 2));
    }

    @Test
    void testEmptyQueue() {

        assertThrows(AssertionError.class, () ->
                artemis.withAwaitMs(400).seeQueueIsNotEmpty(TEST_QUEUE));

        assertThrows(AssertionError.class, () ->
                artemis.seeMessagesCountInQueueExactly(TEST_QUEUE, 1));
    }

    @Test
    void testConsumeEmptyQueue() {

        assertThrows(ConditionTimeoutException.class, () ->
                artemis.withAwaitMs(500).grabMessagesFromQueue(TEST_QUEUE));
    }

    @Test
    void testNotExistingQueue() {

        assertThrows(ArtemisHelperException.class, () ->
                artemis.seeQueueIsNotEmpty("not_exist"));

        assertThrows(ConditionTimeoutException.class, () ->
                artemis.grabMessagesFromQueue("not_exist"));
    }

}

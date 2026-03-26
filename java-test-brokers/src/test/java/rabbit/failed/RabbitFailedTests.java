package rabbit.failed;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import rabbit.TestRabbitSetup;

import static net.bugreaper.core.allurereporter.AllureReporter.reporter;
import static org.junit.jupiter.api.Assertions.assertThrows;


@Feature("[rabbit] Failed tests")
class RabbitFailedTests extends TestRabbitSetup {
    @Test
    void testTopicNotEmptyFailed() {

        reporter("With specific await");
        assertThrows(AssertionError.class, () ->
                rabbit.withAwaitMs(400).seeQueueIsNotEmpty(TEST_QUEUE)
        );

        reporter("With global await");
        assertThrows(AssertionError.class, () ->
                rabbit.seeQueueIsNotEmpty(TEST_QUEUE)
        );
    }

    @Test
    void testCountMessagesFailed() {

        rabbit.pushToQueue(TEST_QUEUE, "test_text1");

        reporter("With specific await");
        assertThrows(AssertionError.class, () ->
                rabbit.withAwaitMs(440).seeQueueMessageCountIsExactly(TEST_QUEUE, 2)
        );

        reporter("With global await");
        assertThrows(AssertionError.class, () ->
                rabbit.seeQueueMessageCountIsExactly(TEST_QUEUE, 3)
        );
    }

}

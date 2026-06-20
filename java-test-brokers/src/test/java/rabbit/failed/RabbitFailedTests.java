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

    @Test
    void grabMessagesFromQueueTestFailed() {

        final String lastMessage = """
                {
                  "status": 112,
                  "statusName": "Something3",
                  "name": "AlexNew"
                }""";

        rabbit.declareQueue(TEST_QUEUE);
        rabbit.pushToQueue(TEST_QUEUE, lastMessage);

        assertThrows(AssertionError.class, () ->
        rabbit.grabMessagesFromQueue(TEST_QUEUE)
                .seeListAnyContainsExtendedJson("""
                        {
                        "status:>": 112,
                        "statusName:regex": "SomeL.*"
                        }""")
        );
    }


}

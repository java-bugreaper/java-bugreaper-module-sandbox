package kafka.failed;

import io.qameta.allure.Feature;
import kafka.TestKafkaSetup;
import org.awaitility.core.ConditionTimeoutException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Feature("[kafka] Failed tests")
class KafkaFailedTests extends TestKafkaSetup {


    @Test
    void testTopicNotEmptyFailed() {

        assertThrows(AssertionError.class, () ->
                kafka.seeTopicIsNotEmpty(TOPIC)
        );

    }

    @Test
    void testCountMessagesFailed() {

        kafka.sendToTopic(TOPIC, "test_text1");

        assertThrows(AssertionError.class, () ->
                kafka.seeCountMessagesInTopicExactly(TOPIC, 3)
        );

    }

    @Test
    void testJsonContainsFailed() {

        kafka.sendToTopic(TOPIC, """
                {
                    "id": 1
                }""");

        kafka.sendToTopic(TOPIC, """
                {
                    "ids": 2
                }""");

        kafka.sendToTopic(TOPIC, """
                {
                    "id": 3
                }""");

        assertThrows(AssertionError.class, () ->
                kafka.seeMessagesContainJson(TOPIC,
                        """
                                {
                                    "id": 2
                                }""")
        );
    }

    @Test
    void testGrabFromEmptyTopicFailed() {

        assertThrows(ConditionTimeoutException.class, () ->
                kafka.grabMessagesFromTopic(TOPIC)
        );
    }

}

package kafka.positive;

import io.qameta.allure.Feature;
import kafka.TestKafkaSetup;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static net.bugreaper.core.filereaders.FileReader.readTextFromFile;

@Feature("[kafka] Tests as is")
class KafkaTests extends TestKafkaSetup {


    @Test
    void testCountMessagesV1() {

        kafka.sendToTopic(TOPIC, "test_text");
        kafka.sendToTopic(TOPIC, "test_text2");
        kafka.grabMessagesFromTopic(TOPIC)
                .seeListHasExactlyCount(2);
    }

    @Test
    void testCountMessagesV2() {
        kafka.sendToTopic(TOPIC_WITH_PART, "test_text");
        kafka.seeTopicIsNotEmpty(TOPIC_WITH_PART);

        kafka.purgeTopic(TOPIC_WITH_PART);
        kafka.seeTopicIsEmpty(TOPIC_WITH_PART);

        kafka.sendToTopic(TOPIC_WITH_PART, "test_text2");
        kafka.sendToTopic(TOPIC_WITH_PART, "test_text3");
        kafka.seeMessagesCountInTopicExactly(TOPIC_WITH_PART, 2);
    }

    @Test
    void testCountAndGrabbedMessagesSaved() {

        kafka.seeTopicIsEmpty(TOPIC);
        kafka.sendToTopic(TOPIC, "test_text1");
        kafka.sendToTopic(TOPIC, "test_text2");
        kafka.seeTopicIsNotEmpty(TOPIC);
        kafka.seeMessagesCountInTopicExactly(TOPIC, 2);

        kafka.grabMessagesFromTopic(TOPIC)
                .seeListHasExactlyCount(2);

        kafka.seeTopicIsNotEmpty(TOPIC);
    }


    @Test
    void testCountMessagesLeftInTopic() {

        kafka.sendToTopic(TOPIC, "test_text1");
        kafka.seeMessagesCountInTopicExactly(TOPIC, 1);
        kafka.sendToTopic(TOPIC, "test_text2");
        kafka.seeMessagesCountInTopicExactly(TOPIC, 2);
    }

    @Test
    void testCountMessagesEmpty() {

        kafka.seeTopicIsEmpty(TOPIC);
    }

    @Test
    void testLongCleanTopicEmpty() {


        for (int i = 0; i < 10; i++) {
            kafka.sendToTopic(TOPIC, "test_text1");
        }

        kafka.purgeTopic(TOPIC);
        kafka.seeTopicIsEmpty(TOPIC);
    }

    @Test
    void testPushAndGrabTopicForCustomAsserts() {

        kafka.sendToTopic(TOPIC, "test_text1");
        kafka.sendToTopic(TOPIC, "test_text2");
        kafka.grabMessagesFromTopic(TOPIC)
                .seeListAnyEquals("test_text2")
                .seeListAnyContains("text1");
    }

    @Test
    void testMessageEqualText() {

        kafka.sendToTopic(TOPIC, "test_text1");
        kafka.sendToTopic(TOPIC, "test_text2");
        kafka.sendToTopic(TOPIC, "test_text3");
        kafka.seeMessagesHaveEqualText(TOPIC, "test_text2");
    }

    @Test
    void testMessageContainsText() {

        kafka.sendToTopic(TOPIC, "test_text1");
        kafka.sendToTopic(TOPIC, "test_text2");
        kafka.seeMessagesContainText(TOPIC, "test_text");
    }


    @Test
    void testMessageEqualJson() {

        final String JSON = """
                {"id":1, "name": "Alex"}""";


        kafka.sendToTopic(TOPIC, JSON);
        kafka.sendToTopic(TOPIC, "test_text2");
        kafka.seeMessagesHaveEqualJson(TOPIC, JSON);
    }

    @Test
    void testMessageContainsJson() {

        kafka.sendToTopic(TOPIC, "test_text1");
        kafka.sendToTopic(TOPIC, """
                {"id":1, "name": "Alex"}""");

        kafka.seeMessagesContainJson(TOPIC, """
                {"name": "Alex"}""");
    }

    @Test
    void testMessageIsJson() {

        kafka.sendToTopic(TOPIC, "test_text1");
        kafka.sendToTopic(TOPIC, """
                {"id":1, "name": "Alex"}""");

        kafka.grabMessagesFromTopic(TOPIC)
                .seeListAnyJsonType();
    }

    @Test
    void testMessageJsonFromFile() {

        var path = "kafkadata/test1.json";

        kafka.sendToTopic(TOPIC, readTextFromFile(path));

        kafka.grabMessagesFromTopic(TOPIC)
                .seeListAnyEqualsJson(Path.of(path))
                .seeListAnyContainsJson(Path.of(path));
    }

    @Test
    void testMessageJsonSchemaCheck() {


        kafka.sendToTopic(TOPIC,
                """
                        {
                            "id": 3
                        }""");

        kafka.grabMessagesFromTopic(TOPIC)
                .seeListAnyJsonMatchSchema("""
                        {
                          "type": "object",
                          "properties": {
                            "id": {
                              "type": "integer"
                            }
                          },
                          "additionalProperties": false
                        }""");
    }

    @Test
    void testMessageJsonSchemaCheckFromFile() {


        kafka.sendToTopic(TOPIC,
                """
                        {
                            "id": 3
                        }""");

        kafka.grabMessagesFromTopic(TOPIC)
                .seeListAnyJsonMatchSchema(Path.of("kafkadata/schema_1.json"));
    }

    @Test
    void testCountInEmptyTopic() {

        kafka.seeMessagesCountInTopicExactly(TOPIC, 0);
    }

    @Test
    void grabMessagesWithKeyTest() {


        kafka.sendToTopicWithKey(TOPIC, "key-1", "message_1");
        kafka.sendToTopicWithKey(TOPIC, "key-2", "message_2");
        kafka.sendToTopicWithKey(TOPIC, "key-1", "message_3");

        kafka.seeMessagesCountInTopicExactly(TOPIC, 3);

        kafka.grabMessagesFromTopic(TOPIC, "key-1")
                .seeListHasExactlyCount(2)
                .seeListAnyEquals("message_1")
                .seeListAnyEquals("message_3");

        kafka.grabMessagesFromTopic(TOPIC)
                .seeListHasExactlyCount(3)
                .seeListAnyEquals("message_1")
                .seeListAnyEquals("message_2")
                .seeListAnyEquals("message_3");


    }



}

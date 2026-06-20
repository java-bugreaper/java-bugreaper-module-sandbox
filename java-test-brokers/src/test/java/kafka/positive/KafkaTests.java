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
        kafka.seeCountMessagesInTopicExactly(TOPIC_WITH_PART, 2);
    }

    @Test
    void testCountAndGrabbedMessagesSaved() {

        kafka.seeTopicIsEmpty(TOPIC);
        kafka.sendToTopic(TOPIC, "test_text1");
        kafka.sendToTopic(TOPIC, "test_text2");
        kafka.seeTopicIsNotEmpty(TOPIC);
        kafka.seeCountMessagesInTopicExactly(TOPIC, 2);

        kafka.grabMessagesFromTopic(TOPIC)
                .seeListHasExactlyCount(2);

        kafka.seeTopicIsNotEmpty(TOPIC);
    }


    @Test
    void testCountMessagesLeftInTopic() {

        kafka.sendToTopic(TOPIC, "test_text1");
        kafka.seeCountMessagesInTopicExactly(TOPIC, 1);
        kafka.sendToTopic(TOPIC, "test_text2");
        kafka.seeCountMessagesInTopicExactly(TOPIC, 2);
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

        kafka.seeCountMessagesInTopicExactly(TOPIC, 0);
    }


}

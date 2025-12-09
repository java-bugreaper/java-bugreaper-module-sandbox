package artemis.positive;

import artemis.TestArtemisSetup;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static io.bugreaper.core.filereaders.FileReader.readTextFromFile;


class ArtemisTests extends TestArtemisSetup {

    @Test
    void testPushToQueue() {
        String queue = "new_topic_2";
        artemis.pushToQueue(queue, "test_text");
    }


    @Test
    void testGetMessagesCount() {
        artemis.pushToQueue(TEST_QUEUE, "test_text");
        artemis.pushToQueue(TEST_QUEUE, "test_text");

        artemis.seeCountMessagesInQueueExactly(TEST_QUEUE, 2);
    }


    @Test
    void testCountMessagesV1() {

        artemis.pushToQueue(TEST_QUEUE, "test_text1");
        artemis.pushToQueue(TEST_QUEUE, "test_text2");
        artemis.grabMessagesFromQueue(TEST_QUEUE)
                .seeListHasExactlyCount(2);
    }

    @Test
    void testCountMessagesV2() {
        artemis.pushToQueue(TEST_QUEUE, "test");
        artemis.cleanQueue(TEST_QUEUE);
        artemis.pushToQueue(TEST_QUEUE, "test_text1");
        artemis.pushToQueue(TEST_QUEUE, "test_text2");
        artemis.seeCountMessagesInQueueExactly(TEST_QUEUE, 2);
    }

    @Test
    void testCountMessagesSaved() {

        artemis.seeQueueIsEmpty(TEST_QUEUE);
        artemis.pushToQueue(TEST_QUEUE, "test_text1");
        artemis.pushToQueue(TEST_QUEUE, "test_text2");
        artemis.seeQueueIsNotEmpty(TEST_QUEUE);
        artemis.seeCountMessagesInQueueExactly(TEST_QUEUE, 2);

        artemis.grabMessagesFromQueue(TEST_QUEUE)
                .seeListHasExactlyCount(2);

        artemis.seeQueueIsEmpty(TEST_QUEUE);
    }


    @Test
    void testCountMessagesLeftFromQueue() {

        artemis.pushToQueue(TEST_QUEUE, "test_text1");
        artemis.seeCountMessagesInQueueExactly(TEST_QUEUE, 1);
        artemis.pushToQueue(TEST_QUEUE, "test_text2");
        artemis.seeCountMessagesInQueueExactly(TEST_QUEUE, 2);
    }

    @Test
    void testCountMessagesEmpty() {

        artemis.seeQueueIsEmpty(TEST_QUEUE);
    }

    @Test
    void testLongCleanQueueEmpty() {


        for (int i = 0; i < 10; i++) {
            artemis.pushToQueue(TEST_QUEUE, "test_text1");
        }

        artemis.cleanQueue(TEST_QUEUE);
        artemis.seeQueueIsEmpty(TEST_QUEUE);
    }

    @Test
    void testPushAndGrabQueueForCustomAsserts() {

        artemis.pushToQueue(TEST_QUEUE, "test_text1");
        artemis.pushToQueue(TEST_QUEUE, "test_text2");
        artemis.grabMessagesFromQueue(TEST_QUEUE)
                .seeListAnyEquals("test_text2")
                .seeListAnyContains("text1");
    }

    @Test
    void testMessageEqualText() {

        artemis.pushToQueue(TEST_QUEUE, "test_text1");
        artemis.pushToQueue(TEST_QUEUE, "test_text2");
        artemis.seeMessagesHaveEqualText(TEST_QUEUE, "test_text2");
    }

    @Test
    void testMessageContainsText() {

        artemis.pushToQueue(TEST_QUEUE, "test_text1");
        artemis.pushToQueue(TEST_QUEUE, "test_text2");
        artemis.seeMessagesContainText(TEST_QUEUE, "test_text");
    }


    @Test
    void testMessageEqualJson() {

        final String JSON = """
                {"id":1, "name": "Alex"}""";


        artemis.pushToQueue(TEST_QUEUE, JSON);
        artemis.pushToQueue(TEST_QUEUE, "test_text2");
        artemis.seeMessagesHaveEqualJson(TEST_QUEUE, JSON);
    }

    @Test
    void testMessageContainsJson() {

        artemis.pushToQueue(TEST_QUEUE, "test_text1");
        artemis.pushToQueue(TEST_QUEUE, """
                {"id":1, "name": "Alex"}""");

        artemis.seeMessagesContainJson(TEST_QUEUE, """
                {"name": "Alex"}""");
    }

    @Test
    void testMessageIsJson() {

        artemis.pushToQueue(TEST_QUEUE, "test_text1");
        artemis.pushToQueue(TEST_QUEUE, """
                {"id":1, "name": "Alex"}""");

        artemis.grabMessagesFromQueue(TEST_QUEUE)
                .seeListAnyJsonType();
    }

    @Test
    void testMessageJsonFromFile() {

        var path = "artemisdata/test1.json";

        artemis.pushToQueue(TEST_QUEUE, readTextFromFile(path));

        artemis.grabMessagesFromQueue(TEST_QUEUE)
                .seeListAnyEqualsJson(Path.of(path))
                .seeListAnyContainsJson(Path.of(path));
    }

    @Test
    void testMessageJsonSchemaCheck() {


        artemis.pushToQueue(TEST_QUEUE,
                """
                        {
                            "id": 3
                        }""");

        artemis.grabMessagesFromQueue(TEST_QUEUE)
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


        artemis.pushToQueue(TEST_QUEUE,
                """
                        {
                            "id": 3
                        }""");

        artemis.grabMessagesFromQueue(TEST_QUEUE)
                .seeListAnyJsonMatchSchema(Path.of("artemisdata/schema_1.json"));
    }

    @Test
    void testCountInEmptyQueue() {

        artemis.seeCountMessagesInQueueExactly(TEST_QUEUE, 0);
    }

    @Test
    void testCleanMultipleQueues() {

        String queue1 = "queue1";
        String queue2 = "queue2";

        artemis.pushToQueue(queue1, "test_text1");
        artemis.pushToQueue(queue2, "test_text2");

        artemis.cleanQueues(queue1, queue2);

        artemis.seeQueueIsEmpty(queue1);
        artemis.seeQueueIsEmpty(queue2);
    }

    @Test
    void testMessageReadAndCheck() {


        artemis.pushToQueue(TEST_QUEUE,"test_1");
        artemis.pushToQueue(TEST_QUEUE,"test_2");

        artemis.readMessagesFromQueue(TEST_QUEUE)
                .seeListAnyEquals("test_2");


        artemis.readMessagesFromQueue(TEST_QUEUE)
                .seeListHasExactlyCount(2)
                .seeListAnyEquals("test_1");

    }

    @Test
    void testMessageReadEmptyCheck() {

        artemis.readMessagesFromQueue(TEST_QUEUE)
                .seeListHasExactlyCount(0);
    }


}

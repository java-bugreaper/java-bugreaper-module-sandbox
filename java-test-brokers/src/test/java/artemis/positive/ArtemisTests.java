package artemis.positive;

import artemis.TestArtemisSetup;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;

import static io.bugreaper.core.assertable.stringlist.ListOperators.*;
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
                .testInLIst(elementsCountInList(2));
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
                .testInLIst(elementsCountInList(2));

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
                .testInLIst(stringContainsInList("test_text2"))
                .testInLIst(stringContainsInList("text1"));
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
        artemis.seeMessagesContainsText(TEST_QUEUE, "test_text");
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

        artemis.seeMessagesContainsJson(TEST_QUEUE, """
                {"name": "Alex"}""");
    }

    @Test
    void testMessageIsJson() {

        artemis.pushToQueue(TEST_QUEUE, "test_text1");
        artemis.pushToQueue(TEST_QUEUE, """
                {"id":1, "name": "Alex"}""");

        artemis.grabMessagesFromQueue(TEST_QUEUE)
                .testInLIst(isJsonType());
    }

    @Test
    void testMessageJsonFromFile() {

        var path = "artemisdata/test1.json";

        artemis.pushToQueue(TEST_QUEUE, readTextFromFile(path));

        artemis.grabMessagesFromQueue(TEST_QUEUE)
                .testInLIst(jsonEqualsInList(Path.of(path)))
                .testInLIst(jsonContainsInList(Path.of(path)));
    }

    @Test
    void testMessageJsonSchemaCheck() {


        artemis.pushToQueue(TEST_QUEUE,
                """
                        {
                            "id": 3
                        }""");

        artemis.grabMessagesFromQueue(TEST_QUEUE)
                .testInLIst(jsonSchemaCheckInList("""
                        {
                          "type": "object",
                          "properties": {
                            "id": {
                              "type": "integer"
                            }
                          },
                          "additionalProperties": false
                        }"""));
    }

    @Test
    void testMessageJsonSchemaCheckFromFile() {


        artemis.pushToQueue(TEST_QUEUE,
                """
                        {
                            "id": 3
                        }""");

        artemis.grabMessagesFromQueue(TEST_QUEUE)
                .testInLIst(jsonSchemaCheckInList(Path.of("artemisdata/schema_1.json")));
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
                .testInLIst(stringEqualsInList("test_2"));


        artemis.readMessagesFromQueue(TEST_QUEUE)
                .testInLIst(elementsCountInList(2))
                .testInLIst(stringEqualsInList("test_1"));

    }

    @Test
    void testMessageReadEmptyCheck() {

        artemis.readMessagesFromQueue(TEST_QUEUE)
                .testInLIst(elementsCountInList(0));
    }


}

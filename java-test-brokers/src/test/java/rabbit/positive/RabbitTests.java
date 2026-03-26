package rabbit.positive;

import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import rabbit.TestRabbitSetup;


@Feature("[rabbit] Tests as is")
class RabbitTests extends TestRabbitSetup {

    @Test
    void createQueue_pushMessageToQueue_checkNotEmpty_checkCount() {

        rabbit.pushToQueue(TEST_QUEUE, "test_message");
        rabbit.seeQueueIsNotEmpty(TEST_QUEUE);
        rabbit.seeQueueMessageCountIsExactly(TEST_QUEUE, 1);
    }

    @Test
    void createQueue_pushMessageToQueueThanPurgeQueue_checkNotEmpty_checkCount() {

        rabbit.declareQueue(TEST_QUEUE);
        rabbit.pushToQueue(TEST_QUEUE, "test_message");
        rabbit.purgeQueue(TEST_QUEUE);
        rabbit.seeQueueIsEmpty(TEST_QUEUE);
        rabbit.seeQueueMessageCountIsExactly(TEST_QUEUE, 0);
    }

    @Test
    void createDirectExchangeCreateQueuesThanBindQueues_pushToExchange_checkNotEmpty_checkEmpty() {


        final String exchange = "Exchange_AB";
        final String queue1 = "test_queue_a";
        final String queue2 = "test_queue_b";
        final String key1 = "r_key_1";
        final String key2 = "r_key_2";

        rabbit.deleteExchange(exchange);
        rabbit.deleteQueue(queue1);
        rabbit.deleteQueue(queue2);

        rabbit.declareExchange(exchange, "direct");

        rabbit.declareQueue(queue1);
        rabbit.declareQueue(queue2);

        rabbit.bindQueue(queue1, exchange, key1);
        rabbit.bindQueue(queue2, exchange, key2);

        rabbit.pushToExchange(exchange, key1,"test_message");

        rabbit.seeQueueIsNotEmpty(queue1);
        rabbit.seeQueueIsEmpty(queue2);
    }

    @Test
    void createQueue_pushMessageToQueue_checkLastMessageEqual() {

        final String lastMessage = "My testMessageToCheckEqual";

        rabbit.declareQueue(TEST_QUEUE);
        rabbit.pushToQueue(TEST_QUEUE, lastMessage);

        rabbit.seeMessagesHaveEqualText(TEST_QUEUE, lastMessage);
    }

    @Test
    void testOneQueueMultipleTimesInOne() {

        rabbit.declareQueue(TEST_QUEUE);

        rabbit.pushToQueue(TEST_QUEUE, "test1");
        rabbit.seeMessagesHaveEqualText(TEST_QUEUE, "test1");

        rabbit.pushToQueue(TEST_QUEUE, "test2");
        rabbit.seeMessagesHaveEqualText(TEST_QUEUE, "test2");
    }

    @Test
    void createQueue_pushMessageToQueue_checkLastMessageContains() {

        final String lastMessage = "My testMessageToCheckContains";

        rabbit.declareQueue(TEST_QUEUE);
        rabbit.pushToQueue(TEST_QUEUE, lastMessage);

        rabbit.seeMessagesContainText(TEST_QUEUE, "MessageToCheck");
    }

    @Test
    void createQueue_pushMessageToQueue_checkLastMessageContainsJson() {

        final String lastMessage = """
                {
                  "status": 11,
                  "statusName": "Something",
                  "name": "Alex"
                }""";
        final String expectedPart = """
                {
                  "status": 11,
                  "name": "Alex"
                }""";

        rabbit.declareQueue(TEST_QUEUE);
        rabbit.pushToQueue(TEST_QUEUE, lastMessage);

        rabbit.seeMessagesContainJson(TEST_QUEUE, expectedPart);
    }

    @Test
    void createQueue_pushMessageToQueue_checkLastMessageIsEqualJson() {

        final String lastMessage = """
                {
                  "status": 112,
                  "statusName": "Something3",
                  "name": "AlexNew"
                }""";

        rabbit.declareQueue(TEST_QUEUE);
        rabbit.pushToQueue(TEST_QUEUE, lastMessage);

        rabbit.seeQueueIsNotEmpty(TEST_QUEUE);
        rabbit.seeQueueMessageCountIsExactly(TEST_QUEUE, 1);

        rabbit.seeMessagesHaveEqualJson(TEST_QUEUE, lastMessage);
    }

}

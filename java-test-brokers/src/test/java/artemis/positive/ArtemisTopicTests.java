package artemis.positive;

import artemis.TestArtemisSetup;
import net.bugreaper.modules.artemis.Artemis;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;


class ArtemisTopicTests extends TestArtemisSetup {

    protected final Artemis artemis = Artemis.getInstance();
    final String topic = "new_topic";

    @BeforeEach
    void deleteTopic() {
        artemis.deleteTopic(topic);
    }

    @Test
    void createCheckDeleteTopicNotBounded() {

        artemis.createTopic(topic);
        artemis.seeTopicExists(topic);

        artemis.deleteTopic(topic);

        assertThrows(AssertionError.class, () ->
                artemis.seeTopicExists(topic)
        );
    }

    @Test
    void createCheckDeleteTopicBounded() {
        String queue1 = "queue_#1";
        String queue2 = "queue_#2";
        String queue3 = "queue_#3";

        artemis.createTopic(topic);
        artemis.boundQueue(queue1, topic);
        artemis.boundQueue(queue2, topic);
        artemis.boundQueue(queue3, topic);

        artemis.getQueuesListForTopic(topic)
                .seeListHasExactlyCount(3)
                .seeListAnyEquals(queue1)
                .seeListAnyEquals(queue2)
                .seeListAnyEquals(queue3);

        artemis.deleteQueue(queue2);

        artemis.getQueuesListForTopic(topic)
                .seeListHasExactlyCount(2)
                .seeListAnyEquals(queue1)
                .seeListAnyEquals(queue3);

        artemis.deleteTopic(topic);

        assertThrows(AssertionError.class, () ->
                artemis.seeTopicExists(topic)
        );
    }

    @Test
    void createAndBoundWithFilters() {
        String queue = "queue_all";
        String queue1 = "queue_#1";
        String queue2 = "queue_#2";

        artemis.createTopic(topic);

        artemis.boundQueue(queue, topic);
        artemis.boundQueue(queue1, topic, "my_filter = 'admin'");
        artemis.boundQueue(queue2, topic, "my_filter = 'user'");

        artemis.getQueuesListForTopic(topic)
                .seeListHasExactlyCount(3);

        artemis.pushToTopic(topic, "message_all");
        artemis.pushToQueue(queue, "message_all2");
        artemis.pushToTopic(topic, "message_1", Map.of("my_filter", "admin"));
        artemis.pushToTopic(topic, "message_2", Map.of("my_filter", "user"));


        artemis.seeCountMessagesInQueueExactly(queue, 4);
        artemis.seeCountMessagesInQueueExactly(queue1, 1);
        artemis.seeCountMessagesInQueueExactly(queue2, 1);

        artemis.grabMessagesFromQueue(queue1)
                .seeListHasExactlyCount(1)
                .seeListAnyEquals("message_1");

        artemis.readMessagesFromQueue(queue2)
                .seeListHasExactlyCount(1)
                .seeListAnyEquals("message_2");

        artemis.grabMessagesFromQueue(queue)
                .seeListHasExactlyCount(4)
                .seeListAnyEquals("message_2")
                .seeListAnyEquals("message_1")
                .seeListAnyEquals("message_all")
                .seeListAnyEquals("message_all2");
    }
}

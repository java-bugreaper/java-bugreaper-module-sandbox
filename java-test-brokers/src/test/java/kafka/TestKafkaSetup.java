package kafka;

import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import net.bugreaper.modules.kafka.Kafka;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;


@Tag("kafka")
@Epic("[kafka] Tests for Kafka")
public abstract class TestKafkaSetup {


    protected static final Kafka kafka = Kafka.getInstance();
    protected static final String TOPIC = "my-topic";
    protected static final String TOPIC_WITH_PART = "my-topic-with_partitions";


    @BeforeAll
    public static void create() {
        kafka.createTopic(TOPIC);
        kafka.createTopic(TOPIC_WITH_PART, 3);
    }

    @BeforeEach
    @Step("Clean topic")
    public void clean() {
        kafka.purgeTopic(TOPIC);
        kafka.purgeTopic(TOPIC_WITH_PART);
    }

}

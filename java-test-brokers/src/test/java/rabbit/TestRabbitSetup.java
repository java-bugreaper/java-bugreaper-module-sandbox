package rabbit;

import io.bugreaper.ProjectConfig;
import io.bugreaper.modules.rabbit.Rabbit;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;


@Tag("rabbit")
@Epic("[rabbit] Tests as is")
public abstract class TestRabbitSetup {

    static final ProjectConfig conf = ConfigFactory.create(ProjectConfig.class, System.getProperties());

    protected static final Rabbit rabbit = new Rabbit(
            conf.rabbitUrl(),
            conf.rabbitPort(),
            conf.rabbitUsername(),
            conf.rabbitPassword());

    protected static final String TEST_QUEUE = "my-test-queue";

    @BeforeAll
    public static void createBucket() {
        rabbit.declareQueue(TEST_QUEUE);
    }

    @BeforeEach
    @Step("Clean queue")
    public void clean() {
        rabbit.purgeQueue(TEST_QUEUE);
    }


}

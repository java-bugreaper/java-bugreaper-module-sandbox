package artemis;

import io.bugreaper.ProjectConfig;
import io.bugreaper.modules.artemis.Artemis;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.aeonbits.owner.ConfigFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;


@Tag("artemis")
@Epic("[artemis] Tests as is")
public abstract class TestArtemisSetup {

    final ProjectConfig conf = ConfigFactory.create(ProjectConfig.class, System.getProperties());

    protected final Artemis artemis = new Artemis(
            conf.artemisUrl(),
            conf.artemisPort(),
            conf.artemisUsername(),
            conf.artemisPassword());

    protected static final String TEST_QUEUE = "my-test-queue";


    @BeforeEach
    @Step("Clean queue")
    public void clean() {
        artemis.cleanQueue(TEST_QUEUE);
    }


}

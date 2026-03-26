package artemis;

import net.bugreaper.modules.artemis.Artemis;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;


@Tag("artemis")
@Epic("[artemis] Tests as is")
public abstract class TestArtemisSetup {

    protected final Artemis artemis = Artemis.getInstance();

    protected static final String TEST_QUEUE = "my-test-queue";


    @BeforeEach
    @Step("Clean queue")
    public void clean() {
        artemis.cleanQueue(TEST_QUEUE);
    }


}

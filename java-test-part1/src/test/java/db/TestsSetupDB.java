package db;

import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;
import setup.db.SqlMaria;


import static net.bugreaper.core.allurereporter.AllureReporter.reporter;


@Tag("db")
@Epic("[db] Tests for DataBases")
@TestMethodOrder(MethodOrderer.MethodName.class)
public abstract class TestsSetupDB {

    protected final SqlMaria sqlMaria = new SqlMaria();

    protected static final String TEST_10_MINUTES = "2025-01-01 00:10:00";
    protected static final String TEST_20_MINUTES = "2025-01-01 00:20:00";
    protected static final String TEST_30_MINUTES = "2025-01-01 00:30:00";


    @BeforeEach
    @Step("Clear test data")
    public void clean() {
        sqlMaria.cleanMariaDb();
    }

    @AfterEach
    @Step("Logs after tests")
    public void getLogs() {
        reporter("Here you can get some logs or other info");
        //SHOW table //TODO
    }

}

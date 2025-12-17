package db;

import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;

import static net.bugreaper.core.allurereporter.AllureReporter.reporter;


@Tag("db")
@Epic("[db] Tests for DataBases")
@TestMethodOrder(MethodOrderer.MethodName.class)
public abstract class TestsSetupDB {


    // example how to use db insert\asset in different instances
    protected final SqlPostgreInsert sqlPgInsert = SqlPostgreInsert.getInstance();
    protected final SqlPostgreAssert sqlPgAssert = SqlPostgreAssert.getInstance();

    protected final SqlMs msSql = SqlMs.getInstance();
    protected final SqlOracle sqlOracle = SqlOracle.getInstance();

    protected static final String DATE_TIME_TEST = "2020-01-02 10:01:00";
    protected static final String DATE_TEST = "2020-01-01";


    @BeforeEach
    @Step("Clear test data")
    public void clean() {
        sqlPgInsert.cleanPostgreDb();
        msSql.cleanMsSqlDb();
        sqlOracle.cleanOracleDb();
    }

    @AfterEach
    @Step("Logs after tests")
    public void getLogs() {
        reporter("Here you can get some logs or other info");
    }

}

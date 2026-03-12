package db;

import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.junit.jupiter.api.*;

import static net.bugreaper.core.allurereporter.AllureReporter.reporter;
import static net.bugreaper.core.generators.DateTimeGenerator.generateDate;
import static net.bugreaper.core.generators.DateTimeGenerator.generateDateTime;


@Tag("db")
@Epic("[db] Tests for DataBases")
@TestMethodOrder(MethodOrderer.MethodName.class)
public abstract class TestsSetupDB {


    // example how to use db insert\asset in different instances
    protected final SqlPostgreInsert sqlPgInsert = SqlPostgreInsert.getInstance();
    protected final SqlPostgreAssert sqlPgAssert = SqlPostgreAssert.getInstance();

    protected final SqlMs msSql = SqlMs.getInstance();
    protected final SqlOracle sqlOracle = SqlOracle.getInstance();

    protected static final String DATE_TIME_TEST = generateDateTime("-1M2d4H");
    protected static final String DATE_TIME_TEST2 = generateDateTime("+1y2d", "11:10:10");
    protected static final String DATE_TEST = generateDate("-1y+2d");


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

package db.failed;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Feature;
import io.qameta.allure.Step;
import net.bugreaper.modules.db.CustomDb;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("db")
@Epic("[db] Tests for DataBases")
@Feature("Db checks: CustomDB (Clickhouse) failed")
class ClickhouseDbFailedTest {

    //custom connect for clickhouse from config
    private final CustomDb db = CustomDb.getInstance();

    @BeforeEach
    @Step("Clear test data")
    public void clean() {
        db.truncateTable("myschema.users");
    }

    @Test
    @Description("""
            Create data in Clickhouse table
            -> failed asserts""")
    void checkClickhouse_failedWithData() {

        db.insertIntoTable("myschema.users",
                """
                        {
                          "id": 2,
                          "name": "Alex"
                        }""");

        assertThrows(AssertionError.class, () ->
                db.seeTableIsEmpty("myschema.users")
        );

        assertThrows(AssertionError.class, () ->
                db.withAwaitMs(200)
                        .seeRecordExistsInTable("myschema.users",
                                """
                                        {
                                          "id": 1,
                                          "name": "John"
                                        }""")
        );
    }

    @Test
    @Description("""
            Clean data in Clickhouse table
            -> failed asserts""")
    void checkClickhouse_failedWithoutData() {


        assertThrows(AssertionError.class, () ->
                db.seeRowsCountInTableExactly("myschema.users", 2)
        );

        assertThrows(AssertionError.class, () ->
                db.withAwaitMs(200)
                        .seeRecordExistsInTable("myschema.users",
                                """
                                        {
                                          "id": 1,
                                          "name": "John"
                                        }""")
        );
    }

}

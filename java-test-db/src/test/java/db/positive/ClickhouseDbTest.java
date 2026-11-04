package db.positive;

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
@Feature("Db checks: CustomDB (Clickhouse)")
//@TestInstance(TestInstance.Lifecycle.PER_CLASS) //To call connection once per class! but .getInstance() is better
class ClickhouseDbTest {

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
            -> check data in Clickhouse table""")
    void checkClickhouse_basicInsertAndAssert() {

        db.insertIntoTable("myschema.users",
                """
                        {
                          "id": 2,
                          "name": "Alex"
                        }""");

        db.seeTableIsNotEmpty("myschema.users");

        db.seeRecordExistsInTable("myschema.users",
                """
                        {
                          "id:>=": 2,
                          "name:LIKE": "Ale%"
                        }""");
    }

    @Test
    @Description("""
            check Clickhouse assert failed (only diff, NOT all data)""")
    void checkClickhouse_assertFailed() {

        db.insertIntoTable("myschema.users",
                new Object[][]{

                        {"id", 2},
                        {"name", "Alex"},
                });

        assertThrows(AssertionError.class, () ->
                db.withAwaitMs(300).seeRecordExistsInTable("myschema.users",
                        new Object[][]{
                                {"id", "3"},
                                {"name", "=", "John"},
                        }));
    }

}

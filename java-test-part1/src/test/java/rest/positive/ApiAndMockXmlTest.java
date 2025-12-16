package rest.positive;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import org.junit.jupiter.api.Test;
import rest.TestsSetupApi;

import java.nio.file.Path;

import static org.hamcrest.Matchers.is;


@Feature("[rest] API & Mock checks XML")
class ApiAndMockXmlTest extends TestsSetupApi {

    @Test
    @Description("""
            Create GET mock XML
            -> GET request check XML fields""")
    void checkXml_mock_request_checkFields() {

        final var id = "77";

        mockCreate.getPetXml(id, 200, "Murka", 5, "55.99");

        api.getPet(id)
                .seeResponseCodeIs(200)
                .seeResponseBodyFieldMatch("pet.name", is("Murka"))
                .seeResponseBodyFieldMatch("pet.age", is("5"))
                .seeResponseBodyFieldMatch("pet.price", is("55.99"));

        mockCustomApi.assertAllMocksCount(1);
    }

    @Test
    @Description("""
            Create GET mock XML
            -> GET request check XML schema""")
    void checkXml_mock_request_checkSchema() {

        final var id = "99";

        mockCreate.getPetXml(id, 200, "Sharik", 15, "155.99");

        api.getPet(id)
                .seeResponseCodeIs(200)
                .seeResponseMatchesXmlSchema(Path.of("data/schema/petsSchema.xsd"));

        mockCustomApi.assertAllMocksCount(1);
    }

}

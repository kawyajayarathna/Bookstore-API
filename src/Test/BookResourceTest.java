import jakarta.ws.rs.core.Application;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import jakarta.ws.rs.core.Response;

public class BookResourceTest extends JerseyTest {

    @Override
    protected Application configure() {
        return new ResourceConfig().packages("com.bookstore.resource");
    }

    @Test
    public void testCreateAndGetBook() {
        String newBookJson = """
            {
              "title": "Test Book",
              "isbn": "1234567890",
              "publicationYear": 2020,
              "authorId": 5
            }
            """;

        Response post = target("/books").request().post(
                jakarta.ws.rs.client.Entity.json(newBookJson)
        );
        assertEquals(201, post.getStatus());

        Response get = target("/books/1").request().get();
        assertEquals(200, get.getStatus());
    }
}

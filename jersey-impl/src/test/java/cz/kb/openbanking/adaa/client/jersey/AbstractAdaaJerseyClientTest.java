package cz.kb.openbanking.adaa.client.jersey;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import io.netty.handler.codec.http.HttpMethod;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import org.mockserver.model.HttpStatusCode;

/**
 * Basic abstract test class for the ADAA client Jersey implementation.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
abstract class AbstractAdaaJerseyClientTest {

    protected static final String ACCOUNT_ID = "aKvemMIKdvv5VEvc5vdovmeeVas5w4wcSCSv4";

    protected static final int MOCK_SERVER_PORT = 1080;

    protected static final String MOCK_SERVER_URI = "http://localhost:" + MOCK_SERVER_PORT;

    protected ClientAndServer mockServer;

    @BeforeEach
    protected void createMockServer() {
        mockServer = new ClientAndServer(MOCK_SERVER_PORT);
    }

    @AfterEach
    protected void stopServer() {
        mockServer.stop();
    }

    /**
     * Configures a mock server.
     *
     * @param urlPath              URL of the mock server
     * @param responseResourceName path to the resource that represents a mock server's response
     * @param method               HTTP method
     * @param status               HTTP response
     */
    protected void configureServer(String urlPath, String responseResourceName, HttpMethod method,
                                   HttpStatusCode status)
    {
        if (StringUtils.isBlank(urlPath)) {
            throw new IllegalArgumentException("urlPath must not be empty");
        }
        if (StringUtils.isBlank(responseResourceName)) {
            throw new IllegalArgumentException("responseResourceName must not be empty");
        }
        if (method == null) {
            throw new IllegalArgumentException("method must not be null");
        }
        if (status == null) {
            throw new IllegalArgumentException("status must not be null");
        }

        String response;
        try {
            response = IOUtils.toString(
                getClass().getClassLoader().getResourceAsStream(responseResourceName), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("Error in getting content of resource '" + responseResourceName + "'.");
        }

        mockServer
            .when(
                request()
                    .withMethod(method.name())
                    .withPath(urlPath))
            .respond(
                response()
                    .withStatusCode(status.code())
                    .withHeader(new Header("Content-Type", "application/json"))
                    .withBody(response));
    }
}

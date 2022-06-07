package cz.kb.openbanking.adaa.client.jersey;

import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.API_KEY_HEADER_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.AUTHORIZATION_HEADER_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.CORRELATION_ID_HEADER_NAME;

import java.io.IOException;
import java.util.UUID;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cz.kb.openbanking.adaa.client.api.exception.ItemSearchException;
import cz.kb.openbanking.adaa.client.api.search.ItemSearch;
import cz.kb.openbanking.adaa.client.model.generated.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.glassfish.jersey.jackson.internal.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract class with partial implementation of the {@link ItemSearch}.
 *
 * @param <RES> type of the response's class
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see ItemSearch
 * @since 1.0
 */
abstract class AbstractItemSearch<RES> implements ItemSearch<RES> {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final WebTarget webTarget;

    /**
     * Necessary request's parameters.
     */
    private final RequestParameters requestParameters;

    /**
     * Specific type of the response class (wrapped by Jersey's {@link GenericType}).
     */
    private final GenericType<RES> responseClass;

    /**
     * New instance.
     *
     * @param webTarget         {@link WebTarget}
     * @param requestParameters {@link RequestParameters}
     * @param responseClass     specific type of the response class (wrapped by Jersey's {@link GenericType})
     */
    protected AbstractItemSearch(WebTarget webTarget, RequestParameters requestParameters,
                                 GenericType<RES> responseClass) {
        if (webTarget == null) {
            throw new IllegalArgumentException("webTarget must not be null");
        }
        if (requestParameters == null) {
            throw new IllegalArgumentException("requestParameters must not be null");
        }
        if (responseClass == null) {
            throw new IllegalArgumentException("responseClass must not be null");
        }

        this.webTarget = webTarget;
        this.requestParameters = requestParameters;
        this.responseClass = responseClass;
    }

    @Override
    public RES find() throws ItemSearchException {
        WebTarget webTarget = getWebTarget();
        webTarget.register(getJacksonJsonProvider());

        String correlationId = UUID.randomUUID().toString();
        log.info("Call resource '{}' with correlation id '{}'.", webTarget.getUri(), correlationId);

        try {
            return webTarget.request()
                            .accept(MediaType.WILDCARD_TYPE)
                            .header(CORRELATION_ID_HEADER_NAME, correlationId)
                            .header(API_KEY_HEADER_NAME, "Bearer " + getRequestParameters().getApiKey())
                            .header(AUTHORIZATION_HEADER_NAME, "Bearer " + getRequestParameters().getAccessToken())
                            .get(getResponseClass());
        } catch (Exception e) {
            log.error("Calling of resource ends with error. Error: " + e.getMessage(), e);
            throw parseException(e);
        }
    }

    /**
     * Parses a thrown exception and maps it to the {@link ItemSearchException}.
     *
     * @param exception any exception
     * @return {@link ItemSearchException}
     */
    private ItemSearchException parseException(Exception exception) {
        if (exception == null) {
            throw new IllegalArgumentException("exception must not be null");
        }

        ItemSearchException result;

        String errorMessage = "Error occurred during calling API. Error: ";
        if (exception instanceof WebApplicationException) {
            Response response = ((WebApplicationException) exception).getResponse();
            String errorBody = response.readEntity(String.class);
            try {
                ErrorResponse errorResponse = new ObjectMapper().readValue(errorBody, ErrorResponse.class);
                if (errorResponse.getErrors().size() == 0) {
                    errorMessage += response.getStatusInfo().getReasonPhrase();
                } else if (errorResponse.getErrors().size() == 1) {
                    errorMessage += errorResponse.getErrors().get(0).getMessage();
                } else {
                    errorMessage = "More then one error (" + errorResponse.getErrors().size() +
                            ") occurred during calling API.";
                }
                result = new ItemSearchException(errorMessage, errorResponse.getErrors());
            } catch (IOException e) {
                result = new ItemSearchException(errorMessage
                        + response.getStatusInfo().getReasonPhrase());
            }
        } else {
            result = new ItemSearchException(errorMessage + exception.getMessage());
        }

        return result;
    }

    /**
     * Gets {@link JacksonJaxbJsonProvider} with set {@link JavaTimeModule}.
     *
     * @return {@link JacksonJaxbJsonProvider} with set {@link JavaTimeModule}
     */
    private JacksonJaxbJsonProvider getJacksonJsonProvider() {
        JacksonJaxbJsonProvider jacksonJaxbJsonProvider = new JacksonJaxbJsonProvider();
        jacksonJaxbJsonProvider.setMapper(new ObjectMapper().registerModule(new JavaTimeModule()));

        return jacksonJaxbJsonProvider;
    }

    /**
     * Gets {@link WebTarget}.
     *
     * @return {@link WebTarget}
     */
    protected WebTarget getWebTarget() {
        return webTarget;
    }

    /**
     * Gets {@link RequestParameters}.
     *
     * @return {@link RequestParameters}
     */
    protected RequestParameters getRequestParameters() {
        return requestParameters;
    }

    /**
     * Gets response's class wrapped by the Jersey's {@link GenericType}.
     *
     * @return response's class wrapped by the Jersey's {@link GenericType}
     */
    protected GenericType<RES> getResponseClass() {
        return responseClass;
    }
}

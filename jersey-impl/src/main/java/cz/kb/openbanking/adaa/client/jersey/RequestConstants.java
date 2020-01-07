package cz.kb.openbanking.adaa.client.jersey;

/**
 * Contains all common constants for requests to use in the ADAA client.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
final class RequestConstants {

    public static final String PAGE_PARAM_NAME = "page";

    public static final String SIZE_PARAM_NAME = "size";

    public static final String FROM_DATE_PARAM_NAME = "fromDate";

    public static final String TO_DATE_PARAM_NAME = "toDate";

    public static final String ACCOUNT_ID_PATH_VAR_NAME = "accountId";

    public static final String CORRELATION_ID_HEADER_NAME = "x-correlation-id";

    public static final String API_KEY_HEADER_NAME = "x-api-key";

    public static final String AUTHORIZATION_HEADER_NAME = "Authorization";

    /**
     * No instance.
     */
    private RequestConstants() {
    }
}

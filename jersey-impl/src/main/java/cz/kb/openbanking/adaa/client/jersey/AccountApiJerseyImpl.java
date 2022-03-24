package cz.kb.openbanking.adaa.client.jersey;

import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.ACCOUNT_ID_PATH_VAR_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.STATEMENTS_DATE_FROM_PARAM_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.STATEMENT_ID_PATH_VAR_NAME;

import java.time.OffsetDateTime;
import java.util.List;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import cz.kb.openbanking.adaa.client.api.AccountApi;
import cz.kb.openbanking.adaa.client.api.model.PageSlice;
import cz.kb.openbanking.adaa.client.api.search.AccountBalancesSearch;
import cz.kb.openbanking.adaa.client.api.search.AccountSearch;
import cz.kb.openbanking.adaa.client.api.search.AccountStatementsSearch;
import cz.kb.openbanking.adaa.client.api.search.PdfStatementSearch;
import cz.kb.openbanking.adaa.client.api.search.TransactionHistorySearch;
import cz.kb.openbanking.adaa.client.model.generated.AccountBalance;
import cz.kb.openbanking.adaa.client.model.generated.AccountTransaction;
import cz.kb.openbanking.adaa.client.model.generated.Statement;
import org.apache.commons.lang3.StringUtils;

/**
 * Jersey implementation of the {@link AccountApi}.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see AccountApi
 * @since 1.0
 */
public class AccountApiJerseyImpl implements AccountApi {

    /**
     * ADAA API endpoint's path for the accounts resource.
     */
    private static final String ACCOUNTS_RESOURCE_PATH = "accounts";

    /**
     * ADAA API endpoint's path for the account's balances resource.
     */
    private static final String ACC_BALANCES_RESOURCE_PATH = ACCOUNTS_RESOURCE_PATH + "/{" + ACCOUNT_ID_PATH_VAR_NAME + "}/balances";

    /**
     * ADAA API endpoint's path for the account's statements resource.
     */
    private static final String ACC_STATEMENTS_RESOURCE_PATH = ACCOUNTS_RESOURCE_PATH + "/{" + ACCOUNT_ID_PATH_VAR_NAME + "}/statements";

    /**
     * ADAA API endpoint's path for the PDF statements resource.
     */
    private static final String PDF_STATEMENT_RESOURCE_PATH =
        ACC_STATEMENTS_RESOURCE_PATH + "/{" + STATEMENT_ID_PATH_VAR_NAME + "}";

    /**
     * ADAA API endpoint's path for the transaction history resource.
     */
    private static final String TH_RESOURCE_PATH = ACCOUNTS_RESOURCE_PATH + "/{" + ACCOUNT_ID_PATH_VAR_NAME + "}/transactions";

    /**
     * Jersey {@link Client} for calling the ADAA API.
     */
    private final Client client;

    /**
     * ADAA API's base URL.
     */
    private final String baseUrl;

    /**
     * API key to use to authorize a request against KB API store.
     */
    private final String apiKey;

    /**
     * New instance.
     *
     * @param baseUrl ADAA API's base URL
     * @param apiKey  API key to use to authorize a request against KB API store
     */
    public AccountApiJerseyImpl(String baseUrl, String apiKey) {
        this(baseUrl, apiKey, ClientBuilder.newClient());
    }

    /**
     * New instance.
     *
     * @param baseUrl ADAA API's base URL
     * @param apiKey  API key to use to authorize a request against KB API store
     * @param client  Jersey {@link Client} for calling the ADAA API
     */
    public AccountApiJerseyImpl(String baseUrl, String apiKey, Client client) {
        if (StringUtils.isBlank(baseUrl)) {
            throw new IllegalArgumentException("baseUrl must not be blank");
        }
        if (StringUtils.isBlank(apiKey)) {
            throw new IllegalArgumentException("apiKey must not be blank");
        }
        if (client == null) {
            throw new IllegalArgumentException("client must not be null");
        }

        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.client = client;
    }

    @Override
    public AccountSearch accounts(String accessToken) {
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be blank");
        }

        WebTarget webTarget = getClient().target(getBaseUrl())
                                         .path(ACCOUNTS_RESOURCE_PATH);

        return new AccountSearchImpl(webTarget, new RequestParameters(getApiKey(), accessToken),
            new GenericType<List<cz.kb.openbanking.adaa.client.model.generated.Account>>() {
            });
    }

    @Override
    public TransactionHistorySearch transactions(String accountId, String accessToken) {
        if (StringUtils.isBlank(accountId)) {
            throw new IllegalArgumentException("accountId must not be blank");
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be blank");
        }

        WebTarget webTarget = getClient().target(getBaseUrl())
                                         .path(TH_RESOURCE_PATH)
                                         .resolveTemplate(ACCOUNT_ID_PATH_VAR_NAME, accountId);

        return new TransactionHistorySearchImpl(webTarget, new RequestParameters(getApiKey(), accessToken),
            new GenericType<PageSlice<AccountTransaction>>() {
            });
    }

    @Override
    public AccountBalancesSearch balances(String accountId, String accessToken) {
        if (StringUtils.isBlank(accountId)) {
            throw new IllegalArgumentException("accountId must not be blank");
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be blank");
        }

        WebTarget webTarget = getClient().target(getBaseUrl())
                                         .path(ACC_BALANCES_RESOURCE_PATH)
                                         .resolveTemplate(ACCOUNT_ID_PATH_VAR_NAME, accountId);

        return new AccountBalancesSearchImpl(webTarget, new RequestParameters(getApiKey(), accessToken),
            new GenericType<List<AccountBalance>>() {
            });
    }

    @Override
    public AccountStatementsSearch statements(String accountId, String accessToken, OffsetDateTime dateFrom) {
        if (StringUtils.isBlank(accountId)) {
            throw new IllegalArgumentException("accountId must not be blank");
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be blank");
        }
        if (dateFrom == null) {
            throw new IllegalArgumentException("dateFrom must not be null");
        }

        WebTarget webTarget = getClient().target(getBaseUrl())
                                         .path(ACC_STATEMENTS_RESOURCE_PATH)
                                         .queryParam(STATEMENTS_DATE_FROM_PARAM_NAME, dateFrom)
                                         .resolveTemplate(ACCOUNT_ID_PATH_VAR_NAME, accountId);

        return new AccountStatementsSearchImpl(webTarget, new RequestParameters(getApiKey(), accessToken),
            new GenericType<List<Statement>>() {
            });
    }

    @Override
    public PdfStatementSearch statementPdf(String accountId, String accessToken, long statementId) {
        if (StringUtils.isBlank(accountId)) {
            throw new IllegalArgumentException("accountId must not be blank");
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be blank");
        }

        WebTarget webTarget = getClient().target(getBaseUrl())
                                         .path(PDF_STATEMENT_RESOURCE_PATH)
                                         .resolveTemplate(ACCOUNT_ID_PATH_VAR_NAME, accountId)
                                         .resolveTemplate(STATEMENT_ID_PATH_VAR_NAME, statementId);

        return new PdfStatementSearchImpl(webTarget, new RequestParameters(getApiKey(), accessToken),
            new GenericType<byte[]>() {
            });
    }

    protected Client getClient() {
        return client;
    }

    protected String getBaseUrl() {
        return baseUrl;
    }

    protected String getApiKey() {
        return apiKey;
    }
}

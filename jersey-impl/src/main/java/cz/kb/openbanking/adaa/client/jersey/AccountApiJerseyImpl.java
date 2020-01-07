package cz.kb.openbanking.adaa.client.jersey;

import cz.kb.openbanking.adaa.client.api.AccountApi;
import cz.kb.openbanking.adaa.client.api.model.Account;
import cz.kb.openbanking.adaa.client.api.model.PageSlice;
import cz.kb.openbanking.adaa.client.api.search.AccountBalancesSearch;
import cz.kb.openbanking.adaa.client.api.search.AccountStatementsSearch;
import cz.kb.openbanking.adaa.client.api.search.PdfStatementSearch;
import cz.kb.openbanking.adaa.client.api.search.TransactionHistorySearch;
import cz.kb.openbanking.adaa.client.model.generated.AccountBalance;
import cz.kb.openbanking.adaa.client.model.generated.AccountTransaction;
import cz.kb.openbanking.adaa.client.model.generated.Statement;
import org.apache.commons.lang3.StringUtils;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.time.OffsetDateTime;
import java.util.List;

import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.ACCOUNT_ID_PATH_VAR_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.STATEMENTS_DATE_FROM_PARAM_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.STATEMENT_ID_PATH_VAR_NAME;

/**
 * Jersey implementation of the {@link AccountApi}.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see AccountIdProvider
 * @see AccountApi
 * @since 1.0
 */
public class AccountApiJerseyImpl extends AccountIdProvider implements AccountApi {

    /**
     * ADAA API endpoint's path for the account's balances resource.
     */
    private static final String ACC_BALANCES_RESOURCE_PATH = "accounts/{" + ACCOUNT_ID_PATH_VAR_NAME + "}/balances";

    /**
     * ADAA API endpoint's path for the account's statements resource.
     */
    private static final String ACC_STATEMENTS_RESOURCE_PATH = "accounts/{" + ACCOUNT_ID_PATH_VAR_NAME + "}/statements";

    /**
     * ADAA API endpoint's path for the PDF statements resource.
     */
    private static final String PDF_STATEMENT_RESOURCE_PATH =
            ACC_STATEMENTS_RESOURCE_PATH + "/{" + STATEMENT_ID_PATH_VAR_NAME + "}";

    /**
     * ADAA API endpoint's path for the transaction history resource.
     */
    private static final String TH_RESOURCE_PATH = "accounts/{" + ACCOUNT_ID_PATH_VAR_NAME + "}/transactions";

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
            throw new IllegalArgumentException("baseUrl must not be empty or blank");
        }
        if (StringUtils.isBlank(apiKey)) {
            throw new IllegalArgumentException("apiKey must not be empty or blank");
        }
        if (client == null) {
            throw new IllegalArgumentException("client must not be null");
        }

        this.baseUrl = baseUrl;
        this.apiKey = apiKey;
        this.client = client;
    }

    @Override
    public TransactionHistorySearch transactions(Account account, String accessToken) {
        if (account == null) {
            throw new IllegalArgumentException("account must not be null");
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be empty or blank");
        }

        WebTarget webTarget = getClient().target(getBaseUrl())
                .path(TH_RESOURCE_PATH)
                .resolveTemplate(ACCOUNT_ID_PATH_VAR_NAME, getAccountId(account, accessToken));

        return new TransactionHistorySearchImpl(webTarget, new RequestParameters(getApiKey(), accessToken),
                new GenericType<PageSlice<AccountTransaction>>() {
                });
    }

    @Override
    public AccountBalancesSearch balances(Account account, String accessToken) {
        if (account == null) {
            throw new IllegalArgumentException("account must not be null");
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be empty or blank");
        }

        WebTarget webTarget = getClient().target(getBaseUrl())
                .path(ACC_BALANCES_RESOURCE_PATH)
                .resolveTemplate(ACCOUNT_ID_PATH_VAR_NAME, getAccountId(account, accessToken));

        return new AccountBalancesSearchImpl(webTarget, new RequestParameters(getApiKey(), accessToken),
                new GenericType<List<AccountBalance>>() {
                });
    }

    @Override
    public AccountStatementsSearch statements(Account account, String accessToken, OffsetDateTime dateFrom) {
        if (account == null) {
            throw new IllegalArgumentException("account must not be null");
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be empty or blank");
        }
        if (dateFrom == null) {
            throw new IllegalArgumentException("dateFrom must not be null");
        }

        WebTarget webTarget = getClient().target(getBaseUrl())
                .path(ACC_STATEMENTS_RESOURCE_PATH)
                .queryParam(STATEMENTS_DATE_FROM_PARAM_NAME, dateFrom)
                .resolveTemplate(ACCOUNT_ID_PATH_VAR_NAME, getAccountId(account, accessToken));

        return new AccountStatementsSearchImpl(webTarget, new RequestParameters(getApiKey(), accessToken),
                new GenericType<List<Statement>>() {
                });
    }

    @Override
    public PdfStatementSearch statementPdf(Account account, String accessToken, long statementId) {
        if (account == null) {
            throw new IllegalArgumentException("account must not be null");
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be empty or blank");
        }

        WebTarget webTarget = getClient().target(getBaseUrl())
                .path(PDF_STATEMENT_RESOURCE_PATH)
                .resolveTemplate(ACCOUNT_ID_PATH_VAR_NAME, getAccountId(account, accessToken))
                .resolveTemplate(STATEMENT_ID_PATH_VAR_NAME, statementId);

        return new PdfStatementSearchImpl(webTarget, new RequestParameters(getApiKey(), accessToken),
                new GenericType<byte[]>() {
                });
    }

    @Override
    protected Client getClient() {
        return client;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public String getApiKey() {
        return apiKey;
    }
}

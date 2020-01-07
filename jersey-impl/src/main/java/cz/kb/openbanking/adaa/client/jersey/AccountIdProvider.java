package cz.kb.openbanking.adaa.client.jersey;

import cz.kb.openbanking.adaa.client.api.model.Account;
import cz.kb.openbanking.adaa.client.model.generated.PostAccountIdsRequest;
import cz.kb.openbanking.adaa.client.model.generated.PostAccountIdsResponse;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.util.UUID;

import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.API_KEY_HEADER_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.AUTHORIZATION_HEADER_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.CORRELATION_ID_HEADER_NAME;

/**
 * Provides unique ID for a IBAN with a currency,
 * which will be used to get information about transaction history and account's balance.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
abstract class AccountIdProvider {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * ADAA API endpoint's path for the account's ID resource.
     */
    private static final String ACC_ID_RESOURCE_PATH = "accounts/account-ids";

    /**
     * Simple cache to store account IDs.
     */
    private static final InMemoryCacheImpl<Account, String> cache = InMemoryCacheImpl.getInstance();

    /**
     * Unique ID for a IBAN with a currency.
     *
     * @param account     {@link Account}
     * @param accessToken access token
     * @return unique ID for a IBAN with a currency
     */
    protected String getAccountId(Account account, String accessToken) {
        if (account == null) {
            throw new IllegalArgumentException("account must not be null");
        }
        if (StringUtils.isBlank(accessToken)) {
            throw new IllegalArgumentException("accessToken must not be empty");
        }

        String cachedAccountId = cache.get(account);
        if (cachedAccountId != null) {
            log.info("Returning account ID '{}' from cache...", cachedAccountId);
            return cachedAccountId;
        } else {
            PostAccountIdsRequest request = new PostAccountIdsRequest();
            request.setIban(account.getIban().toString());
            request.setCurrency(account.getCurrency().getCurrencyCode());

            String correlationId = UUID.randomUUID().toString();
            log.debug("Calling 'account-id' resource with correlation id '{}'.", correlationId);

            try {
                String accountId = getClient().target(getBaseUrl()).path(ACC_ID_RESOURCE_PATH)
                        .request(MediaType.APPLICATION_JSON_TYPE)
                        .header(CORRELATION_ID_HEADER_NAME, correlationId)
                        .header(API_KEY_HEADER_NAME, "Bearer " + getApiKey())
                        .header(AUTHORIZATION_HEADER_NAME, "Bearer " + accessToken)
                        .post(Entity.json(request), PostAccountIdsResponse.class).getAccountId();

                cache.put(account, accountId);
                return accountId;
            } catch (Exception e) {
                log.error("Calling of 'account-id'resource ends with error. Error: " + e.getMessage(), e);
                throw new IllegalStateException("Calling of 'account-id'resource ends with error.", e);
            }
        }
    }

    /**
     * Gets a {@link Client} for calling the ADAA API.
     *
     * @return {@link Client} for calling the ADAA API
     */
    protected abstract Client getClient();

    /**
     * Gets the ADAA API's base URL.
     *
     * @return ADAA API's base URL
     */
    protected abstract String getBaseUrl();

    /**
     * Gets API key.
     *
     * @return API key
     */
    protected abstract String getApiKey();
}

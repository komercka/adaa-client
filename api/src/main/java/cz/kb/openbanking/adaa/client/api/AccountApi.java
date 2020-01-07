package cz.kb.openbanking.adaa.client.api;

import cz.kb.openbanking.adaa.client.api.model.Account;
import cz.kb.openbanking.adaa.client.api.search.AccountBalancesSearch;
import cz.kb.openbanking.adaa.client.api.search.TransactionHistorySearch;

/**
 * Provides information about user's account details.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public interface AccountApi {

    /**
     * Searches for the user's transaction history.
     *
     * @param account     IBAN code with a currency
     * @param accessToken OAuth2 access token, used to requests' authorization
     * @return {@link TransactionHistorySearch}
     */
    TransactionHistorySearch transactions(Account account, String accessToken);

    /**
     * Searches for all account's balances.
     *
     * @param account     IBAN code with a currency
     * @param accessToken OAuth2 access token, used to requests' authorization
     * @return {@link AccountBalancesSearch}
     */
    AccountBalancesSearch balances(Account account, String accessToken);
}

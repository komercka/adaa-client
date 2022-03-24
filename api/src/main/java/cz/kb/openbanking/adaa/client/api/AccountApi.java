package cz.kb.openbanking.adaa.client.api;

import java.time.OffsetDateTime;

import cz.kb.openbanking.adaa.client.api.search.AccountBalancesSearch;
import cz.kb.openbanking.adaa.client.api.search.AccountSearch;
import cz.kb.openbanking.adaa.client.api.search.AccountStatementsSearch;
import cz.kb.openbanking.adaa.client.api.search.PdfStatementSearch;
import cz.kb.openbanking.adaa.client.api.search.TransactionHistorySearch;

/**
 * Provides information about user's account details.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public interface AccountApi {

    /**
     * Searches for all accounts.
     *
     * @param accessToken OAuth2 access token, used to requests' authorization
     * @return {@link AccountSearch}
     */
    AccountSearch accounts(String accessToken);

    /**
     * Searches for the user's transaction history.
     *
     * @param accountId     id of account
     * @param accessToken OAuth2 access token, used to requests' authorization
     * @return {@link TransactionHistorySearch}
     */
    TransactionHistorySearch transactions(String accountId, String accessToken);

    /**
     * Searches for all account's balances.
     *
     * @param accountId     id of account
     * @param accessToken OAuth2 access token, used to requests' authorization
     * @return {@link AccountBalancesSearch}
     */
    AccountBalancesSearch balances(String accountId, String accessToken);

    /**
     * Searches for all account's statements.
     *
     * @param accountId     id of account
     * @param accessToken OAuth2 access token, used to requests' authorization
     * @param dateFrom    date of returned statements
     * @return {@link AccountStatementsSearch}
     */
    AccountStatementsSearch statements(String accountId, String accessToken, OffsetDateTime dateFrom);

    /**
     * Searches for statement in the PDF format.
     *
     * @param accountId     id of account
     * @param accessToken OAuth2 access token, used to requests' authorization
     * @param statementId statement identifier
     * @return {@link PdfStatementSearch}
     */
    PdfStatementSearch statementPdf(String accountId, String accessToken, long statementId);
}

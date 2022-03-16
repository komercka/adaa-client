package cz.kb.openbanking.adaa.client.api.search;

import java.util.List;

import cz.kb.openbanking.adaa.client.model.generated.Account;

/**
 * Searches for all accounts.
 *
 * @see ItemSearch
 * @see Account
 * @since 1.3
 */
public interface AccountSearch extends ItemSearch<List<Account>> {
}

package cz.kb.openbanking.adaa.client.api.search;

import cz.kb.openbanking.adaa.client.model.generated.AccountBalance;

import java.util.List;

/**
 * Searches for all account's balances.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see ItemSearch
 * @see AccountBalance
 * @since 1.0
 */
public interface AccountBalancesSearch extends ItemSearch<List<AccountBalance>> {
}

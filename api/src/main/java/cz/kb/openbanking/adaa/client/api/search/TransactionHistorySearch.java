package cz.kb.openbanking.adaa.client.api.search;

import cz.kb.openbanking.adaa.client.api.model.PageSlice;
import cz.kb.openbanking.adaa.client.model.generated.AccountTransaction;

/**
 * Searches for all {@link AccountTransaction}s.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see ItemSearch
 * @see PageSearch
 * @see DateSearch
 * @see PageSlice
 * @since 1.0
 */
public interface TransactionHistorySearch extends ItemSearch<PageSlice<AccountTransaction>>,
        PageSearch<TransactionHistorySearch>, DateSearch<TransactionHistorySearch> {
}

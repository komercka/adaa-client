package cz.kb.openbanking.adaa.client.api.search;

import cz.kb.openbanking.adaa.client.model.generated.Statement;

import java.util.List;

/**
 * Searches for all account's statements.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see ItemSearch
 * @see Statement
 * @since 1.1
 */
public interface AccountStatementsSearch extends ItemSearch<List<Statement>> {
}

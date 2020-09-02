package cz.kb.openbanking.adaa.client.jersey;

import cz.kb.openbanking.adaa.client.api.search.AccountStatementsSearch;
import cz.kb.openbanking.adaa.client.model.generated.Statement;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

/**
 * Jersey implementation of the {@link AccountStatementsSearch}.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see AccountStatementsSearch
 * @since 1.1
 */
final class AccountStatementsSearchImpl extends AbstractItemSearch<List<Statement>>
        implements AccountStatementsSearch {

    /**
     * New instance.
     *
     * @param webTarget         {@link WebTarget}
     * @param requestParameters {@link RequestParameters}
     * @param responseClass     specific type of the response class
     */
    public AccountStatementsSearchImpl(WebTarget webTarget, RequestParameters requestParameters,
                                       GenericType<List<Statement>> responseClass) {
        super(webTarget, requestParameters, responseClass);
    }
}

package cz.kb.openbanking.adaa.client.jersey;

import java.util.List;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

import cz.kb.openbanking.adaa.client.api.search.AccountSearch;
import cz.kb.openbanking.adaa.client.model.generated.Account;

/**
 * Jersey implementation of the {@link AccountSearch}.
 *
 * @see AccountSearch
 * @since 1.3
 */
public class AccountSearchImpl extends AbstractItemSearch<List<Account>> implements AccountSearch {

    /**
     * New instance.
     *
     * @param webTarget         {@link WebTarget}
     * @param requestParameters {@link RequestParameters}
     * @param responseClass     specific type of the response class (wrapped by Jersey's {@link GenericType})
     */
    protected AccountSearchImpl(WebTarget webTarget, RequestParameters requestParameters, GenericType<List<Account>> responseClass) {
        super(webTarget, requestParameters, responseClass);
    }
}

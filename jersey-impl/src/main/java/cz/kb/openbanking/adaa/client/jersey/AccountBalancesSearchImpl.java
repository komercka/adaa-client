package cz.kb.openbanking.adaa.client.jersey;

import cz.kb.openbanking.adaa.client.api.search.AccountBalancesSearch;
import cz.kb.openbanking.adaa.client.model.generated.AccountBalance;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.util.List;

/**
 * Jersey implementation of the {@link AccountBalancesSearch}.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see AccountBalancesSearch
 * @since 1.0
 */
final class AccountBalancesSearchImpl extends AbstractItemSearch<List<AccountBalance>>
        implements AccountBalancesSearch {

    /**
     * New instance.
     *
     * @param webTarget         {@link WebTarget}
     * @param requestParameters {@link RequestParameters}
     * @param responseClass     specific type of the response class
     */
    public AccountBalancesSearchImpl(WebTarget webTarget, RequestParameters requestParameters,
                                     GenericType<List<AccountBalance>> responseClass) {
        super(webTarget, requestParameters, responseClass);
    }
}

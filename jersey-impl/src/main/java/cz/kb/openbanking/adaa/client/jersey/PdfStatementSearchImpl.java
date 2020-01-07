package cz.kb.openbanking.adaa.client.jersey;

import cz.kb.openbanking.adaa.client.api.search.PdfStatementSearch;

import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;

/**
 * Jersey implementation of the {@link PdfStatementSearch}.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see PdfStatementSearch
 * @since 1.1
 */
final class PdfStatementSearchImpl extends AbstractItemSearch<byte[]> implements PdfStatementSearch {

    /**
     * New instance.
     *
     * @param webTarget         {@link WebTarget}
     * @param requestParameters {@link RequestParameters}
     * @param responseClass     specific type of the response class
     */
    public PdfStatementSearchImpl(WebTarget webTarget, RequestParameters requestParameters,
                                  GenericType<byte[]> responseClass) {
        super(webTarget, requestParameters, responseClass);
    }
}

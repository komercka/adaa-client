package cz.kb.openbanking.adaa.client.jersey;

import cz.kb.openbanking.adaa.client.api.model.PageSlice;
import cz.kb.openbanking.adaa.client.api.search.TransactionHistorySearch;
import cz.kb.openbanking.adaa.client.model.generated.AccountTransaction;

import javax.annotation.Nullable;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import java.time.OffsetDateTime;

import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.FROM_DATE_PARAM_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.PAGE_PARAM_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.SIZE_PARAM_NAME;
import static cz.kb.openbanking.adaa.client.jersey.RequestConstants.TO_DATE_PARAM_NAME;

/**
 * Jersey implementation of the {@link TransactionHistorySearch}.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see TransactionHistorySearch
 * @since 1.0
 */
final class TransactionHistorySearchImpl extends AbstractItemSearch<PageSlice<AccountTransaction>>
        implements TransactionHistorySearch {

    /**
     * Default page size.
     */
    public static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * New instance.
     *
     * @param webTarget         {@link WebTarget}
     * @param requestParameters {@link RequestParameters}
     * @param responseClass     specific type of the response class
     */
    public TransactionHistorySearchImpl(WebTarget webTarget, RequestParameters requestParameters,
                                        GenericType<PageSlice<AccountTransaction>> responseClass) {
        super(webTarget, requestParameters, responseClass);
    }

    @Override
    public TransactionHistorySearch page(int page) {
        return new TransactionHistorySearchImpl(getWebTarget().queryParam(PAGE_PARAM_NAME, page),
                getRequestParameters(), getResponseClass());
    }

    /**
     * Specifies a size of the requested page of items. If {@code null} then default size (10) will be used.
     *
     * @param size size of the requested page of items
     * @return requested page of items
     */
    @Override
    public TransactionHistorySearch size(@Nullable Integer size) {
        if (size == null) {
            size = DEFAULT_PAGE_SIZE;
        }
        return new TransactionHistorySearchImpl(getWebTarget().queryParam(SIZE_PARAM_NAME, size),
                getRequestParameters(), getResponseClass());
    }

    @Override
    public TransactionHistorySearch fromDate(OffsetDateTime fromDate) {
        if (fromDate == null) {
            throw new IllegalArgumentException("fromDate must not be null");
        }

        return new TransactionHistorySearchImpl(getWebTarget().queryParam(FROM_DATE_PARAM_NAME, fromDate),
                getRequestParameters(), getResponseClass());
    }

    @Override
    public TransactionHistorySearch toDate(OffsetDateTime toDate) {
        if (toDate == null) {
            throw new IllegalArgumentException("toDate must not be null");
        }

        return new TransactionHistorySearchImpl(getWebTarget().queryParam(TO_DATE_PARAM_NAME, toDate),
                getRequestParameters(), getResponseClass());
    }
}

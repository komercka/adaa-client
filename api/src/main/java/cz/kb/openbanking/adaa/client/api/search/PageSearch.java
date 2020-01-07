package cz.kb.openbanking.adaa.client.api.search;

import javax.annotation.Nullable;

/**
 * Represents pagination requests.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public interface PageSearch<S extends PageSearch<S>> {

    /**
     * Specifies a number of the requested page of items.
     *
     * @param page page's number of the requested page of items
     * @return requested page of items
     */
    S page(int page);

    /**
     * Specifies a size of the requested page of items.
     *
     * @param size size of the requested page of items
     * @return requested page of items
     */
    S size(@Nullable Integer size);
}

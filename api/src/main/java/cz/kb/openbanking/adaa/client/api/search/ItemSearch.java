package cz.kb.openbanking.adaa.client.api.search;

import cz.kb.openbanking.adaa.client.api.exception.ItemSearchException;

/**
 * Searches for desired items.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public interface ItemSearch<RES> {

    /**
     * Finds a requested item.
     *
     * @return requested item
     * @throws ItemSearchException all errors
     */
    RES find() throws ItemSearchException;
}

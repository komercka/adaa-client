package cz.kb.openbanking.adaa.client.api.exception;

import cz.kb.openbanking.adaa.client.api.search.ItemSearch;
import cz.kb.openbanking.adaa.client.model.generated.Error;

import java.util.Collection;

/**
 * Exception that could be thrown by {@link ItemSearch}.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public class ItemSearchException extends AbstractAdaaClientException {

    /**
     * New instance.
     *
     * @param message error message
     */
    public ItemSearchException(String message) {
        super(message);
    }

    /**
     * New instance.
     *
     * @param message error message
     * @param errors  list of errors
     */
    public ItemSearchException(String message, Collection<Error> errors) {
        super(message, errors);
    }
}

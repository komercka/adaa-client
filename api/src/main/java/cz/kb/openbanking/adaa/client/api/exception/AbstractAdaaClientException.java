package cz.kb.openbanking.adaa.client.api.exception;

import cz.kb.openbanking.adaa.client.model.generated.Error;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Basic abstract exception for the ADAA client.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public abstract class AbstractAdaaClientException extends RuntimeException {

    private final List<Error> errors = new LinkedList<>();

    /**
     * New instance.
     *
     * @param message error message
     */
    protected AbstractAdaaClientException(String message) {
        super(message);
    }

    /**
     * New instance.
     *
     * @param message error message
     * @param errors  list of errors
     */
    protected AbstractAdaaClientException(String message, Collection<Error> errors) {
        this(message);

        this.errors.addAll(errors);
    }

    /**
     * Get all {@link Error}s.
     *
     * @return list of {@link Error}s
     */
    public List<Error> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}

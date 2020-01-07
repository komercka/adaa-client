package cz.kb.openbanking.adaa.client.api.search;

import java.time.OffsetDateTime;

/**
 * Searches requested items by a specific date interval.
 * <b>ADAA provides only transaction history not older than two years.</b>
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public interface DateSearch<T extends PageSearch<T>> {

    /**
     * Sets a start of specific date interval.
     *
     * @param fromDate a start of specific date interval
     * @return requested items filtered by specific date
     */
    T fromDate(OffsetDateTime fromDate);

    /**
     * Sets the end of specific date interval.
     *
     * @param toDate the end of specific date interval
     * @return requested items filtered by specific date
     */
    T toDate(OffsetDateTime toDate);
}

package cz.kb.openbanking.adaa.client.api.model;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.iban4j.Iban;

import java.util.Currency;

/**
 * Represents bank's account by IBAN code with currency.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
public class Account {
    private final Iban iban;
    private final Currency currency;

    /**
     * New instance.
     *
     * @param iban     IBAN code
     * @param currency currency
     */
    public Account(Iban iban, Currency currency) {
        if (iban == null) {
            throw new IllegalArgumentException("iban must not be null");
        }
        if (currency == null) {
            throw new IllegalArgumentException("currency must not be null");
        }

        this.iban = iban;
        this.currency = currency;
    }

    /**
     * Gets IBAN code.
     *
     * @return IBAN code
     */
    public Iban getIban() {
        return iban;
    }

    /**
     * Gets currency.
     *
     * @return currency
     */
    public Currency getCurrency() {
        return currency;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Account)) {
            return false;
        }

        Account that = (Account) o;

        return new EqualsBuilder()
                .append(getIban(), that.getIban())
                .append(getCurrency(), that.getCurrency())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(getIban())
                .append(getCurrency())
                .toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("iban", iban)
                .append("currency", currency)
                .toString();
    }
}

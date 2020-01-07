package cz.kb.openbanking.adaa.client.jersey;

import cz.kb.openbanking.adaa.client.api.model.Account;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.iban4j.Iban;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Currency;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test class for {@link InMemoryCacheImpl}.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @see InMemoryCacheImpl
 * @since 1.0
 */
class InMemoryCacheImplTest {

    /**
     * Test method for putting and removing cache items.
     */
    @Test
    void testPutRemoveItems() throws ReflectiveOperationException {
        setFinalStaticField(InMemoryCacheImpl.class, "CACHE_MAX_SIZE", 3);

        InMemoryCacheImpl<Account, String> cache = InMemoryCacheImpl.getInstance();

        Account account = new Account(Iban.valueOf("CZ9501000000001234567899"), Currency.getInstance("CZK"));
        Account account1 = new Account(Iban.valueOf("CZ9501000000001234567899"), Currency.getInstance("USD"));
        Account account2 = new Account(Iban.valueOf("CZ5508000000001234567899"), Currency.getInstance("CZK"));
        Account account3 = new Account(Iban.valueOf("CZ5508000000001234567899"), Currency.getInstance("USD"));

        cache.put(account, "accId1");
        cache.put(account1, "accId2");
        cache.put(account2, "accId3");
        assertThat(cache.size()).isEqualTo(3);
        assertThat(cache.get(account)).isNotNull();
        assertThat(cache.get(account1)).isNotNull();
        assertThat(cache.get(account2)).isNotNull();

        // put item that exceeds cache limit size (the first item will be removed from cache)
        cache.put(account3, "accId4");
        assertThat(cache.size()).isEqualTo(3);
        assertThat(cache.get(account)).isNull();
        assertThat(cache.get(account1)).isNotNull();
        assertThat(cache.get(account2)).isNotNull();
        assertThat(cache.get(account3)).isNotNull();
    }

    private static void setFinalStaticField(Class<?> clazz, String fieldName, Object value)
            throws ReflectiveOperationException {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        FieldUtils.removeFinalModifier(field);
        FieldUtils.writeStaticField(field, value);
    }
}
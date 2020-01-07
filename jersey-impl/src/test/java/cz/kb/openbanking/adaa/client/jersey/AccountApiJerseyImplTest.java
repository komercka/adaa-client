package cz.kb.openbanking.adaa.client.jersey;

import cz.kb.openbanking.adaa.client.api.AccountApi;
import cz.kb.openbanking.adaa.client.api.exception.ItemSearchException;
import cz.kb.openbanking.adaa.client.api.model.Account;
import cz.kb.openbanking.adaa.client.api.model.PageSlice;
import cz.kb.openbanking.adaa.client.model.generated.AccountBalance;
import cz.kb.openbanking.adaa.client.model.generated.AccountTransaction;
import cz.kb.openbanking.adaa.client.model.generated.AccountType;
import cz.kb.openbanking.adaa.client.model.generated.CreditDebitIndicator;
import cz.kb.openbanking.adaa.client.model.generated.CreditLine;
import cz.kb.openbanking.adaa.client.model.generated.CurrencyAmount;
import cz.kb.openbanking.adaa.client.model.generated.Error;
import cz.kb.openbanking.adaa.client.model.generated.TransactionCounterparty;
import cz.kb.openbanking.adaa.client.model.generated.TransactionReferences;
import cz.kb.openbanking.adaa.client.model.generated.TransactionType;
import io.netty.handler.codec.http.HttpMethod;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpStatusCode;
import org.mockserver.verify.VerificationTimes;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockserver.model.HttpRequest.request;

/**
 * Test class for the {@link AccountApiJerseyImpl}.
 *
 * @author <a href="mailto:aleh_kuchynski@kb.cz">Aleh Kuchynski</a>
 * @since 1.0
 */
class AccountApiJerseyImplTest extends AbstractAdaaJerseyClientTest {

    /**
     * Test method for the {@link AccountApiJerseyImpl#balances(Account, String)} with positive result.
     */
    @Test
    void test_getAccountBalances_ok() {
        configureServer("/accounts/account-ids", "response-account-id.json", HttpMethod.POST, HttpStatusCode.OK_200);
        configureServer("/accounts/" + ACCOUNT_ID + "/balances", "response-account-balances.json",
                HttpMethod.GET, HttpStatusCode.OK_200);

        AccountApi accountApi = new AccountApiJerseyImpl(MOCK_SERVER_URI, "apiKey");
        List<AccountBalance> result = accountApi.balances(getIbanWithCurrency(), "accessToken").find();

        assertThat(result.size()).isEqualTo(1);
        AccountBalance balance = result.get(0);
        CurrencyAmount amount = new CurrencyAmount();
        amount.setCurrency("EUR");
        amount.setValue(10000d);
        assertThat(balance.getAmount()).isEqualTo(amount);
        assertThat(balance.getValidAt()).isEqualTo(OffsetDateTime.parse("2020-01-16T14:03:31.217Z"));
        assertThat(balance.getCreditDebitIndicator()).isEqualTo(CreditDebitIndicator.CREDIT);
        CreditLine creditLine = new CreditLine();
        creditLine.setCurrency("EUR");
        creditLine.setValue(10000d);
        assertThat(balance.getCreditLine()).isEqualTo(creditLine);
        assertThat(balance.getType()).isEqualTo(AccountBalance.TypeEnum.PREVIOUSLY_CLOSED_BOOK);

        mockServer.verify(
                request()
                        .withPath("/accounts/" + ACCOUNT_ID + "/balances")
                        .withMethod(HttpMethod.GET.name())
                        .withHeader("x-api-key", "Bearer apiKey")
                        .withHeader("Authorization", "Bearer accessToken")
                        .withHeader("x-correlation-id")
                        .withHeader("Accept", "application/json"),
                VerificationTimes.exactly(1)
        );
    }


    /**
     * Test method for the {@link AccountApiJerseyImpl#transactions(Account, String)} with positive result.
     */
    @Test
    void test_getTransactions_ok() {
        configureServer("/accounts/account-ids", "response-account-id.json", HttpMethod.POST, HttpStatusCode.OK_200);
        configureServer("/accounts/" + ACCOUNT_ID + "/transactions", "response-transaction-history.json",
                HttpMethod.GET, HttpStatusCode.OK_200);

        AccountApi accountApi = new AccountApiJerseyImpl(MOCK_SERVER_URI, "apiKey");

        PageSlice<AccountTransaction> result = accountApi.transactions(getIbanWithCurrency(), "accessToken")
                .size(3)
                .page(0)
                .find();

        assertThat(result.getContent().size()).isEqualTo(2);
        assertThat(result.getTotalPages()).isEqualTo(1);
        assertThat(result.isEmpty()).isFalse();
        assertThat(result.getPageNumber()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(3);
        assertThat(result.getNumberOfElements()).isEqualTo(2);
        assertThat(result.isFirst()).isEqualTo(true);
        assertThat(result.isLast()).isEqualTo(true);

        CurrencyAmount currencyAmount = new CurrencyAmount();
        currencyAmount.setCurrency("EUR");
        currencyAmount.setValue(10000.0);
        CurrencyAmount instructed = new CurrencyAmount();
        instructed.setCurrency("EUR");
        instructed.setValue(10000.0);

        AccountTransaction transaction = result.getContent().get(0);
        assertThat(transaction.getLastUpdated()).isEqualTo(OffsetDateTime.parse("2020-01-14T09:33:06.323Z"));
        assertThat(transaction.getAccountType()).isEqualTo(AccountType.KB);
        assertThat(transaction.getEntryReference()).isEqualTo("KB-1234567890");
        assertThat(transaction.getIban()).isEqualTo("CZ9501000000001234567899");
        assertThat(transaction.getCreditDebitIndicator()).isEqualTo(CreditDebitIndicator.CREDIT);
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.DOMESTIC);
        assertThat(transaction.getAmount()).isEqualTo(currencyAmount);
        assertThat(transaction.getBookingDate()).isEqualTo(LocalDate.parse("2019-04-23"));
        assertThat(transaction.getValueDate()).isEqualTo(LocalDate.parse("2019-04-24"));
        assertThat(transaction.getInstructed()).isEqualTo(instructed);
        assertThat(transaction.getReversalIndicator()).isFalse();
        assertThat(transaction.getStatus()).isEqualTo("BOOK");
        TransactionCounterparty counterParty = transaction.getCounterParty();
        assertThat(counterParty).isNotNull();
        assertThat(counterParty.getAccountNo()).isEqualTo("1234567899");
        assertThat(counterParty.getBankBic()).isEqualTo("KOMBCZPPXXX");
        assertThat(counterParty.getBankCode()).isEqualTo("0100");
        assertThat(counterParty.getBankName()).isEqualTo("Česká spořitelna, a.s.");
        assertThat(counterParty.getIban()).isEqualTo("CZ9501000000001234567899");
        assertThat(counterParty.getName()).isEqualTo("Tesco Stores ČR a.s.");
        TransactionReferences references = transaction.getReferences();
        assertThat(references).isNotNull();
        assertThat(references.getConstant()).isEqualTo("0514");
        assertThat(references.getReceiver()).isEqualTo("Zpráva pro příjemce");
        assertThat(references.getSpecific()).isEqualTo("708090");
        assertThat(references.getVariable()).isEqualTo("2018001");
        assertThat(transaction.getAdditionalTransactionInformation())
                .isEqualTo("8201701069595 BIC: GIBACZPXXXX; #71A# SHA ZALOHA DLE SMLOUVY O DODAVKACH");

        // the second transaction
        currencyAmount.setCurrency("USD");
        currencyAmount.setValue(100.0);
        instructed.setCurrency("USD");
        instructed.setValue(100.0);

        transaction = result.getContent().get(1);
        assertThat(transaction.getLastUpdated()).isEqualTo(OffsetDateTime.parse("2020-01-15T09:33:06.323Z"));
        assertThat(transaction.getAccountType()).isEqualTo(AccountType.KB);
        assertThat(transaction.getEntryReference()).isEqualTo("KB-1234567891");
        assertThat(transaction.getIban()).isEqualTo("CZ9501000000001234567890");
        assertThat(transaction.getCreditDebitIndicator()).isEqualTo(CreditDebitIndicator.DEBIT);
        assertThat(transaction.getTransactionType()).isEqualTo(TransactionType.DOMESTIC);
        assertThat(transaction.getAmount()).isEqualTo(currencyAmount);
        assertThat(transaction.getBookingDate()).isEqualTo(LocalDate.parse("2019-05-23"));
        assertThat(transaction.getValueDate()).isEqualTo(LocalDate.parse("2019-05-24"));
        assertThat(transaction.getInstructed()).isEqualTo(instructed);
        assertThat(transaction.getReversalIndicator()).isFalse();
        assertThat(transaction.getStatus()).isEqualTo("BOOK");
        counterParty = transaction.getCounterParty();
        assertThat(counterParty).isNotNull();
        assertThat(counterParty.getAccountNo()).isEqualTo("1234567890");
        assertThat(counterParty.getBankBic()).isEqualTo("KOMBCZPPXXX");
        assertThat(counterParty.getBankCode()).isEqualTo("0100");
        assertThat(counterParty.getBankName()).isEqualTo("Česká spořitelna, a.s.");
        assertThat(counterParty.getIban()).isEqualTo("CZ9501000000001234567890");
        assertThat(counterParty.getName()).isEqualTo("Tesco Stores ČR a.s.");
        references = transaction.getReferences();
        assertThat(references).isNotNull();
        assertThat(references.getConstant()).isEqualTo("0515");
        assertThat(references.getReceiver()).isEqualTo("Zpráva pro příjemce 2");
        assertThat(references.getSpecific()).isEqualTo("708091");
        assertThat(references.getVariable()).isEqualTo("2018002");
        assertThat(transaction.getAdditionalTransactionInformation())
                .isEqualTo("8201701069596 BIC: GIBACZPXXXX; #71A# SHA ZALOHA DLE SMLOUVY O DODAVKACH");

        mockServer.verify(
                request()
                        .withPath("/accounts/" + ACCOUNT_ID + "/transactions")
                        .withMethod(HttpMethod.GET.name())
                        .withHeader("x-api-key", "Bearer apiKey")
                        .withHeader("Authorization", "Bearer accessToken")
                        .withHeader("x-correlation-id")
                        .withHeader("Accept", "application/json")
                        .withQueryStringParameter("page", "0")
                        .withQueryStringParameter("size", "3")
        );
    }

    /**
     * Test method for the {@link AccountApiJerseyImpl#balances(Account, String)} with unknown error occurred.
     */
    @Test
    void test_getAccountBalances_unknownError() {
        configureServer("/accounts/account-ids", "response-account-id.json", HttpMethod.POST, HttpStatusCode.OK_200);
        configureServer("/accounts/" + ACCOUNT_ID + "/balances", "response-unknown-error.xml", HttpMethod.GET,
                HttpStatusCode.FORBIDDEN_403);

        AccountApi accountApi = new AccountApiJerseyImpl(MOCK_SERVER_URI, "apiKey");

        Throwable thrown = catchThrowable(() -> accountApi.balances(getIbanWithCurrency(), "accessToken").find());

        ItemSearchException itemSearchException = (ItemSearchException) thrown;
        assertThat(itemSearchException.getMessage()).isEqualTo("Error occurred during calling API. Error: Forbidden");
        assertThat(itemSearchException.getErrors().size()).isEqualTo(0);
    }

    /**
     * Test method fot the {@link AccountApiJerseyImpl#transactions(Account, String)}
     * with missing correlation ID.
     */
    @Test
    void test_getTransactions_missingCorrelationId() {
        configureServer("/accounts/account-ids", "response-account-id.json", HttpMethod.POST, HttpStatusCode.OK_200);
        configureServer("/accounts/" + ACCOUNT_ID + "/transactions", "response-missing-corr-id.json", HttpMethod.GET,
                HttpStatusCode.BAD_REQUEST_400);

        AccountApi accountApi = new AccountApiJerseyImpl(MOCK_SERVER_URI, "apiKey");

        Throwable thrown = catchThrowable(() ->
                accountApi.transactions(getIbanWithCurrency(), "accessToken")
                        .size(3)
                        .page(0)
                        .find()
        );

        ItemSearchException itemSearchException = (ItemSearchException) thrown;
        assertThat(itemSearchException.getMessage()).isEqualTo(
                "Error occurred during calling API. Error: "
                        + "Required String parameter 'x-correlation-id' is not present");
        assertThat(itemSearchException.getErrors().size()).isEqualTo(1);

        Error error = itemSearchException.getErrors().get(0);
        assertThat(error.getCode()).isNull();
        assertThat(error.getMessage()).isEqualTo("Required String parameter 'x-correlation-id' is not present");
        assertThat(error.getAdditionalInfo()).isNotNull();
        assertThat(error.getAdditionalInfo().getParameterName()).isEqualTo("x-correlation-id");
        assertThat(error.getAdditionalInfo().getRejectedValue()).isEqualTo("wrongValue");
    }

    /**
     * Test method for the {@link AccountApiJerseyImpl#transactions(Account, String)}
     * with unknown error occurred.
     */
    @Test
    void test_getTransactions_unknownError() {
        configureServer("/accounts/account-ids", "response-account-id.json", HttpMethod.POST, HttpStatusCode.OK_200);
        configureServer("/accounts/" + ACCOUNT_ID + "/transactions", "response-unknown-error.xml", HttpMethod.GET,
                HttpStatusCode.FORBIDDEN_403);

        AccountApi accountApi = new AccountApiJerseyImpl(MOCK_SERVER_URI, "apiKey");

        Throwable thrown = catchThrowable(() ->
                accountApi.transactions(getIbanWithCurrency(), "accessToken")
                        .size(3)
                        .page(0)
                        .find()
        );

        ItemSearchException itemSearchException = (ItemSearchException) thrown;
        assertThat(itemSearchException.getMessage()).isEqualTo(
                "Error occurred during calling API. Error: Forbidden");
        assertThat(itemSearchException.getErrors().size()).isEqualTo(0);
    }

}
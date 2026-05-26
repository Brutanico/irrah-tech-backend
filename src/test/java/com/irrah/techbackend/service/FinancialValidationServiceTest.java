package com.irrah.techbackend.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.irrah.techbackend.domain.DocumentType;
import com.irrah.techbackend.domain.PlanType;
import com.irrah.techbackend.entity.Client;
import com.irrah.techbackend.exception.BusinessException;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class FinancialValidationServiceTest {

    private final FinancialValidationService service = new FinancialValidationService();

    @Test
    void shouldChargePrepaidClientWhenBalanceIsEnough() {
        Client client = client(PlanType.PREPAID, "1.00", "0.00", true);

        service.validateAndCharge(client, new BigDecimal("0.25"));

        assertThat(client.getBalance()).isEqualByComparingTo("0.75");
    }

    @Test
    void shouldRejectPrepaidClientWhenBalanceIsInsufficient() {
        Client client = client(PlanType.PREPAID, "0.10", "0.00", true);

        assertThatThrownBy(() -> service.validateAndCharge(client, new BigDecimal("0.25")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Insufficient balance");
    }

    @Test
    void shouldChargePostpaidLimitWhenLimitIsEnough() {
        Client client = client(PlanType.POSTPAID, "0.00", "1.00", true);

        service.validateAndCharge(client, new BigDecimal("0.50"));

        assertThat(client.getLimit()).isEqualByComparingTo("0.50");
    }

    @Test
    void shouldRejectInactiveClient() {
        Client client = client(PlanType.PREPAID, "1.00", "0.00", false);

        assertThatThrownBy(() -> service.validateAndCharge(client, new BigDecimal("0.25")))
                .isInstanceOf(BusinessException.class)
                .hasMessage("Client is inactive");
    }

    private Client client(PlanType planType, String balance, String limit, boolean active) {
        Client client = new Client();
        client.setId("client-1");
        client.setName("Client");
        client.setDocumentId("12345678901");
        client.setDocumentType(DocumentType.CPF);
        client.setPlanType(planType);
        client.setBalance(new BigDecimal(balance));
        client.setLimit(new BigDecimal(limit));
        client.setActive(active);
        return client;
    }
}

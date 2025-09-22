package com.portal.de.pagamentos.domain.payment_document;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.math.BigDecimal;
import java.math.RoundingMode;

@Embeddable
public class PaymentValue {

    @Column(name = "value", nullable = false, precision = 15, scale = 2)
    private BigDecimal value;

    public PaymentValue() {}

    public PaymentValue(double value) {
        setValue(value);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("O valor do pagamento não pode ser negativo");
        }
        // 2 casas decimais
        this.value = BigDecimal.valueOf(value).setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

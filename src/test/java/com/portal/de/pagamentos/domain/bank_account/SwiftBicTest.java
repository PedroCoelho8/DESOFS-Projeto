package com.portal.de.pagamentos.domain.bank_account;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("SwiftBic domain value object tests")
class SwiftBicTest {

    private static final String ERROR = "SWIFT/BIC inválido. Deve seguir o padrão internacional (ex: BCOMPTPLXXX)";

    @Nested
    @DisplayName("Invalid values")
    class InvalidValues {

        @Test
        @DisplayName("Should throw exception for null SwiftBic")
        void shouldThrowExceptionForNullSwiftBic() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new SwiftBic(null)
            );
            assertEquals(ERROR, exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for empty SwiftBic")
        void shouldThrowExceptionForEmptySwiftBic() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new SwiftBic("")
            );
            assertEquals(ERROR, exception.getMessage());
        }

        @Test
        @DisplayName("Should throw exception for invalid SwiftBic format")
        void shouldThrowExceptionForInvalidSwiftBicFormat() {
            String[] invalidValues = {
                    "123456789",
                    "BCOM1TPLXXX",
                    "BCOMP",
                    "BCOMPTP$",
                    "BCOMPTPLXXXXXX",
                    "BCOM PTPLXXX"
            };

            for (String invalid : invalidValues) {
                IllegalArgumentException exception = assertThrows(
                        IllegalArgumentException.class,
                        () -> new SwiftBic(invalid),
                        "Valor inválido aceite: " + invalid
                );
                assertEquals(ERROR, exception.getMessage(), "Mensagem de erro incorreta para: " + invalid);
            }
        }
    }

    @Nested
    @DisplayName("Valid values")
    class ValidValues {

        @Test
        @DisplayName("Should accept valid SwiftBic formats")
        void shouldAcceptValidSwiftBicFormats() {
            String[] validValues = {
                    "BCOMPTPLXXX",
                    "DEUTDEFF",
                    "NEDSZAJJ",
                    "DABAIE2D",
                    "UNCRIT2B912"
            };

            for (String valid : validValues) {
                SwiftBic swiftBic = assertDoesNotThrow(() -> new SwiftBic(valid), "Valor válido rejeitado: " + valid);
                assertEquals(valid, swiftBic.getValue());
            }
        }
    }

    @Nested
    @DisplayName("Equality and representation")
    class EqualityAndToString {

        @Test
        @DisplayName("Should be equal when SwiftBic values are the same")
        void shouldBeEqualWhenSwiftBicValuesAreTheSame() {
            String value = "BCOMPTPLXXX";
            SwiftBic s1 = new SwiftBic(value);
            SwiftBic s2 = new SwiftBic(value);
            assertEquals(s1.getValue(), s2.getValue());
        }

        @Test
        @DisplayName("Should not be equal when SwiftBic values are different")
        void shouldNotBeEqualWhenSwiftBicValuesAreDifferent() {
            SwiftBic s1 = new SwiftBic("BCOMPTPLXXX");
            SwiftBic s2 = new SwiftBic("DEUTDEFF");
            assertNotEquals(s1.getValue(), s2.getValue());
        }

        @Test
        @DisplayName("toString should return the raw value")
        void toStringShouldReturnRawValue() {
            String value = "BCOMPTPLXXX";
            SwiftBic swiftBic = new SwiftBic(value);
            assertEquals(value, swiftBic.toString());
        }
    }
}

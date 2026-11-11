package com.formation.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;

class PrixServiceTest {

    private final PrixService service = new PrixService();

    @Test
    void calculerTTC_avecTva20_majoreLePrix() {
        assertEquals(new BigDecimal("120.00"),
            service.calculerTTC(new BigDecimal("100"), new BigDecimal("0.20")));
    }

    // TODO : écrire les tests demandés dans l'énoncé
    @Test
    void calculerTTC_arrondiAuCentime() {
        // 19,99 x 1,055 = 21,08945 -> arrondi à 21,09
        assertEquals(new BigDecimal("21.09"),
            service.calculerTTC(new BigDecimal("19.99"), new BigDecimal("0.055")));
    }

    @Test
    void calculerTTC_prixNegatif_leveUneException() {
        assertThrows(IllegalArgumentException.class,
            () -> service.calculerTTC(new BigDecimal("-1"), new BigDecimal("0.20")));
    }
    @Test
    void appliquerRemise_de25pourcent() {
        assertEquals(new BigDecimal("75.00"),
            service.appliquerRemise(new BigDecimal("100"), 25));
    }
    @Test
    void appliquerRemise_horsBornes_leveUneException() {
        assertThrows(IllegalArgumentException.class,
            () -> service.appliquerRemise(new BigDecimal("100"), 120));
    }
       @Test
   void appliquerRemise_zeroPourcent_laisseLePrixInchange() {
       assertEquals(new BigDecimal("59.90"),
           service.appliquerRemise(new BigDecimal("59.00"), 0));
   }
}

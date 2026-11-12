
package com.formation.demo;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CalculRemiseServiceTest {

    private final CalculRemiseService service =
            new CalculRemiseService();

    @Test
    void montantNegatif() {
        assertEquals(0.0, service.calculerRemise(-100, 30));
    }

    @Test
    void remiseMineur() {
        assertEquals(10.0, service.calculerRemise(100, 16));
    }

    @Test
    void remiseSenior() {
        assertEquals(20.0, service.calculerRemise(100, 70));
    }

    @Test
    void remiseMontantEleve() {
        assertEquals(180.0, service.calculerRemise(1200, 30));
    }

    @Test
    void remiseStandard() {
        assertEquals(5.0, service.calculerRemise(100, 30));
    }
}

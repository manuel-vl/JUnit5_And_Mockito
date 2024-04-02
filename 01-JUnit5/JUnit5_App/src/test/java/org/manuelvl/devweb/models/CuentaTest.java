package org.manuelvl.devweb.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CuentaTest {
    @Test
    void testNombreCuenta() {
        // Arrange
        Cuenta cuenta=new Cuenta("Manuel", new BigDecimal("1200.13456"));

        // Act
        String expected="Manuel";
        String current=cuenta.getPersona();

        // Assert
        assertEquals(expected, current);
    }

    @Test
    void testSaldoCuenta(){
        // Arrange
        Cuenta cuenta=new Cuenta("Manuel", new BigDecimal("1000.12345"));

        // Act
        BigDecimal expected= BigDecimal.valueOf(1000.12345);
        BigDecimal current=cuenta.getSaldo();

        // Assert
        assertEquals(expected, current);
        // Validamos que el saldo no sea menor a cero
        assertFalse(current.compareTo(BigDecimal.ZERO)<0);
        // Validamos que el saldo sea mayor a cero
        assertTrue(current.compareTo(BigDecimal.ZERO)>0);
    }
}
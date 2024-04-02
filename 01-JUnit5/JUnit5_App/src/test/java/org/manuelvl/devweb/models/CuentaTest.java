package org.manuelvl.devweb.models;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.manuelvl.devweb.exceptions.DineroInsuficienteException;

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

    @Test
    void testReferenciaCuenta() {
        // Arrange
        Cuenta cuenta1=new Cuenta("John Doe", new BigDecimal("8900.9987"));
        Cuenta cuenta2=new Cuenta("John Doe", new BigDecimal("8900.9987"));

        // Act & Assert
        assertEquals(cuenta2, cuenta1);
    }

    @Test
    void testDebitoCuenta() {
        // Arrange
        Cuenta cuenta=new Cuenta("Manuel", new BigDecimal("1000.1234"));

        // Act
        Integer expectedInt= 900;
        String expectedBig="900.1234";
        cuenta.debito(new BigDecimal(100));

        // Assert
        assertNotNull(cuenta.getSaldo());
        assertEquals(expectedInt, cuenta.getSaldo().intValue());
        assertEquals(expectedBig, cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta(){
        // Arrange
        Cuenta cuenta=new Cuenta("Manuel", new BigDecimal("1000.1234"));

        // Act
        Integer expectedInt= 1100;
        String expectedBig="1100.1234";
        cuenta.credito(new BigDecimal(100));

        // Assert
        assertNotNull(cuenta.getSaldo());
        assertEquals(expectedInt, cuenta.getSaldo().intValue());
        assertEquals(expectedBig, cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteException(){
        // Arrange
        Cuenta cuenta=new Cuenta("Manuel", new BigDecimal("1000.1234"));

        // Act & Assert
        Exception exception= assertThrows(DineroInsuficienteException.class, ()->{
            // Como no tenemos ese monto, se debe lanzar excepcion
           cuenta.debito(new BigDecimal(1500));
        });

        String current=exception.getMessage();
        String expected="Dinero Insuficiente";

        assertEquals(expected, current);
    }
}
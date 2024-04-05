package org.manuelvl.devweb.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.manuelvl.devweb.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;

class CuentaTest {
    @Test
    // Disable deshabilita la prueba
    @Disabled
    @DisplayName("Test para probar el nombre de la cuenta")
    void testNombreCuenta() {
        // Arrange
        Cuenta cuenta=new Cuenta("Manuel", new BigDecimal("1200.13456"));
        String expected="Manuel";

        // Act
        String current=cuenta.getPersona();

        // Assert
        assertNotNull(current, ()->"La cuenta no puede ser nula");
        assertEquals(expected, current, ()->"El nombre de la cuenta no es el esperado");
        assertTrue(current.equals(expected),()-> "El nombre de la cuenta debe ser igual al esperado");
    }

    @Test
    @DisplayName("Test para probar el saldo de la cuenta")
    void testSaldoCuenta(){
        // Arrange
        Cuenta cuenta=new Cuenta("Manuel", new BigDecimal("1000.12345"));
        BigDecimal expected= BigDecimal.valueOf(1000.12345);

        // Act
        BigDecimal current=cuenta.getSaldo();

        // Assert
        assertEquals(expected, current);
        // Validamos que el saldo no sea menor a cero
        assertFalse(current.compareTo(BigDecimal.ZERO)<0);
        // Validamos que el saldo sea mayor a cero
        assertTrue(current.compareTo(BigDecimal.ZERO)>0);
    }

    @Test
    @DisplayName("Test para probar si dos cuentas son iguales")
    void testReferenciaCuenta() {
        // Arrange
        Cuenta cuenta1=new Cuenta("John Doe", new BigDecimal("8900.9987"));
        Cuenta cuenta2=new Cuenta("John Doe", new BigDecimal("8900.9987"));

        // Act & Assert
        assertEquals(cuenta2, cuenta1);
    }

    @Test
    @DisplayName("Test para probar el debito a una cuenta")
    void testDebitoCuenta() {
        // Arrange
        Cuenta cuenta=new Cuenta("Manuel", new BigDecimal("1000.1234"));
        Integer expectedInt= 900;
        String expectedBig="900.1234";

        // Act
        cuenta.debito(new BigDecimal(100));

        // Assert
        assertNotNull(cuenta.getSaldo());
        assertEquals(expectedInt, cuenta.getSaldo().intValue());
        assertEquals(expectedBig, cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Test para probar el credito a una cuenta")
    void testCreditoCuenta(){
        // Arrange
        Cuenta cuenta=new Cuenta("Manuel", new BigDecimal("1000.1234"));
        Integer expectedInt= 1100;
        String expectedBig="1100.1234";

        // Act
        cuenta.credito(new BigDecimal(100));

        // Assert
        assertNotNull(cuenta.getSaldo());
        assertEquals(expectedInt, cuenta.getSaldo().intValue());
        assertEquals(expectedBig, cuenta.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Test para probar que se lance la excepcion de Dinero Insuficiente")
    void testDineroInsuficienteException(){
        // Arrange
        Cuenta cuenta=new Cuenta("Manuel", new BigDecimal("1000.1234"));
        String expected="Dinero Insuficiente";

        // Act & Assert
        Exception exception= assertThrows(DineroInsuficienteException.class, ()->{
            // Como no tenemos ese monto, se debe lanzar excepcion
           cuenta.debito(new BigDecimal(1500));
        });

        String current=exception.getMessage();

        assertEquals(expected, current);
    }

    @Test
    @DisplayName("Test para probar la transferencia de dinero entre cuentas")
    void testTransferirDineroCuentas() {
        // Arrange
        Cuenta cuentaOrigen=new Cuenta("Manuel", new BigDecimal("2500"));
        Cuenta cuentaDestino=new Cuenta("Manuel", new BigDecimal("2000"));
        Banco banco=new Banco();

        // Act
        banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal(500));

        // Assert
        assertEquals("2000", cuentaOrigen.getSaldo().toPlainString());
        assertEquals("2500", cuentaDestino.getSaldo().toPlainString());
    }

    @Test
    @DisplayName("Test para probar las relaciones entre banco y cuentas, cuenta y banco")
    void testRelacionBancoCuentas(){
        // Arrange
        Cuenta cuenta1=new Cuenta("Manuel", new BigDecimal("2500"));
        Cuenta cuenta2=new Cuenta("Valencia", new BigDecimal("2000"));
        Banco banco=new Banco();
        banco.setNombre("Bancolombia");

        // Act
        banco.addCuenta(cuenta1);
        banco.addCuenta(cuenta2);

        // Assert
        assertAll(
                ()->assertEquals(2, banco.getCuentas().size()),
                ()-> assertEquals("Bancolombia", cuenta1.getBanco().getNombre(), ()-> "El Banco debe ser igual a Bancolombia"),
                ()-> assertEquals("Manuel", banco.getCuentas().stream()
                            .filter(cuenta -> cuenta.getPersona().equalsIgnoreCase("Manuel"))
                            .findFirst()
                            .get().getPersona()),
                ()-> assertTrue(banco.getCuentas().stream()
                            .anyMatch(cuenta -> cuenta.getPersona().equalsIgnoreCase("Manuel")))
        );
    }
}
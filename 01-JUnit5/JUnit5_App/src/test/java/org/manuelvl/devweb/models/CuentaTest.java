package org.manuelvl.devweb.models;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.manuelvl.devweb.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Properties;

class CuentaTest {
    Cuenta cuenta;

    // BeforeAll -> Se ejecuta antes de todos los metodos una sola vez
    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test.");
    }

    // AfterAll -> Se ejecuta al final de las pruebas una sola vez
    @AfterAll
    static void afterAll() {
        System.out.println("Finalizando el test.");
    }

    // BeforeEach -> Se ejecuta una vez antes de cada metodo
    @BeforeEach
    void initMethodTest(){
        this.cuenta=new Cuenta("Manuel", new BigDecimal("1200.12345"));
        System.out.println("Iniciando metodo.");
    }

    // AfterEach -> Se ejecuta una vez despues de cada metodo
    @AfterEach
    void tearDown(){
        System.out.println("Finalizando metodo.");
    }

    // Nested nos permite organizar nuestros tests dentro de una clase anidada Inner Class
    @Nested
    @DisplayName("Test de los metodos para validar el nombre y saldo de la cuenta")
    class CuentaTestNombreSaldo{
        @Test
        // Disable deshabilita la prueba
        @Disabled
        @DisplayName("Test para probar el nombre de la cuenta")
        void testNombreCuenta() {
            // Arrange
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
            BigDecimal expected= BigDecimal.valueOf(1200.12345);

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
    }

    // Nested nos permite organizar nuestros tests dentro de una clase anidada Inner Class
    @Nested
    class CuentaOperacionesTest{
        @Test
        @DisplayName("Test para probar el debito a una cuenta")
        void testDebitoCuenta() {
            // Arrange
            Integer expectedInt= 1100;
            String expectedBig="1100.12345";

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
            Integer expectedInt= 1300;
            String expectedBig="1300.12345";

            // Act
            cuenta.credito(new BigDecimal(100));

            // Assert
            assertNotNull(cuenta.getSaldo());
            assertEquals(expectedInt, cuenta.getSaldo().intValue());
            assertEquals(expectedBig, cuenta.getSaldo().toPlainString());
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
    }


    @Test
    @DisplayName("Test para probar que se lance la excepcion de Dinero Insuficiente")
    void testDineroInsuficienteException(){
        // Arrange
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

    // Nested nos permite organizar nuestros tests dentro de una clase anidada Inner Class
    @Nested
    class SistemaOperativoTest{
        @Test
        // Condicional para que solo se ejecute en OS Windows
        @EnabledOnOs(OS.WINDOWS)
        void testOnlyWindows(){
            System.out.println("Test solo windows");
        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testOnlyMacLinux() {
            System.out.println("Test solo en Mac");
        }

        @Test
        // Condicional para que no se ejecute en Windows
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows(){
            System.out.println("No se ejecuta en Windows");
        }
    }

    // Nested nos permite organizar nuestros tests dentro de una clase anidada Inner Class
    @Nested
    class JavaVersionTest{
        @Test
        // Condicional para que solo se ejecute en Java JDK 8
        @EnabledOnJre(JRE.JAVA_8)
        void onlyJDK8() {
            System.out.println("Test solo Java 8");
        }
    }

    // Nested nos permite organizar nuestros tests dentro de una clase anidada Inner Class
    @Nested
    class SystemPropertiesTest{
        @Test
        void showProperties(){
            // Obteniendo las propiedades del computador
            Properties properties=System.getProperties();
            properties.forEach((k,v)-> System.out.println(k + ":"+v));
        }

        @Test
        // Condicional para validar si coincide la propiedad del sistema indicada
        @EnabledIfSystemProperty(named="java.specification.version", matches = "17")
        void testPropertyJavaVersion(){
            System.out.println("Test version 17");
        }

        @Test
        @EnabledIfSystemProperty(named = "user.country", matches = "CO")
        void testUserCountry(){
            System.out.println("Test user country");
        }
    }

    // Nested nos permite organizar nuestros tests dentro de una clase anidada Inner Class
    @Nested
    class VariablesAmbienteTest{
        @Test
        void showEnvironments(){
            // Obtenemos las environments
            Map<String, String> getenv=System.getenv();
            getenv.forEach((k,v)-> System.out.println(k+" = "+v));
        }

        @Test
        // Condicional para validar si coincide o hace match la variable de entorno
        @EnabledIfEnvironmentVariable(named = "USER", matches = "cvalencialon")
        void testLogname(){
            System.out.println(System.getenv());
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
        void testNumberProcessors(){
            System.out.println("8");
        }

        @Test
        // Para que el test pase agregar CUSTOM_ENVIRONMENT a las variables de entorno del proyecto
        @EnabledIfEnvironmentVariable(named = "CUSTOM_ENVIRONMENT", matches = "dev")
        void testEnvironment(){
            System.out.println("Probando enviroments custom");
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "CUSTOM_ENVIRONMENT", matches = "dev")
        void testNoDevEnvironment(){
            System.out.println("No se ejecuta en dev");
        }
    }

    @Test
    @DisplayName("Test para probar el saldo de la cuenta, solo en dev")
    void testSaldoCuentaDev(){
        // Arrange
        // Validando que tengamos una propiedad ENV con valor dev, agregar en las configs del proyecto
        boolean isDev="dev".equals(System.getProperty("ENV"));
        BigDecimal expected= BigDecimal.valueOf(1200.12345);

        // Act
        BigDecimal current=cuenta.getSaldo();

        // Assert
        // Asumme true valida que la condicion indicada sea true para poder ejecutar el resto del test
        assumeTrue(isDev);
        assertEquals(expected, current);
        // Validamos que el saldo no sea menor a cero
        assertFalse(current.compareTo(BigDecimal.ZERO)<0);
        // Validamos que el saldo sea mayor a cero
        assertTrue(current.compareTo(BigDecimal.ZERO)>0);
    }

    @Test
    @DisplayName("Test para probar el saldo de la cuenta, solo en dev 2")
    void testSaldoCuentaDev2(){
        // Arrange
        // Validando que tengamos una propiedad ENV con valor dev, agregar en las configs del proyecto
        boolean isDev="dev".equals(System.getProperty("ENV"));
        boolean isStage="stage".equals(System.getProperty("ENV"));
        BigDecimal expected= BigDecimal.valueOf(1200.12345);

        // Act
        BigDecimal current=cuenta.getSaldo();

        // Assert
        // Asumme that recibe el valor que debe ser true, y los metodos que se ejecutaran en caso de que la condicion sea true
        assumingThat(isDev, ()->{
            System.out.println("Es dev");
            assertEquals(expected, current);
            // Validamos que el saldo no sea menor a cero
            assertFalse(current.compareTo(BigDecimal.ZERO)<0);
            // Validamos que el saldo sea mayor a cero
            assertTrue(current.compareTo(BigDecimal.ZERO)>0);
        });
        assumingThat(isStage, ()->{
            assertTrue(true);
        });
        System.out.println("Ejecutando algo luego independiente del assumingThat");
        assertTrue(true);
    }

    // Permite repetir el test las veces que indiquemos y tambien podremos agregar el nombre, numero de repeticiones, etc.
    // Se suele usar cuando tenemos algun tipo de aleatoriedad o randoms.
    @RepeatedTest(value = 5, name="{displayName} - Repeticion numero {currentRepetition} de {totalRepetitions}")
    @DisplayName("Test para probar el debito a una cuenta, 5 veces")
    void testRepetirDebitoCuenta(RepetitionInfo info) {
        if(info.getCurrentRepetition()==3){
            System.out.println("Este es el test #3");
        }

        // Arrange
        Integer expectedInt= 1100;
        String expectedBig="1100.12345";

        // Act
        cuenta.debito(new BigDecimal(100));

        // Assert
        assertNotNull(cuenta.getSaldo());
        assertEquals(expectedInt, cuenta.getSaldo().intValue());
        assertEquals(expectedBig, cuenta.getSaldo().toPlainString());
    }

    // Permite repetir la prueba pero con datos de entrada diferentes
    @ParameterizedTest(name = "Numero {index} ejecutando con el valor {argumentsWithNames}")
    // Pasamos un array de datos
    @ValueSource(doubles = {100,300,500,700,1000})
    @DisplayName("Test para probar el debito a una cuenta con diferentes valores")
    void testDebitoCuenta(double monto) {
        // Arrange

        // Act
        cuenta.debito(new BigDecimal(monto));

        // Assert
        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO)>0);
    }

}
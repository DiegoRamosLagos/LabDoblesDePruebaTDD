package tais;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClienteTest {

    private final static String PRESUPUESTO_NO_VIGENTE_EXCEPTION_MSG = "Presupuesto no vigente";
    private final static String PRESUPUESTO_INEXISTENTE_EXCEPTION_MSG = "Presupuesto inexistente";
    private final static String ITEM_DUPLICADO_EXCEPTION_MSG = "Item duplicado";
    private final static String ITEM_INEXISTENTE_EXCEPTION_MSG = "Item inexistente";

    @Mock
    Presupuesto presupuesto;
    @InjectMocks
    Cliente cliente;
    @Test // Ej.1
    void siSeInvocaObtieneTotalPresupuestoYElClientePoseePresupuestoVigenteSeDebePoderObtenerSuMontoTotal()
            throws Exception {
        // Arrange
        long montoObtenido;
        when(presupuesto.estaVigente()).thenReturn(true);
        when(presupuesto.obtieneMontoTotal()).thenReturn((long)250500);
        // Act
        montoObtenido = cliente.obtieneTotalPresupuesto(); // Lanza excepción de acuerdo a lo programado en Ej.2
        // Assert
        assertEquals(250500,montoObtenido);
    }
    @Test // Ej.2
    void siSeInvocaObtieneTotalPresupuestoYElClientePoseePrespuestoNoVigenteSeDebeRecibirUnaExcepcion() {
        // Arrange
        Exception exception;
        when(presupuesto.estaVigente()).thenReturn(false);
        // Act + Assert
        exception = assertThrows(PresupuestoInvalidoException.class, ()->cliente.obtieneTotalPresupuesto());
        assertEquals(PRESUPUESTO_NO_VIGENTE_EXCEPTION_MSG, exception.getMessage());
    }

    @Test // Ej.3
    void siSeInvocaObtieneTotalPresupuestoYElClienteNoTienePresupuestoSeDebeRecibirUnaExcepcion() {
        // Arrange
        Exception exception;
        cliente.setPresupuesto(null); // Se programa el cliente asociándole NINGÚN presupuesto
        // Act + Assert
        exception = assertThrows(PresupuestoInvalidoException.class, ()->cliente.obtieneTotalPresupuesto());
        assertEquals(PRESUPUESTO_INEXISTENTE_EXCEPTION_MSG, exception.getMessage());
    }
    @Test // Ej.4
    void siSeInvocaAgregaItemPresupuestoYElClientePoseePresupuestoVigenteSeDebePoderAgregarItem() throws PresupuestoInvalidoException, ItemInvalidoException {
        // Arrange
        String descripcion = "Arreglos florales";
        long monto = 95000;
        lenient().when(presupuesto.estaVigente()).thenReturn(true);
        // Act
        cliente.agregaItemPresupuesto(descripcion,monto);
        // Assert
        verify(presupuesto).agregaItem(descripcion,monto);
        // También pudo ser: verify(presupuesto,times(1)).agregaItem(descripcion,monto); ¿Cuál es la diferencia?
    }

    @Test // Ej.5
    void siSeInvocaAgregaItemPresupuestoYElClientePoseePresupuestoNoVigenteSeDebeRecibirUnaExcepcion() {
        // Arrange
        Exception exception;
        String descripcion = "Arreglos florales";
        long monto = 95000;
        lenient().when(presupuesto.estaVigente()).thenReturn(false);
        cliente.setPresupuesto(null);
        // Act
        exception = assertThrows(PresupuestoInvalidoException.class, ()->cliente.agregaItemPresupuesto(descripcion, monto));
        // Assert
        assertEquals(PRESUPUESTO_INEXISTENTE_EXCEPTION_MSG, exception.getMessage());
    }

    @Test // Ej.6
    void siSeInvocaAgregaItemPresupuestoYElClienteNoPoseePresupuestoSeDebeRecibirUnaExcepcion() {
        // Arrange
        Exception exception;
        String descripcion = "Arreglos florales";
        long monto = 95000;
        lenient().when(presupuesto.estaVigente()).thenReturn(false);
        // Act
        exception = assertThrows(PresupuestoInvalidoException.class, ()->cliente.agregaItemPresupuesto(descripcion, monto));
        // Assert
        assertEquals(PRESUPUESTO_NO_VIGENTE_EXCEPTION_MSG, exception.getMessage());
    }

    @Test // Ej.7
    @DisplayName("EJ 7")
    void siSeInvocaAgregaItemPresupuestoYElClientePoseePresupuestoVigenteYelIElItemExisteSeDebeRecibirUnaExcepcion() throws PresupuestoInvalidoException, ItemInvalidoException {
        // Arrange
        Exception exception;
        String descripcion = "Arreglos florales";
        long monto = 95000;
        when(presupuesto.estaVigente()).thenReturn(true);
        doThrow(ItemInvalidoException.class).when(presupuesto)
                .agregaItem(descripcion, monto);
        // Act
        exception = assertThrows(ItemInvalidoException.class, () -> cliente.agregaItemPresupuesto(descripcion, monto));

        // Assert
        assertEquals(ITEM_DUPLICADO_EXCEPTION_MSG, exception.getMessage());
        verify(presupuesto).estaVigente();
        verify(presupuesto).agregaItem(anyString(), anyLong());
    }

    @Test // Ej.8
    void siSeInvocaEliminaItemPresupuestoYClientePoseePresupuestoVigenteYTieneItemsSeDebeEliminarElItem() throws PresupuestoInvalidoException, ItemInvalidoException {
        // Arrange
        int nroItem = 1;
        when(presupuesto.obtieneNroItems()).thenReturn(1);
        lenient().when(presupuesto.estaVigente()).thenReturn(true);
        // Act
        // cliente.agregaItemPresupuesto(  "prueba", 10);
        cliente.eliminaItemPresupuesto(nroItem);
        // Assert
        verify(presupuesto).eliminaItem(nroItem);
    }

    @Test // Ej.9
    void siSeInvocaEliminaItemPresupuestoYClientePoseePresupuestoNoVigenteSeDebeRecibirUnaExcepcion() {
        // Arrange
        int nroItem = 1;
        Exception exception;
        when(presupuesto.estaVigente()).thenReturn(false);
        // Act
        exception = assertThrows(PresupuestoInvalidoException.class, ()->
                cliente.eliminaItemPresupuesto(nroItem));
        // Assert
        assertEquals(PRESUPUESTO_NO_VIGENTE_EXCEPTION_MSG, exception.getMessage());
    }

    @Test // Ej.10
    void siSeInvocaEliminaItemPresupuestoYClienteNoPoseePresupuestoSeDebeRecibirUnaExcepcion() {
        // Arrange
        int nroItem = 1;
        Exception exception;
        cliente.setPresupuesto(null);
        // Act
        exception = assertThrows(PresupuestoInvalidoException.class,
                ()->cliente.eliminaItemPresupuesto(nroItem));
        // Assert
        assertEquals(PRESUPUESTO_INEXISTENTE_EXCEPTION_MSG, exception.getMessage());
    }

    @Test // Ej.11
    void siSeInvocaEliminaItemPresupuestoYClientePoseePresupuestoVigentePeroNroItemNoExisteSeDebeRecibirUnaExcepcion() throws PresupuestoInvalidoException, ItemInvalidoException {
        // Arrange
        int nroItem = 2;
        Exception exception;
        when(presupuesto.estaVigente()).thenReturn(true);
        when(presupuesto.obtieneNroItems()).thenReturn(1);
        doThrow(ItemInvalidoException.class).when(presupuesto).eliminaItem(nroItem);
        // Act
        exception = assertThrows(ItemInvalidoException.class, ()->
                cliente.eliminaItemPresupuesto(nroItem));
        // Assert
        verify(presupuesto).eliminaItem(nroItem);
        assertEquals(ITEM_INEXISTENTE_EXCEPTION_MSG, exception.getMessage());
    }

    @Test // Ej.12
    void siSeInvocaObtieneItemsPresupuestoYElClientePoseePresupuestoVigenteSeDebePoderObtenerSusItems() throws PresupuestoInvalidoException {
        // Arrange
        String[] item1 = {"Arreglos florales", "95000"};
        String[] item2 = {"Arriendo local recepcion", "129000"};
        String[][] items;
        when(presupuesto.estaVigente()).thenReturn(true);
        when(presupuesto.obtieneNroItems()).thenReturn(2);
        when(presupuesto.obtieneItem(anyInt())).thenReturn(item1, item2);
        // Act
        items = cliente.obtieneItemsPresupuesto();
        // Assert
        assertNotNull(items);
        assertAll("items",
                () -> assertEquals("Arreglos florales",items[0][0]),
                () -> assertEquals("95000",items[0][1]),
                () -> assertEquals("Arriendo local recepcion",items[1][0]),
                () -> assertEquals("129000",items[1][1])
        );
        verify(presupuesto).estaVigente();
        verify(presupuesto).obtieneNroItems();
        verify(presupuesto, times(2)).obtieneItem(anyInt());
    }

    @Test // Ej.13
    void siSeInvocaObtieneItemsPresupuestoYElClientePoseePresupuestoNoVigenteSeDebeRecibirUnaExcepcion() {
        // Arrange
        Exception exception;
        when(presupuesto.estaVigente()).thenReturn(false);
        // Act
        exception = assertThrows(PresupuestoInvalidoException.class,
                ()-> cliente.obtieneItemsPresupuesto());
        // Assert
        assertEquals(PRESUPUESTO_NO_VIGENTE_EXCEPTION_MSG, exception.getMessage());
    }

    @Test // Ej.14
    void siSeInvocaObtieneItemsPresupuestoYElClienteNpoPoseePresupuestoSeDebeRecibirUnaExcepcion() throws PresupuestoInvalidoException {
        // Arrange
        Exception exception;
        cliente.setPresupuesto(null);
        // Act
        exception = assertThrows(PresupuestoInvalidoException.class,
                ()-> cliente.obtieneItemsPresupuesto());
        // Assert
        assertEquals(PRESUPUESTO_INEXISTENTE_EXCEPTION_MSG, exception.getMessage());
    }

    @Test
    @DisplayName("EJERCICIO 15")
    void siSeInvocaObtieneItemsPresupuestoYElClientePoseePresupuestoVigenteYNoTieneItemsDebeRetornarNull() throws PresupuestoInvalidoException {
        when(presupuesto.estaVigente()).thenReturn(true);

        assertNull(cliente.obtieneItemsPresupuesto());
    }
}

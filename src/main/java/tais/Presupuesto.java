package tais;

public class Presupuesto {
    public boolean estaVigente() {
        return false;
    }

    public Object obtieneMontoTotal() {
        return null;
    }

    public void agregaItem(String descripcion, long monto) throws ItemInvalidoException{

    }


    public void eliminaItem(int nroItem) throws ItemInvalidoException {

    }

    public int obtieneNroItems() {
        return 0;
    }

    public String[] obtieneItem(int anyInt) {
        return null;
    }
}

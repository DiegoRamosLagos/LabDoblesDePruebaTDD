package tais;

public class Cliente {

    private Presupuesto presupuesto;
    public long obtieneTotalPresupuesto() throws PresupuestoInvalidoException {
        if (this.presupuesto == null) throw new PresupuestoInvalidoException("Presupuesto inexistente");
        if (!this.presupuesto.estaVigente())
            throw new PresupuestoInvalidoException("Presupuesto no vigente");
        return (long) this.presupuesto.obtieneMontoTotal();
    }

    public void setPresupuesto(Presupuesto presupuesto) {
        this.presupuesto = presupuesto;
    }

    public void agregaItemPresupuesto(String descripcion, long monto) throws PresupuestoInvalidoException, ItemInvalidoException {
        if (this.presupuesto == null)
            throw new PresupuestoInvalidoException("Presupuesto inexistente");
        if (!this.presupuesto.estaVigente())
            throw new PresupuestoInvalidoException("Presupuesto no vigente");
        try {
            this.presupuesto.agregaItem(descripcion, monto);
        }catch (ItemInvalidoException e) {
            throw new ItemInvalidoException("Item duplicado");
        }
    }

    public void eliminaItemPresupuesto(int nroItem) throws PresupuestoInvalidoException, ItemInvalidoException {
        if (presupuesto == null)
            throw new PresupuestoInvalidoException("Presupuesto inexistente");
        if (!presupuesto.estaVigente())
            throw new PresupuestoInvalidoException("Presupuesto no vigente");
        if (presupuesto.obtieneNroItems() > 0){
            try{
                presupuesto.eliminaItem(nroItem);
            }catch (ItemInvalidoException e){
                throw new ItemInvalidoException("Item inexistente");
            }
        }

    }

    public String[][] obtieneItemsPresupuesto() throws PresupuestoInvalidoException {
        if (presupuesto == null)
            throw new PresupuestoInvalidoException("Presupuesto inexistente");
        if (!presupuesto.estaVigente())
            throw new PresupuestoInvalidoException("Presupuesto no vigente");

        int cantItems = presupuesto.obtieneNroItems();
        if (cantItems == 0)
            return null;
        String[][]items = new String[2][2];

        for (int i = 0; i < cantItems; i++) {
            items[i] = presupuesto.obtieneItem(i);
        }

        return items;
    }
}

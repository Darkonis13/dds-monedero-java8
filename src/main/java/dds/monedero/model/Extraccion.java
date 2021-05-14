package dds.monedero.model;

import java.time.LocalDate;

public class Extraccion extends Movimiento{
    public Extraccion(LocalDate fecha, double monto) {
        super(fecha, monto);
    }

    public boolean esDeFecha(LocalDate fecha){
        return getFecha() == fecha;
    }

    @Override
    public void agregarMovimientoA(Cuenta cuenta) {
        super.agregarMovimientoA(cuenta);
        cuenta.agregarExtraccion(this);
    }

    @Override
    public double calcularValor(Cuenta cuenta) {
        return cuenta.getSaldo() - getMonto();
    }
}

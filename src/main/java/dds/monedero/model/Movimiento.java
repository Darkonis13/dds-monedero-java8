package dds.monedero.model;

import java.time.LocalDate;

public class Movimiento {
  private final LocalDate fecha;
  private final double monto;

  public Movimiento(LocalDate fecha, double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public double getMonto() {
    return monto;
  }

  public boolean esDeFecha(LocalDate fecha){
    return getFecha() == fecha;
  }

  public LocalDate getFecha() {
    return fecha;
  }
}

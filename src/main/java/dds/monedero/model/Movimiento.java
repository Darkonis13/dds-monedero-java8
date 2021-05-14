package dds.monedero.model;

import java.time.LocalDate;

public abstract class Movimiento {
  private final LocalDate fecha;
  private final double monto;

  public Movimiento(LocalDate fecha, double monto) {
    this.fecha = fecha;
    this.monto = monto;
  }

  public double getMonto() {
    return monto;
  }

  public LocalDate getFecha() {
    return fecha;
  }

  public void agregarMovimientoA(Cuenta cuenta) {
    cuenta.setSaldo(calcularValor(cuenta));
  }

  public double calcularValor(Cuenta cuenta) {
      return cuenta.getSaldo() + getMonto();
  }
}

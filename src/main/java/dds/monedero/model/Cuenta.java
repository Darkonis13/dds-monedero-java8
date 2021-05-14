package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cuenta {

  private double saldo;
  private final List<Movimiento> movimientos = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void agregarMovimiento(Movimiento movimiento) {
    this.poner(movimiento.getMonto());
    this.movimientos.add(movimiento);
  }

  public List<Movimiento> getMovimientos() {
    return movimientos;
  }

  public void poner(double cuanto) {
    verificarMontoNegativo(cuanto);
    verificarCantidadDepositos(cuanto);
    Movimiento movimiento = new Movimiento(LocalDate.now(), cuanto);
    agregarMovimiento(movimiento);
  }

  public void verificarCantidadDepositos(double cuanto){
    if (cuanto >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }
  }

  public void verificarMontoNegativo(double cuanto){ //Este método no se usa nunca
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }
  }

  public void verificarExtraccion(double cuanto){
    verificarMontoNegativo(cuanto);
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
  }

  public void sacar(double cuanto) {
    verificarExtraccion(cuanto);
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    Movimiento movimiento = new Movimiento(LocalDate.now(), cuanto);
    agregarMovimiento(movimiento);
  }

  public double getMontoExtraidoA(LocalDate fecha) {
    return sumarExtracciones(getExtraccionesDeLaFecha(fecha));
  }

  public double sumarExtracciones(List <Movimiento> extracciones){
    return extracciones.stream()
            .mapToDouble(Movimiento::getMonto)
            .sum();
  }

  public List<Movimiento> getExtraccionesDeLaFecha(LocalDate fecha){
    return getMovimientos().stream()
            .filter(extraccion -> extraccion.esDeFecha(fecha))
            .collect(Collectors.toList());
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

}

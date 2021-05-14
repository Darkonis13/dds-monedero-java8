package dds.monedero.model;

import dds.monedero.exceptions.MaximaCantidadDepositosException;
import dds.monedero.exceptions.MaximoExtraccionDiarioException;
import dds.monedero.exceptions.MontoNegativoException;
import dds.monedero.exceptions.SaldoMenorException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Cuenta {

  private double saldo = 0;
  private List<Deposito> depositos = new ArrayList<>();
  private List<Extraccion> extracciones = new ArrayList<>();

  public Cuenta() {
    saldo = 0;
  }

  public Cuenta(double montoInicial) {
    saldo = montoInicial;
  }

  public void agregarExtraccion(Extraccion extraccion){
    this.extracciones.add(extraccion);
  }

  public void agregarDeposito(Deposito deposito) {
    this.depositos.add(deposito);
  }

  public List<Extraccion> getExtracciones() {
    return extracciones;
  }

  public List<Deposito> getDepositos() {
    return depositos;
  }

  public void setExtracciones(List<Extraccion> extracciones) {
    this.extracciones = extracciones;
  }

  public void poner(double cuanto) {
    if (getDepositos().size() >= 3) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios");
    }

    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    new Deposito(LocalDate.now(), cuanto).agregarMovimientoA(this);
  }

  public void verificarMonto(double cuanto, int limiteExtraccion){ //Este método no se usa nunca
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    }

    if (cuanto >= limiteExtraccion) {
      throw new MaximaCantidadDepositosException("Ya excedio los " + 3 + " depositos diarios"); //el limiteExtraccion está harcodeado en la exception
    }
  }



  public void sacar(double cuanto) {
    if (cuanto <= 0) {
      throw new MontoNegativoException(cuanto + ": el monto a ingresar debe ser un valor positivo");
    } //Duplicated code (igual a la linea 40, se puede abstraer el método que checkea)
    if (getSaldo() - cuanto < 0) {
      throw new SaldoMenorException("No puede sacar mas de " + getSaldo() + " $");
    }
    double montoExtraidoHoy = getMontoExtraidoA(LocalDate.now());
    double limite = 1000 - montoExtraidoHoy;
    if (cuanto > limite) {
      throw new MaximoExtraccionDiarioException("No puede extraer mas de $ " + 1000
          + " diarios, límite: " + limite);
    }
    new Extraccion(LocalDate.now(), cuanto).agregarMovimientoA(this);
  }//Long method (se puede extraer en más de un método)

  public double getMontoExtraidoA(LocalDate fecha) {
    return getExtracciones().stream()
        .filter(extraccion -> extraccion.getFecha().equals(fecha))
        .mapToDouble(Movimiento::getMonto)
        .sum(); //Feature Envy
  }

  public List<Movimiento> getMovimientos() {
    List<Movimiento> movimientos = new ArrayList<>();
    movimientos.addAll(extracciones);
    movimientos.addAll(depositos);
    return movimientos;
  }

  public double getSaldo() {
    return saldo;
  }

  public void setSaldo(double saldo) {
    this.saldo = saldo;
  }

} //Long Method (Faltan abstracciones que eliminen la baja cohesión del código actual)

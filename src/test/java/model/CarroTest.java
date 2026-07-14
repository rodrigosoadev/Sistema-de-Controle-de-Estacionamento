package test.java.model;

import java.time.LocalDateTime;

import model.Carro;

public class CarroTest {
    public static void main(String[] args) {
        Carro carro = new Carro("ABC-1234", "Gol", LocalDateTime.now());

        double tarifa = carro.calcularTarifa(60);
        if (tarifa != 10.8) {
            throw new AssertionError("Tarifa esperada com desconto foi 10.8, mas foi " + tarifa);
        }

        double desconto = carro.aplicarDesconto(100.0);
        if (desconto != 90.0) {
            throw new AssertionError("Desconto esperado foi 90.0, mas foi " + desconto);
        }

        System.out.println("CarroTest ok");
    }
}

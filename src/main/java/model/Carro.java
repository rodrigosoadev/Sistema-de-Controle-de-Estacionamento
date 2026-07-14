package model;

import java.time.LocalDateTime;

public class Carro extends Veiculo implements Descontavel {
    private static final double TAXA_POR_MINUTO = 0.20;
    private static final double DESCONTO_FIDELIDADE = 0.10;

    public Carro(String placa, String modelo, LocalDateTime horaEntrada) {
        super(placa, modelo, horaEntrada);
    }

    @Override
    public double calcularTarifa(long minutos) {
        return aplicarDesconto(minutos * TAXA_POR_MINUTO);
    }

    @Override
    public double aplicarDesconto(double valorOriginal) {
        return valorOriginal - (valorOriginal * DESCONTO_FIDELIDADE);
    }
}

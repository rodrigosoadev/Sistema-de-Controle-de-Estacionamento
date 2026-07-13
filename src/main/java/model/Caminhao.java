package model;

import java.time.LocalDateTime;

public class Caminhao extends Veiculo {
    private static final double TAXA_POR_MINUTO = 0.50;

    public Caminhao(String placa, String modelo, LocalDateTime horaEntrada) {
        super(placa, modelo, horaEntrada);
    }

    @Override
    public double calcularTarifa(long minutos) {
        return minutos * TAXA_POR_MINUTO;
    }
}

package model;

import java.time.LocalDateTime;

public class Moto extends Veiculo {
    private static final double TAXA_POR_MINUTO = 0.10;

    public Moto(String placa, String modelo, LocalDateTime horaEntrada) {
        super(placa, modelo, horaEntrada);
    }

    @Override
    public double calcularTarifa(long minutos) {
        return minutos * TAXA_POR_MINUTO;
    }
}

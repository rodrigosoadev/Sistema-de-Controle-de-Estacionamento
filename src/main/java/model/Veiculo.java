package model;

import java.time.LocalDateTime;

public abstract class Veiculo {
    private final String placa;
    private final String modelo;
    private final LocalDateTime horaEntrada;

    public Veiculo(String placa, String modelo, LocalDateTime horaEntrada) {
        this.placa = placa;
        this.modelo = modelo;
        this.horaEntrada = horaEntrada;
    }

    public String getPlaca() {
        return placa;
    }

    public String getModelo() {
        return modelo;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public abstract double calcularTarifa(long minutos);
}

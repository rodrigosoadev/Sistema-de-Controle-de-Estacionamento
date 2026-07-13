package model;

import java.time.LocalDateTime;

public abstract class Veiculo {
    private String placa;
    private String modelo;
    private LocalDateTime horaEntrada;

    public Veiculo(String placa, String modelo, LocalDateTime horaEntrada) {
        this.placa = placa;
        this.modelo = modelo;
        this.horaEntrada = horaEntrada;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public LocalDateTime getHoraEntrada() {
        return horaEntrada;
    }

    public void setHoraEntrada(LocalDateTime horaEntrada) {
        this.horaEntrada = horaEntrada;
    }

    public abstract double calcularTarifa(long minutos);
}

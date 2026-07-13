package model;

import java.time.LocalDateTime;

public record Ticket(String placa, LocalDateTime horaEntrada, String tipoVeiculo) {
}

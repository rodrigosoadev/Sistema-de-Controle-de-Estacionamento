package test.java.ui;

import ui.ConsoleUI;

import java.time.LocalDateTime;

public class ConsoleUIHelperTest {
    public static void main(String[] args) {
        if (ConsoleUI.parseTipoVeiculo("carro") != 1) {
            throw new AssertionError("Esperava tipo CARRO como 1");
        }

        if (ConsoleUI.parseTipoVeiculo("moto") != 2) {
            throw new AssertionError("Esperava tipo MOTO como 2");
        }

        if (ConsoleUI.parseTipoVeiculo("caminhao") != 3) {
            throw new AssertionError("Esperava tipo CAMINHAO como 3");
        }

        LocalDateTime data = ConsoleUI.parseDataHora("2026-07-13 10:30");
        if (data == null || !data.equals(LocalDateTime.of(2026, 7, 13, 10, 30))) {
            throw new AssertionError("Data/hora não foi parseada corretamente");
        }

        System.out.println("ConsoleUIHelperTest ok");
    }
}

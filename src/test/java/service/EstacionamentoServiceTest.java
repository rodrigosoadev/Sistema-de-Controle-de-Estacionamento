package test.java.service;

import exception.EstacionamentoLotadoException;
import model.Carro;
import repository.EstacionamentoRepository;
import service.EstacionamentoService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

public class EstacionamentoServiceTest {
    public static void main(String[] args) throws Exception {
        Files.deleteIfExists(Path.of("veiculos_ativos.csv"));
        Files.deleteIfExists(Path.of("historico_financeiro.csv"));

        EstacionamentoRepository repository = new EstacionamentoRepository();
        EstacionamentoService service = new EstacionamentoService(2, repository);

        Carro carro = new Carro("ABC-1234", "Gol", LocalDateTime.of(2024, 1, 1, 10, 0));
        service.entrarVeiculo(carro);

        if (service.getVeiculos().size() != 1) {
            throw new AssertionError("Esperava 1 veículo ativo após entrada");
        }

        double valor = service.sairVeiculo("ABC-1234", LocalDateTime.of(2024, 1, 1, 11, 0));
        if (Math.abs(valor - 10.8) > 0.0001) {
            throw new AssertionError("Valor esperado 10.8, mas foi " + valor);
        }

        if (!service.getVeiculos().isEmpty()) {
            throw new AssertionError("Esperava lista vazia após saída");
        }

        if (!Files.exists(Path.of("historico_financeiro.csv"))) {
            throw new AssertionError("Histórico financeiro não foi criado");
        }

        System.out.println("EstacionamentoServiceTest ok");
    }
}

package service;

import exception.EstacionamentoLotadoException;
import exception.VeiculoNaoEncontradoException;
import model.Caminhao;
import model.Carro;
import model.Moto;
import model.Veiculo;
import repository.EstacionamentoRepository;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class EstacionamentoService {
    private final int capacidade;
    private final EstacionamentoRepository repository;
    private final Map<String, Veiculo> veiculos;

    public EstacionamentoService(int capacidade, EstacionamentoRepository repository) throws IOException {
        this.capacidade = capacidade;
        this.repository = repository;
        this.veiculos = new LinkedHashMap<>();
        carregarVeiculosAtivos();
    }

    public void entrarVeiculo(Veiculo veiculo) throws EstacionamentoLotadoException, IOException {
        if (veiculos.size() >= capacidade) {
            throw new EstacionamentoLotadoException("Estacionamento lotado");
        }

        veiculos.put(veiculo.getPlaca(), veiculo);
        repository.salvarVeiculosAtivos(veiculos.values());
    }

    public double sairVeiculo(String placa, LocalDateTime horaSaida) throws IOException {
        Veiculo veiculo = veiculos.remove(placa);
        if (veiculo == null) {
            throw new VeiculoNaoEncontradoException("Veículo não encontrado");
        }

        long minutos = calcularMinutosPermanencia(veiculo.getHoraEntrada(), horaSaida);
        double valorPago = veiculo.calcularTarifa(minutos);

        repository.salvarVeiculosAtivos(veiculos.values());
        repository.registrarHistoricoPagamento(
                placa,
                tipoDeVeiculo(veiculo),
                veiculo.getHoraEntrada(),
                horaSaida,
                minutos,
                valorPago
        );

        return valorPago;
    }

    public Collection<Veiculo> getVeiculos() {
        return veiculos.values();
    }

    public int getCapacidade() {
        return capacidade;
    }

    public int getVagasDisponiveis() {
        return capacidade - veiculos.size();
    }

    private void carregarVeiculosAtivos() throws IOException {
        for (Veiculo veiculo : repository.carregarVeiculosAtivos()) {
            veiculos.put(veiculo.getPlaca(), veiculo);
        }
    }

    private long calcularMinutosPermanencia(LocalDateTime horaEntrada, LocalDateTime horaSaida) {
        if (horaEntrada == null || horaSaida == null) {
            return 0;
        }

        long minutos = Duration.between(horaEntrada, horaSaida).toMinutes();
        return Math.max(minutos, 0);
    }

    private String tipoDeVeiculo(Veiculo veiculo) {
        if (veiculo instanceof Carro) return "CARRO";
        if (veiculo instanceof Moto) return "MOTO";
        if (veiculo instanceof Caminhao) return "CAMINHAO";
        return "VEICULO";
    }
}

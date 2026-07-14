package repository;

import model.Caminhao;
import model.Carro;
import model.Moto;
import model.Veiculo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Repositório simples para persistir veículos ativos e histórico financeiro em CSV.
 */
public class EstacionamentoRepository {
    private static final Path VEICULOS_ATIVOS = Paths.get("veiculos_ativos.csv");
    private static final Path HISTORICO_FINANCEIRO = Paths.get("historico_financeiro.csv");
    private static final String CSV_SEPARATOR = ";";
    private static final String HEADER_VEICULOS = "tipo;placa;modelo;horaEntrada";
    private static final String HEADER_HISTORICO = "placa;tipo;horaEntrada;horaSaida;minutos;valorPago";

    public List<Veiculo> carregarVeiculosAtivos() throws IOException {
        List<Veiculo> veiculos = new ArrayList<>();
        if (!Files.exists(VEICULOS_ATIVOS)) {
            return veiculos;
        }

        try (BufferedReader reader = Files.newBufferedReader(VEICULOS_ATIVOS, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String linhaLimpa = linha.trim();
                if (linhaLimpa.isEmpty() || linhaLimpa.toLowerCase().startsWith("tipo")) {
                    continue;
                }

                String[] partes = linhaLimpa.split(CSV_SEPARATOR, -1);
                if (partes.length < 4) {
                    continue;
                }

                String tipo = partes[0].trim();
                String placa = partes[1].trim();
                String modelo = partes[2].trim();
                String horaEntradaStr = partes[3].trim();

                try {
                    LocalDateTime horaEntrada = LocalDateTime.parse(horaEntradaStr);
                    Veiculo veiculo = criarVeiculo(tipo, placa, modelo, horaEntrada);
                    if (veiculo != null) {
                        veiculos.add(veiculo);
                    }
                } catch (DateTimeParseException ignored) {
                    // Ignorar linha com data inválida.
                }
            }
        }

        return veiculos;
    }

    public void salvarVeiculosAtivos(Collection<Veiculo> veiculos) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(
                VEICULOS_ATIVOS,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE
        )) {
            writer.write(HEADER_VEICULOS);
            writer.newLine();

            for (Veiculo veiculo : veiculos) {
                writer.write(String.join(
                        CSV_SEPARATOR,
                        tipoDeVeiculo(veiculo),
                        safe(veiculo.getPlaca()),
                        safe(veiculo.getModelo()),
                        formatarHora(veiculo.getHoraEntrada())
                ));
                writer.newLine();
            }
        }
    }

    public void registrarHistoricoPagamento(String placa, String tipo, LocalDateTime horaEntrada, LocalDateTime horaSaida, long minutos, double valorPago) throws IOException {
        if (!Files.exists(HISTORICO_FINANCEIRO)) {
            try (BufferedWriter writer = Files.newBufferedWriter(
                    HISTORICO_FINANCEIRO,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING,
                    StandardOpenOption.WRITE
            )) {
                writer.write(HEADER_HISTORICO);
                writer.newLine();
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(
                HISTORICO_FINANCEIRO,
                StandardCharsets.UTF_8,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND,
                StandardOpenOption.WRITE
        )) {
            String linha = String.join(
                    CSV_SEPARATOR,
                    safe(placa),
                    safe(tipo),
                    formatarHora(horaEntrada),
                    formatarHora(horaSaida),
                    String.valueOf(minutos),
                    String.valueOf(valorPago)
            );
            writer.write(linha);
            writer.newLine();
        }
    }

    public List<String[]> carregarHistoricoFinanceiro() throws IOException {
        List<String[]> registros = new ArrayList<>();
        if (!Files.exists(HISTORICO_FINANCEIRO)) {
            return registros;
        }

        try (BufferedReader reader = Files.newBufferedReader(HISTORICO_FINANCEIRO, StandardCharsets.UTF_8)) {
            String linha;
            while ((linha = reader.readLine()) != null) {
                String linhaLimpa = linha.trim();
                if (linhaLimpa.isEmpty() || linhaLimpa.toLowerCase().startsWith("placa")) {
                    continue;
                }
                registros.add(linhaLimpa.split(CSV_SEPARATOR, -1));
            }
        }

        return registros;
    }

    private Veiculo criarVeiculo(String tipo, String placa, String modelo, LocalDateTime horaEntrada) {
        return switch (tipo.toUpperCase()) {
            case "CARRO" -> new Carro(placa, modelo, horaEntrada);
            case "MOTO" -> new Moto(placa, modelo, horaEntrada);
            case "CAMINHAO", "CAMINHÃO" -> new Caminhao(placa, modelo, horaEntrada);
            default -> null;
        };
    }

    private String tipoDeVeiculo(Veiculo veiculo) {
        if (veiculo instanceof Carro) {
            return "CARRO";
        }
        if (veiculo instanceof Moto) {
            return "MOTO";
        }
        if (veiculo instanceof Caminhao) {
            return "CAMINHAO";
        }
        return "VEICULO";
    }

    private String safe(String valor) {
        if (valor == null) {
            return "";
        }
        return valor.replace(CSV_SEPARATOR, " ");
    }

    private String formatarHora(LocalDateTime hora) {
        return hora != null ? hora.toString() : "";
    }
}

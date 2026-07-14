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
 *
 * Arquivos gerados no diretório de trabalho atual:
 * - veiculos_ativos.csv
 * - historico_financeiro.csv
 */
public class EstacionamentoRepository {
    private static final Path VEICULOS_ATIVOS = Paths.get("veiculos_ativos.csv");
    private static final Path HISTORICO_FINANCEIRO = Paths.get("historico_financeiro.csv");
    private static final String CSV_SEPARATOR = ";";

    public List<Veiculo> carregarVeiculosAtivos() throws IOException {
        List<Veiculo> lista = new ArrayList<>();
        if (!Files.exists(VEICULOS_ATIVOS)) return lista;

        try (BufferedReader r = Files.newBufferedReader(VEICULOS_ATIVOS, StandardCharsets.UTF_8)) {
            String line;
            while ((line = r.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                // pular header caso exista
                if (line.toLowerCase().startsWith("tipo")) continue;

                String[] parts = line.split(CSV_SEPARATOR, -1);
                if (parts.length < 4) continue; // formato inválido

                String tipo = parts[0].trim();
                String placa = parts[1].trim();
                String modelo = parts[2].trim();
                String horaEntradaStr = parts[3].trim();

                try {
                    LocalDateTime horaEntrada = LocalDateTime.parse(horaEntradaStr);
                    Veiculo v = switch (tipo.toUpperCase()) {
                        case "CARRO" -> new Carro(placa, modelo, horaEntrada);
                        case "MOTO" -> new Moto(placa, modelo, horaEntrada);
                        case "CAMINHAO", "CAMINHÃO" -> new Caminhao(placa, modelo, horaEntrada);
                        default -> null;
                    };

                    if (v != null) lista.add(v);
                } catch (DateTimeParseException ex) {
                    // ignorar linha com data inválida
                }
            }
        }

        return lista;
    }

    public void salvarVeiculosAtivos(Collection<Veiculo> veiculos) throws IOException {
        // sobrescreve arquivo com estado atual
        try (BufferedWriter w = Files.newBufferedWriter(VEICULOS_ATIVOS, StandardCharsets.UTF_8)) {
            // header
            w.write("tipo" + CSV_SEPARATOR + "placa" + CSV_SEPARATOR + "modelo" + CSV_SEPARATOR + "horaEntrada");
            w.newLine();

            for (Veiculo v : veiculos) {
                String tipo = tipoDeVeiculo(v);
                String placa = safe(v.getPlaca());
                String modelo = safe(v.getModelo());
                String hora = v.getHoraEntrada() != null ? v.getHoraEntrada().toString() : "";
                w.write(String.join(CSV_SEPARATOR, tipo, placa, modelo, hora));
                w.newLine();
            }
        }
    }

    public void registrarHistoricoPagamento(String placa, String tipo, LocalDateTime horaEntrada, LocalDateTime horaSaida, long minutos, double valorPago) throws IOException {
        // append ao histórico financeiro
        if (!Files.exists(HISTORICO_FINANCEIRO)) {
            // criar com header
            try (BufferedWriter w = Files.newBufferedWriter(HISTORICO_FINANCEIRO, StandardCharsets.UTF_8, StandardOpenOption.CREATE)) {
                w.write("placa" + CSV_SEPARATOR + "tipo" + CSV_SEPARATOR + "horaEntrada" + CSV_SEPARATOR + "horaSaida" + CSV_SEPARATOR + "minutos" + CSV_SEPARATOR + "valorPago");
                w.newLine();
            }
        }

        try (BufferedWriter w = Files.newBufferedWriter(HISTORICO_FINANCEIRO, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            String horaEnt = horaEntrada != null ? horaEntrada.toString() : "";
            String horaSai = horaSaida != null ? horaSaida.toString() : "";
            String linha = String.join(CSV_SEPARATOR, safe(placa), safe(tipo), horaEnt, horaSai, String.valueOf(minutos), String.valueOf(valorPago));
            w.write(linha);
            w.newLine();
        }
    }

    private String tipoDeVeiculo(Veiculo v) {
        if (v instanceof Carro) return "CARRO";
        if (v instanceof Moto) return "MOTO";
        if (v instanceof Caminhao) return "CAMINHAO";
        return "VEICULO";
    }

    private String safe(String s) {
        if (s == null) return "";
        // remover separador para evitar corromper CSV
        return s.replace(CSV_SEPARATOR, " ");
    }
}

package ui;

import exception.EstacionamentoLotadoException;
import exception.VeiculoNaoEncontradoException;
import model.Caminhao;
import model.Carro;
import model.Moto;
import model.Veiculo;
import repository.EstacionamentoRepository;
import service.EstacionamentoService;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class ConsoleUI {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final EstacionamentoService service;
    private final Scanner scanner;
    private LocalDateTime horaAtualSimulada;

    public ConsoleUI(EstacionamentoService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
        this.horaAtualSimulada = LocalDateTime.now();
    }

    public void iniciar() throws IOException {
        boolean executando = true;

        while (executando) {
            limparTela();
            exibirMenu();
            int opcao = lerOpcaoMenu();

            switch (opcao) {
                case 1 -> registrarEntrada();
                case 2 -> registrarSaida();
                case 3 -> exibirVeiculosEstacionados();
                case 4 -> exibirRelatorioFinanceiro();
                case 5 -> simularAvancoTempo();
                case 6 -> {
                    salvarEstado();
                    executando = false;
                }
                default -> exibirMensagem("Opcao invalida.");
            }

            if (executando) {
                aguardarEnter();
            }
        }

        System.out.println("Sistema encerrado. Estado salvo.");
    }

    private void exibirMenu() {
        System.out.println("+----------------------------------------+");
        System.out.println("|        SISTEMA DE ESTACIONAMENTO        |");
        System.out.println("+----------------------------------------+");
        System.out.println("| 1. Registrar Entrada de Veiculo         |");
        System.out.println("| 2. Registrar Saida de Veiculo          |");
        System.out.println("| 3. Exibir Veiculos Estacionados        |");
        System.out.println("| 4. Exibir Relatorio Financeiro        |");
        System.out.println("| 5. Simular Avanco de Tempo            |");
        System.out.println("| 6. Sair                                |");
        System.out.println("+----------------------------------------+");
        System.out.print("Escolha uma opcao: ");
    }

    private int lerOpcaoMenu() {
        try {
            int opcao = scanner.nextInt();
            scanner.nextLine();
            return opcao;
        } catch (InputMismatchException ex) {
            scanner.nextLine();
            return -1;
        }
    }

    private void registrarEntrada() throws IOException {
        limparTela();
        System.out.println("== Registrar Entrada ==");
        String placa = lerTextoNaoVazio("Placa: ");
        String modelo = lerTextoNaoVazio("Modelo: ");

        System.out.println("Tipo de veiculo:");
        System.out.println("1. Carro");
        System.out.println("2. Moto");
        System.out.println("3. Caminhao");
        int tipo = lerOpcaoTipo();

        Veiculo veiculo = criarVeiculo(tipo, placa, modelo);
        if (veiculo == null) {
            exibirMensagem("Tipo invalido.");
            return;
        }

        try {
            service.entrarVeiculo(veiculo);
            exibirMensagem("Veiculo registrado com sucesso.\nVagas disponiveis: " + service.getVagasDisponiveis());
        } catch (EstacionamentoLotadoException ex) {
            exibirMensagem(ex.getMessage());
        }
    }

    private void registrarSaida() throws IOException {
        limparTela();
        System.out.println("== Registrar Saida ==");
        String placa = lerTextoNaoVazio("Placa: ");
        LocalDateTime horaSaida = lerDataHoraOpcional("Hora de saida (yyyy-MM-dd HH:mm, deixe vazio para usar a hora simulada atual): ");

        if (horaSaida == null) {
            return;
        }

        try {
            Veiculo veiculo = service.buscarVeiculo(placa);
            if (veiculo == null) {
                throw new VeiculoNaoEncontradoException("Veiculo nao encontrado");
            }

            double valor = service.sairVeiculo(placa, horaSaida);
            exibirRecibo(placa, veiculo, valor);
            exibirMensagem("Saida registrada com sucesso.");
        } catch (VeiculoNaoEncontradoException ex) {
            exibirMensagem(ex.getMessage());
        }
    }

    private void exibirVeiculosEstacionados() {
        limparTela();
        System.out.println("== Veiculos Estacionados ==");
        Collection<Veiculo> veiculos = service.getVeiculos();
        if (veiculos.isEmpty()) {
            exibirMensagem("Nenhum veiculo estacionado.");
            return;
        }

        for (Veiculo veiculo : veiculos) {
            System.out.printf(
                    "Placa: %s | Modelo: %s | Tipo: %s | Entrada: %s%n",
                    veiculo.getPlaca(),
                    veiculo.getModelo(),
                    tipoParaTexto(veiculo),
                    veiculo.getHoraEntrada().format(FORMATTER)
            );
        }
    }

    private void exibirRelatorioFinanceiro() throws IOException {
        limparTela();
        System.out.println("== Relatorio Financeiro ==");
        EstacionamentoRepository repository = new EstacionamentoRepository();
        List<String[]> registros = repository.carregarHistoricoFinanceiro();
        if (registros.isEmpty()) {
            exibirMensagem("Nenhum registro financeiro encontrado.");
            return;
        }

        double total = 0.0;
        for (String[] registro : registros) {
            if (registro.length >= 6) {
                total += Double.parseDouble(registro[5]);
            }
        }

        System.out.println("Registros salvos:");
        for (String[] registro : registros) {
            System.out.printf("Placa: %s | Tipo: %s | Valor: R$ %s%n", registro[0], registro[1], registro[5]);
        }
        System.out.printf("Total arrecadado: R$ %.2f%n", total);
    }

    private void simularAvancoTempo() {
        limparTela();
        System.out.println("== Simular Avanco de Tempo ==");
        System.out.println("1. Definir hora de saida manualmente");
        System.out.println("2. Avancar X horas");
        int opcao = lerOpcaoTipo();

        if (opcao == 1) {
            LocalDateTime hora = lerDataHoraOpcional("Nova hora de saida (yyyy-MM-dd HH:mm, deixe vazio para cancelar): ");
            if (hora != null) {
                horaAtualSimulada = hora;
                System.out.println("Hora simulada definida para: " + horaAtualSimulada.format(FORMATTER));
            }
        } else if (opcao == 2) {
            System.out.print("Quantas horas deseja avancar? ");
            int horas = scanner.nextInt();
            scanner.nextLine();
            horaAtualSimulada = horaAtualSimulada.plusHours(horas);
            System.out.println("Avanco de " + horas + " horas aplicado. Hora simulada atual: " + horaAtualSimulada.format(FORMATTER));
        } else {
            exibirMensagem("Opcao invalida.");
        }
    }

    private void salvarEstado() throws IOException {
        service.salvarEstado();
        System.out.println("Estado salvo automaticamente pelo sistema.");
    }

    private String lerTextoNaoVazio(String prompt) {
        while (true) {
            System.out.print(prompt);
            String valor = scanner.nextLine().trim();
            if (!valor.isEmpty()) {
                return valor;
            }
            exibirMensagem("Valor invalido. Informe um texto nao vazio.");
        }
    }

    private int lerOpcaoTipo() {
        while (true) {
            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();
                return opcao;
            } catch (InputMismatchException ex) {
                scanner.nextLine();
                exibirMensagem("Opcao invalida. Digite um numero.");
            }
        }
    }

    private LocalDateTime lerDataHoraOpcional(String prompt) {
        while (true) {
            System.out.print(prompt);
            String valor = scanner.nextLine().trim();
            if (valor.isEmpty()) {
                return horaAtualSimulada;
            }
            try {
                return LocalDateTime.parse(valor, FORMATTER);
            } catch (DateTimeParseException ex) {
                exibirMensagem("Formato invalido. Use yyyy-MM-dd HH:mm");
            }
        }
    }

    private void aguardarEnter() {
        System.out.print("Pressione Enter para continuar...");
        scanner.nextLine();
    }

    private void limparTela() {
        try {
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch (Exception ex) {
            System.out.print("\n\n");
        }
    }

    private void exibirMensagem(String mensagem) {
        System.out.println(mensagem);
    }

    private void exibirRecibo(String placa, Veiculo veiculo, double valor) {
        System.out.println("+----------------------------------------+");
        System.out.println("| RECIBO DE PAGAMENTO                    |");
        System.out.println("+----------------------------------------+");
        System.out.printf("| Placa: %-30s |%n", placa);
        System.out.printf("| Modelo: %-30s |%n", veiculo.getModelo());
        System.out.printf("| Tipo: %-30s |%n", tipoParaTexto(veiculo));
        System.out.printf("| Valor: R$ %-26.2f |%n", valor);
        System.out.println("+----------------------------------------+");
    }

    private Veiculo criarVeiculo(int tipo, String placa, String modelo) {
        LocalDateTime entrada = horaAtualSimulada;
        return switch (tipo) {
            case 1 -> new Carro(placa, modelo, entrada);
            case 2 -> new Moto(placa, modelo, entrada);
            case 3 -> new Caminhao(placa, modelo, entrada);
            default -> null;
        };
    }

    private String tipoParaTexto(Veiculo veiculo) {
        if (veiculo instanceof Carro) {
            return "Carro";
        }
        if (veiculo instanceof Moto) {
            return "Moto";
        }
        if (veiculo instanceof Caminhao) {
            return "Caminhao";
        }
        return "Veiculo";
    }

    public static int parseTipoVeiculo(String valor) {
        if (valor == null) {
            return -1;
        }
        return switch (valor.trim().toLowerCase()) {
            case "carro" -> 1;
            case "moto" -> 2;
            case "caminhao", "caminhão" -> 3;
            default -> -1;
        };
    }

    public static LocalDateTime parseDataHora(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return LocalDateTime.parse(valor, FORMATTER);
        } catch (DateTimeParseException ex) {
            return null;
        }
    }
}

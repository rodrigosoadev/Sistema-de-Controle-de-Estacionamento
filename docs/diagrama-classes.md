# Diagrama de Classes - Sistema de Estacionamento

Versão visual, pronta para README ou documentação do projeto.

```mermaid
%%{init: {'theme': 'base'}}%%
classDiagram
    classDef entidade fill:#E3F2FD,stroke:#1E88E5,color:#0D47A1,stroke-width:1.5px;
    classDef interface fill:#F3E5F5,stroke:#8E24AA,color:#4A148C,stroke-width:1.5px;
    classDef service fill:#E8F5E9,stroke:#43A047,color:#1B5E20,stroke-width:1.5px;
    classDef repo fill:#FFF3E0,stroke:#FB8C00,color:#E65100,stroke-width:1.5px;
    classDef ui fill:#FCE4EC,stroke:#D81B60,color:#880E4F,stroke-width:1.5px;
    classDef exception fill:#FFEBEE,stroke:#E53935,color:#B71C1C,stroke-width:1.5px;

    class Veiculo,Carro,Moto,Caminhao,Ticket entidade;
    class Descontavel interface;
    class EstacionamentoService service;
    class EstacionamentoRepository repo;
    class ConsoleUI ui;
    class Main,EstacionamentoLotadoException,VeiculoNaoEncontradoException exception;

    class Veiculo {
        <<abstract>>
        -String placa
        -String modelo
        -LocalDateTime horaEntrada
        +Veiculo(String placa, String modelo, LocalDateTime horaEntrada)
        +String getPlaca()
        +String getModelo()
        +LocalDateTime getHoraEntrada()
        +double calcularTarifa(long minutos)
    }

    class Carro {
        -double TAXA_POR_MINUTO
        -double DESCONTO_FIDELIDADE
        +Carro(String placa, String modelo, LocalDateTime horaEntrada)
        +double calcularTarifa(long minutos)
        +double aplicarDesconto(double valorOriginal)
    }

    class Moto {
        -double TAXA_POR_MINUTO
        +Moto(String placa, String modelo, LocalDateTime horaEntrada)
        +double calcularTarifa(long minutos)
    }

    class Caminhao {
        -double TAXA_POR_MINUTO
        +Caminhao(String placa, String modelo, LocalDateTime horaEntrada)
        +double calcularTarifa(long minutos)
    }

    class Descontavel {
        <<interface>>
        +double aplicarDesconto(double valorOriginal)
    }

    class Ticket {
        <<record>>
        +String placa
        +LocalDateTime horaEntrada
        +String tipoVeiculo
    }

    class EstacionamentoService {
        -int capacidade
        -EstacionamentoRepository repository
        -Map~String, Veiculo~ veiculos
        +EstacionamentoService(int capacidade, EstacionamentoRepository repository)
        +void entrarVeiculo(Veiculo veiculo)
        +double sairVeiculo(String placa, LocalDateTime horaSaida)
        +Collection~Veiculo~ getVeiculos()
        +Veiculo buscarVeiculo(String placa)
        +int getCapacidade()
        +void salvarEstado()
        +int getVagasDisponiveis()
    }

    class EstacionamentoRepository {
        -Path VEICULOS_ATIVOS
        -Path HISTORICO_FINANCEIRO
        +List~Veiculo~ carregarVeiculosAtivos()
        +void salvarVeiculosAtivos(Collection~Veiculo~ veiculos)
        +void registrarHistoricoPagamento(String placa, String tipo, LocalDateTime horaEntrada, LocalDateTime horaSaida, long minutos, double valorPago)
        +List~String[]~ carregarHistoricoFinanceiro()
    }

    class ConsoleUI {
        -EstacionamentoService service
        -Scanner scanner
        -LocalDateTime horaAtualSimulada
        +ConsoleUI(EstacionamentoService service)
        +void iniciar()
        +void registrarEntrada()
        +void registrarSaida()
        +void exibirVeiculosEstacionados()
        +void exibirRelatorioFinanceiro()
        +void simularAvancoTempo()
    }

    class Main {
        +void main(String[] args)
    }

    class EstacionamentoLotadoException {
        +EstacionamentoLotadoException(String message)
    }

    class VeiculoNaoEncontradoException {
        +VeiculoNaoEncontradoException(String message)
    }

    Veiculo <|-- Carro
    Veiculo <|-- Moto
    Veiculo <|-- Caminhao
    Carro ..|> Descontavel

    EstacionamentoService o-- "0..*" Veiculo : controla
    EstacionamentoService --> EstacionamentoRepository : persiste e consulta
    ConsoleUI --> EstacionamentoService : interage

    Main ..> EstacionamentoRepository : instancia
    Main ..> EstacionamentoService : instancia
    Main ..> ConsoleUI : instancia

    Exception <|-- EstacionamentoLotadoException
    RuntimeException <|-- VeiculoNaoEncontradoException
```

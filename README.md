# Sistema de Controle de Estacionamento

Sistema de gerenciamento de estacionamento desenvolvido em Java com foco em conceitos de Programação Orientada a Objetos, persistência de dados em arquivos CSV e interação via terminal.

## 1. Visão Geral

O projeto simula operações básicas de um estacionamento, permitindo registrar entrada e saída de veículos, controlar vagas, calcular tarifas, exibir relatórios financeiros e salvar o estado do sistema para recuperação futura.

## 2. Funcionalidades

- Registrar entrada de veículos de diferentes tipos.
- Registrar saída de veículos e calcular o valor a ser pago.
- Controlar a capacidade do estacionamento e informar vagas disponíveis.
- Listar os veículos atualmente estacionados.
- Exibir um relatório financeiro com os pagamentos registrados.
- Persistir dados em arquivos CSV para manter o estado entre execuções.
- Tratar exceções para situações como estacionamento lotado e veículo não encontrado.

## 3. Como Executar

### Requisitos

- Java JDK 17 ou superior.
- Terminal ou prompt de comando.

### Passo a passo

1. Abra o terminal na pasta do projeto:

   ```bash
   cd "C:\Users\Rodrigo Soares\sistema-estacionamento\Sistema-de-Controle-de-Estacionamento"
   ```

2. Compile os arquivos Java:

   No PowerShell, execute:

   ```powershell
   javac -d out (Get-ChildItem -Recurse -Filter *.java -Path src\main\java | ForEach-Object { $_.FullName })
   ```

   Em ambientes baseados em shell Unix, a versão equivalente seria:

   ```bash
   javac -d out $(find src/main/java -name "*.java")
   ```

3. Execute o programa:

   ```powershell
   java -cp out Main
   ```

4. O sistema iniciará com um menu interativo no terminal.

### Arquivos gerados

Ao executar o programa, os arquivos abaixo serão criados ou atualizados na raiz do projeto:

- `veiculos_ativos.csv`
- `historico_financeiro.csv`

## 4. Estrutura do Projeto

A organização segue uma separação por responsabilidades:

- `model`: classes de domínio, como `Veiculo`, `Carro`, `Moto`, `Caminhao`, `Ticket` e interfaces.
- `service`: regras de negócio, como controle de vagas e cálculo de permanência.
- `repository`: leitura e escrita de dados em arquivos CSV.
- `ui`: interação com o usuário pelo terminal.
- `exception`: exceções personalizadas.

## 5. Conceitos de POO Aplicados

### Herança

A classe abstrata `Veiculo` serve como base para `Carro`, `Moto` e `Caminhao`, permitindo reutilizar atributos e comportamentos comuns.

### Polimorfismo

O método `calcularTarifa(long minutos)` é implementado de forma diferente em cada tipo de veículo, permitindo que a mesma chamada seja tratada de maneira específica conforme o objeto.

### Encapsulamento

Os atributos principais das classes, como placa, modelo e hora de entrada, são privados e acessados por métodos getters, protegendo o estado interno dos objetos.

### Interfaces

A interface `Descontavel` define um contrato para classes que precisam aplicar desconto, como o carro do sistema.

### Records

A classe `Ticket` usa o recurso `record` para representar um modelo simples e imutável de dados, com menos boilerplate de código.

### Coleções

O sistema usa `Map` para armazenar veículos por placa e `Collection` para manipular listas de forma flexível.

### Tratamento de Exceções

As exceções personalizadas `EstacionamentoLotadoException` e `VeiculoNaoEncontradoException` tornam o fluxo do programa mais claro e seguro.

### Persistência em Arquivos

Os dados são salvos em arquivos CSV, permitindo que o estado do estacionamento seja recuperado em execuções futuras.

## 6. Observações

Este projeto foi pensado como uma aplicação didática para demonstrar, de forma prática, conceitos fundamentais de Java e POO em um contexto simples e compreensível.

## 7. Diagrama

![Diagrama de Classes - Sistema de Estacionamento](docs/diagrama_estacionamento.png)
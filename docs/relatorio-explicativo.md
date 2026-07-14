# Relatório Explicativo - Sistema de Controle de Estacionamento

## 1. Introdução

O presente relatório descreve o desenvolvimento do Sistema de Controle de Estacionamento, uma aplicação em Java voltada para simular operações básicas de um estacionamento. O projeto foi concebido como uma solução didática para demonstrar, de forma prática, a aplicação de conceitos fundamentais de Programação Orientada a Objetos, organização de código em camadas e persistência de dados por meio de arquivos CSV.

## 2. Decisões de Projeto

A escolha do domínio do estacionamento se deu pela sua simplicidade conceitual e pela ampla possibilidade de modelagem de problemas reais em um escopo controlado. O tema permite representar características importantes do desenvolvimento de software, como controle de entrada e saída, cálculo financeiro, gestão de capacidade e armazenamento do estado do sistema.

A estrutura do projeto foi organizada em pacotes com finalidades específicas. A divisão em `model`, `service`, `repository`, `ui` e `exception` favorece a separação de responsabilidades e aumenta a legibilidade do código. A camada de modelos concentra as entidades do problema, a camada de serviço implementa as regras de negócio, a camada de repositório cuida da persistência e a interface de usuário encapsula a comunicação com o terminal.

Essa organização também facilita a evolução futura do sistema, permitindo, por exemplo, substituir a interface textual por uma interface gráfica ou migrar a persistência de arquivos CSV para um banco de dados sem precisar reestruturar completamente o domínio.

## 3. Principais Desafios Enfrentados

### 3.1 Manipulação de datas e horários

Um dos principais desafios foi o controle de horários de entrada e saída, especialmente para o cálculo de permanência e tarifação. Para isso, foi utilizado `LocalDateTime`, uma classe da API de data e hora do Java que oferece maior clareza e segurança do que estruturas mais primitivas. A manipulação desse tipo de dado exigiu atenção à conversão entre entrada do usuário e valores internos do sistema, além da necessidade de normalizar o formato de exibição para leitura no terminal.

### 3.2 Persistência em arquivos CSV

Outro desafio relevante foi o fluxo de leitura e gravação dos dados em arquivos CSV. Como o sistema precisava manter informações sobre veículos ativos e histórico financeiro mesmo após o encerramento da execução, foi necessário implementar uma estratégia simples, mas eficiente, para salvar e recuperar essas informações. O uso de `BufferedReader` e `BufferedWriter` permitiu a leitura e escrita controlada dos arquivos, além de tornar a persistência mais robusta para cenários reais de uso.

### 3.3 Organização do fluxo de negócio

Também foi necessário estruturar bem o fluxo das operações, de modo que a interface de usuário não realizasse diretamente tarefas de domínio ou persistência. A criação da camada de serviço ajudou a centralizar as regras do sistema, reduzindo o acoplamento e tornando as operações mais coesas e fáceis de testar.

## 4. Soluções Adotadas

A solução adotada foi implementar um modelo orientado a objetos com classes especializadas para cada tipo de veículo, preservando as características comuns em uma classe base. O polimorfismo foi utilizado para tratar o cálculo da tarifa de forma específica para cada subtipo, enquanto a interface `Descontavel` permitiu incluir um comportamento adicional para carros sem comprometer a estrutura geral do projeto.

Para o tratamento de erros, foram criadas exceções personalizadas que sinalizam situações relevantes do contexto do sistema, como capacidade esgotada e ausência de veículo. Isso contribuiu para o aumento da clareza do código e da experiência de uso da aplicação.

## 5. Considerações Finais

O projeto atende aos objetivos propostos de forma satisfatória, pois combina conceitos teóricos de POO com uma implementação prática e funcional. A aplicação demonstra como modelos simples podem evoluir para soluções organizadas, reutilizáveis e preparadas para futuras expansões, como a inclusão de novas regras de tarifa, uma interface gráfica ou persistência em banco de dados.

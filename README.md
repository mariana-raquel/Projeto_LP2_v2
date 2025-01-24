# Laboratório 01 - Sistema Bancário

## Sumário:
* **[Descrição](#descrição)**
* **[Funcionalidades](#funcionalidades)**
* **[Como executar?](#como-executar)**
* **[Observações](#observações)**
* **[Discente](#discente)**


## Descrição:
Desenvolver um sistema de banco simples que permite o cadastro de usuários e operações bancárias básicas, como consulta de saldo, depósito, saque e transferência entre contas. A implementação deve ser feita na linguagem Java.

O detalhamento do projeto encontra-se __*[aqui](src/main/resources/LAB_1_LP_2.pdf)*__.


## Funcionalidades:

### 1 - Cadastrar um cliente:
Permite o usuário cadastrar um novo cliente no banco, incluindo sua primeira conta.

### 2 - Cadastrar nova uma conta:
Permite o usuário cadastrar uma nova conta para algum dos clientes cadastrados.

### 3 - Listar clientes cadastrados:
Permite o usuário listar todos os clientes cadastrados.

### 4 - Listar contas cadastradas:
Permite o usuário listar todas as contas cadastradas, com seus respectivos donos.

### 5 - Realizar login:
Permite o usuário realizar login em algum cliente cadastrado, e ter acesso as operações: Consultar saldo, Realizar depósito, Realizar saque, Realizar tranferência ou Realizar logout.


## Como executar?

Será necessário executar o método main presente no arquivo [BancoAzul.java](src/main/java/ufrn/bti/bancoAzul/BancoAzul.java). Isso irá abrir o console com as funcionalidades.


## Observações:

O projeto utiliza lombok para os **logs**, e em algumas IDE's, como no caso do eclipse, é necessário realizar algumas configurações anteriormente:

Caso esteja utilizando o eclipe, pode seguir esses passos:

1. Baixar o jar do lombok: Entre no site do [lombok](https://projectlombok.org/) e baixe o arquivo;

2. Execute o jar do lombok: ```java -jar lombok.jar```;

3. Selecione o local onde está o eclipe: Após executar o jar, ele procurará o eclipse em seu sistema operacional, caso não encontre, você pode informar a localização;

4. Finalizar a instalação no eclipse;

5. Reiniciar o eclipse.

Link de um tutorial: https://dicasdeprogramacao.com.br/como-configurar-o-lombok-no-eclipse/


## Discente:
Mariana Raquel de Morais (20230033690)









Funções 2:
Classe banco tem string de agencia
Lista de classes de bancos, cada uma sendo uma agencia

classes pra contas com herança

Exceções personalizadas - saldo insuficiente

Salvar todos os dados em arquivo csv
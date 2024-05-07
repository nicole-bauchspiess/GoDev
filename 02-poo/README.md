Exercicios de Programação Orientada a Objetos - GoDev
> Para todos os exercícios foram implementados classes de testes visando o bom funcionamento do código somado a aprimorar as boas práticas de programação

1. Exercício bonificação:
Tem o objetivo de controlar o valor em reais das bonificações oferecidas aos colaboradores de uma empresa. A classe Gerente cujo o percentual de bonificação é 10% do salário vigente herda a classe Funcionario em que a bonificação representa 5% do salário.  

Neste caso, foi possível aprimorar os conhecimentos de dependencia e herança com base nos diagramas UML oferecidos. 

2. Exercício titular carro:
Com finalidade de calcular o IPVA e valor da revenda de um carro, foi criada a classe Carro com atributos de fabricante, modelo, cor, anoFabricao e precoCompra. Para cálculo da revenda, teve-se como base que o carro perde 5% do valor a cada ano, até o 20º ano e depois não tem depreciação. Já o IPVA correspondia a 4% do valor de revenda. Somado a isso, a classe Carro tem um vínculo de associação com a classe Pessoa. 


3. Exercício pedido:
Visando obter o valor total de um pedido foi implementado 3 classes (Pedido, ItemPedido e Produto) em que a soma do pedido é feita pelo  soma dos valores do itens multiplicado pelas quantidades.

4. Exercício conta bancária: 
A classe Conta tem os atributos de número, agência, saldo e tipoConta. O TIPOCONTA é um ENUM cujo pode ter valores de CONTA_CORRENTE OU POUPANCA. Somado a isso, foi feito operações de deposito e saque em que o valor do saldo é validado e alterado caso a operação retorne com sucesso. 








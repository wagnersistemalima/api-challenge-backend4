# api-challenge-backend4
aplicação para controle de orçamento familiar.

## História
* Após alguns testes com protótipos feitos pelo time de UX de uma empresa, foi requisitada a primeira versão de uma aplicação para controle de orçamento familiar. A aplicação deve permitir que uma pessoa cadastre suas receitas e despesas do mês, bem como gerar um relatório mensal.

* Os times de frontend e UI já estão trabalhando no layout e nas telas. Para o back-end, as principais funcionalidades a serem implementadas são:

* API com rotas implementadas seguindo as boas práticas do modelo REST;
* Validações feitas conforme as regras de negócio;
* Implementação de base de dados para persistência das informações;
* Serviço de autenticação/autorização para restringir acesso às informações.


## Metodologia agil Scrum

* Ferramenta para gerenciar o desenvolvimento do projeto
![sprint active](/image/sprintActive.png)

## Setup do Projeto
* Linguagem de programação: Kotlin
* Tecnologia: Spring Boot 2.7.2
* Gerenciador de dependência: Maven
* Java 11
* IDE IntelJ


## Implementação utilizando as ferramentas do ecossistema Spring com Kotlin


* Spring Web: Crie aplicativos da web, incluindo RESTful, usando Spring MVC. Usa Apache Tomcat como o contêiner integrado padrão.

* Bean Validation: é uma especificação que permite validar objetos com facilidade em diferentes camadas da aplicação. A vantagem de usar Bean Validation é que as restrições ficam inseridas nas classes de modelo.

* H2: Banco de dados em memória, para testes

* MySQL: banco de desenvolvimento

* Spring Data JPA: Especificação da biblioteca padrão de persistência de dados no java, baseado no mapeamento objeto relacional (javax.percistence)

* Hibernate: É uma das implementações da especificação JPA mais popular

* Especificação: (javax.persistence.Enty) É uma boa prática fazer o código com base na especificação, pois caso depois precise trocar a implementação Hibernate por outra implementação, a aplicação continuará funcionando

* Testes unitarios utilizando o MockK, que é uma ferramenta aqui do Kotlin que vem ganhando bastante espaço no mercado.

## Desafio da segunda semana, Mudanças na API

* Após finalizar as atividades da semana 1, foi liberada uma primeira versão da aplicação. Assim, quem utilizá-la pode realizar testes e fornecer feedbacks para o time de desenvolvimento.

* As pessoas gostaram bastante da aplicação e solicitaram um ajuste importante: possibilitar a categorização das despesas. Além disso, solicitaram também mais algumas funcionalidades:

![feature](/image/proxima-feature.png)
* Busca de receitas e despesas por descrição
* Listagem de receitas e despesas de um determinado mês
* Resumo de determinado mês (total de receitas, total de despesas, saldo final e valor total agrupado por categoria)

![sprintActive](/image/sprintActive2.png)


## Relatorio do total gasto no mes por categoria
* Teste com Insomnia

![relatorio](/image/insomnia-json-relatorio.png)
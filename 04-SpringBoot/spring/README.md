<h1>
API REST de Gestão Médica: Cadastro de Médicos, Pacientes e Agendamento de Consultas</h1>

<br> 
Com objetivo de controlar as consultas de uma clínica médica, é possível realizar o cadastro dos médicos, pacientes e suas respectivas consultas. A fim de ordenar e limitar os registros de médicos e pacientes  retornados pela api foi utilizado a paginação por meio de parâmetros na url.  Somado a isso, por meio do Spring security foi validado a autenticação e autorização do usuário para poder fazer as requisições na API  e geração do token JWT, visando uma aplicação stateless (não armazena as informações se o usuário está logado ou não) mais segura. Foi feita a documentação através do SpringDoc utilizando o swagger e testes unitários e de integração utilizando o JUnit e Mockito.  

<br>
<br>

## Instalação e execução: 

- Instalação do postgres, versão 14.11: 
https://www.enterprisedb.com/downloads/postgres-postgresql-downloads
- Criação do banco de dados: 'CREATE DATABASE vollmed_api';

- É possível fazer a clonagem do projeto no gitLab e posteriormente, com a IDE intellij aberta, pode-se selecionar a opção File - Open - e abrir a pasta do projeto baixado. 
- Configuração do aplication-properties: no caminho scr/main/resources/aplication-properties é necessário informar o usuário e senha do banco de dados criado na instalação do postgres. Por padrão é criado um usuário 'postgres' onde é definido apenas a senha. 

- Após isso, no intellij - canto superior direito - maven - clicar sobre 'Reload all maven projects' para baixar as dependencias do maven. Executar o projeto. 

- Antes de executar as requisições é necessário fazer um post do login para autenticar as credenciais. Utilizar a url e o json (via navegador ou postman/insomnia) :

<br>
localhost:8080/login : 
{
	"login": "ana.souza@voll.med",
	"senha": 123456"
}

- Pegar o token gerado no login e informar no autenticathionHeader das outras requisições para conseguir autenticação necessária e não retornar código 403 (forbidden)

<br>

## Documentação SpringDoc:
- Após a execução do projeto rodar a url: 
- http://localhost:8080/swagger-ui/index.html
- http://localhost:8080/v3/api-docs

<br>

## Tecnologias utilizadas:
- Linguagem: Java 17
- Framework: Spring Boot 3.0.0
- JPA/Hibernate 
- Banco de dados relacional: Postgres 14
- SGBD: PG Admin 4
- Testes da API: Postman
- Gerenciador de dependencias: Maven


## Dependencias Maven:
- Spring Security 
- Sprin Web
- Spring Data JPA
- Devtools 
- Postgres 
- SpringDoc: utilizado para documentação com swagger: https://springdoc.org/
- Validation: permite a validação de campos da entidade JPA 
- Lombok: reduzir verbosidade de classes que seguem o padrão de  encapsulamento geter/setter/constructor/hashcode and equals
- Java-JWT: geração de token para autenticação na API REST, desenvolvido por AUTh0 -> documentação:https://github.com/auth0/java-jwt
- Flyway: controle das versões do banco de dados por meio de migrations


<br>

## Endpoint básicos:

 MEDICOS
- GET/POST/PUT: localhost:8080/medicos
- GET (byId)/DELETE: localhost:8080/medicos/1 

PACIENTE:
- GET/POST/PUT: localhost:8080/pacientes
- GET (byId)/DELETE: localhost:8080/pacientes/1 

LOGIN:
- POST: localhost:8080/login
- Ao enviar uma requisição post com esta url, gera-se um token cujo será usado nas outras requisições para identificar que o usuário está autenticado. 
Este token tem validade de 2 horas a partir de sua geração 




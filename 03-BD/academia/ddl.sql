
CREATE TABLE aluno (
	id_aluno SERIAL PRIMARY KEY NOT NULL,
	nome VARCHAR(255) NOT NULL,
	telefone VARCHAR(15) NOT NULL,
	dt_nascimento DATE,
	rua VARCHAR(255),
	numero INT,
	altura FLOAT,
	peso INT
);

CREATE TABLE atividade(
	id_atividade SERIAL PRIMARY KEY NOT NULL, 
	nome VARCHAR(50)
);


CREATE TABLE instrutor(
	id_instrutor SERIAL PRIMARY KEY NOT NULL, 
	rg VARCHAR(12),
	nome VARCHAR(255) NOT NULL, 
	dt_nascimento DATE NOT NULL
);

/*
CREATE TABLE telefone_instrutor(
	id_telefone SERIAL PRIMARY KEY NOT NULL, 
	numero VARCHAR(15),
	tipo VARCHAR(45),
	id_instrutor INT REFERENCES instrutor(id_instrutor)
);
*/

CREATE TABLE turma(
	id_turma SERIAL PRIMARY KEY NOT NULL, 
	horario TIME NOT NULL,
	duracao INT, 
	dt_inicio DATE, 
	instrutor_id_instrutor INT REFERENCES instrutor(id_instrutor), 
	instrutor_id_atividade INT NOT NULL REFERENCES atividade(id_atividade)
);

CREATE TABLE matricula (
    id_aluno INT NOT NULL,
    id_turma INT NOT NULL,
    dt_matricula DATE NOT NULL,
    PRIMARY KEY (id_aluno, id_turma),
    FOREIGN KEY (id_aluno) REFERENCES aluno(id_aluno),
    FOREIGN KEY (id_turma) REFERENCES turma(id_turma)
);

CREATE TABLE frequencia (
    id_frequencia SERIAL PRIMARY KEY NOT NULL,
    dt_frequencia DATE NOT NULL,
    presente BOOLEAN,
    matricula_id_aluno int,
    matricula_id_turma int,
    FOREIGN KEY (matricula_id_aluno, matricula_id_turma) 
	REFERENCES matricula(id_aluno, id_turma)
);
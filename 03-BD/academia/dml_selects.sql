update aluno set peso = 90 where id_aluno = 4;
update instrutor set rg = '123456' where id_instrutor = 3; 
delete from aluno where id_aluno = 8; 

--1.Exiba a atividade, o horário da turma e a duração de todas as turmas de “Musculação” e “Bike indoor”
select a.nome, t.horario, t.duracao 
from atividade a 
inner join turma t on a.id_atividade = t.instrutor_id_atividade
where a.id_atividade = 10 or a.id_atividade = 1;


--2.Exiba a atividade, o horário da turma, duração e o nome do instrutor de todas as turmas de "Ginástica" e "Circo". Ordene pelo horário da atividade
select a.nome, t.horario, t.duracao, i.nome 
from atividade a 
join turma t on a.id_atividade = t.instrutor_id_atividade
join instrutor i on i.id_instrutor = t.instrutor_id_instrutor
where a.id_atividade = 2 or a.id_atividade = 6;
order by 2, 1;


--3.Exiba a atividade, o horário da turma, duração e o nome do instrutor de todas as turmas que ocorrem no período vespertino
select a.nome, t.horario, t.duracao, i.nome 
from atividade a 
join turma t on a.id_atividade = t.instrutor_id_atividade
join instrutor i on i.id_instrutor = t.instrutor_id_instrutor
where t.horario between '12:00:00' and '17:59:00'
order by 2,1;


--4. Exiba o horário da turma, o professor e a duração de todas as turmas do professor “Bernardo Souza”
select a.nome, t.horario, t.duracao, i.nome 
from atividade a 
join turma t on a.id_atividade = t.instrutor_id_atividade
join instrutor i on i.id_instrutor = t.instrutor_id_instrutor
where i.nome = 'Bernardo Souza'
order by 2,1;


--5.Mostre o nome dos alunos que fizeram as 5 ultimas matriculas(mais recentes)
select a.nome, m.dt_matricula
from aluno a
inner join matricula m on a.id_aluno = m.id_aluno
order by dt_matricula desc
limit 5;


--6.Exiba o nome de todos os alunos que estão matriculados em uma turma
select a.nome
from aluno a
right join matricula m on a.id_aluno = m.id_aluno
group by a.id_aluno;


--7.Exiba o nome dos alunos que não possuem matriculas
select nome from aluno where id_aluno not in (select id_aluno from matricula);

--8. Exiba a nome da atividade, o horário da turma e a duração de todas as turmas sem instrutor
select a.nome, t.horario, t.duracao
from atividade a
inner join turma t on a.id_atividade = t.instrutor_id_atividade
where t.instrutor_id_instrutor is null;


--9. Liste o instrutor, atividade, horário e duração ordenado pelo o nome do instrutor
select i.nome, a.nome, t.horario, t.duracao
from atividade a
inner join turma t on a.id_atividade = t.instrutor_id_atividade
left join instrutor i on i.id_instrutor = t.instrutor_id_instrutor
order by i.nome;


--10. Mesmo item que o anterior, mostrando também as turmas sem instrutor
select i.nome, a.nome, t.horario, t.duracao
from atividade a
right join turma t on a.id_atividade = t.instrutor_id_atividade
join instrutor i on i.id_instrutor = t.instrutor_id_instrutor
order by i.nome;


--11. Exiba o nome e o dia/nome do mês dos alunos que fazem aniversário no segundo semestre.  A lista deve ser ordenada dia mês de aniversário
select nome, to_char(dt_nascimento, 'DD/TMMonth') as nascimento
from aluno 
where extract(month from dt_nascimento) > 6
order by dt_nascimento; 


--*********************************************
--1. Infome quantos alunos existem?
select count(*) from aluno;


--2. Infome quantos instrutores existem?
select count(*) from instrutor;


--3. Informe o nome da atividade e quantos instrutores (distintos) a atividade tem. Ordenado pela atividade que possui mais instrutores
select a.nome, count(distinct(t.instrutor_id_instrutor))
from atividade a
inner join turma t on t.instrutor_id_atividade = a.id_atividade 
group by a.nome
order by 2 desc, 1 asc;



--4. Informe o nome da atividade, a quantidade de turmas e quantos instrutores (distintos) a atividade tem 
select a.nome, count(t.id_turma) as qtd_turma, count(distinct(t.instrutor_id_instrutor)) as qtd_instrutores
from atividade a
inner join turma t on t.instrutor_id_atividade = a.id_atividade 
group by a.nome
order by 1 desc;


--5. Selecione todos os instrutores que estão dando aula
select i.nome, count(t.instrutor_id_instrutor) as total
from instrutor i 
left join turma t on t.instrutor_id_instrutor = i.id_instrutor
group by  i.nome,t.instrutor_id_instrutor
having (count(t.instrutor_id_instrutor) > 0)
;

--6. Selecione todos os instrutores que não estão dando aula
select i.nome, count(t.instrutor_id_instrutor) as total
from instrutor i 
left join turma t on t.instrutor_id_instrutor = i.id_instrutor 
group by i.nome, t.instrutor_id_instrutor
having (count(t.instrutor_id_instrutor) <= 0)
;



--7. Exiba a quantidade de turmas por atividade
select a.nome, count(*) 
from atividade a 
inner join turma t on a.id_atividade = t.instrutor_id_atividade 
group by a.id_atividade;



--8. Exiba o nome dos alunos que estão matriculdos em pelo menos 2 turmas 
select a.nome
from aluno a
inner join matricula m on a.id_aluno = m.id_aluno
group by a.nome
having (count(m.id_turma) >= 2);



--9. Exiba o nome da atividade, dados da turma e quantidade de alunos
select a.nome, t.horario, t.duracao, count(m.id_aluno) 
from matricula m
right join turma t on t.id_turma = m.id_turma
inner join atividade a on a.id_atividade = t.instrutor_id_atividade
group by t.id_turma, a.nome, t.horario
order by 4 desc;



--10. Exiba o nome da atividade com mais alunos
select a.nome,  count(*) as alunos
from atividade a
inner join turma t on t.instrutor_id_atividade = a.id_atividade
inner join matricula m on m.id_turma = t.id_turma
group by m.id_turma, a.nome
order by alunos desc 
limit 1;



--11.Exiba o nome do aluno mais assíduo 
select a.nome, count(*) as total
from aluno a 
inner join frequencia f
on a.id_aluno = f.matricula_id_aluno
group by a.id_aluno 
order by 2 desc
limit 1;



--12. Exiba o nome e a quantidade de presenças dos alunos que vieram pelo menos em 3 aulas
select a.nome, count(*) as total
from aluno a 
inner join frequencia f
on a.id_aluno = f.matricula_id_aluno
group by a.id_aluno 
having(count(*) >=3)
order by 2 desc;

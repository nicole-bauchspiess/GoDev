--1 - Quantos clientes estão cadastrados?
select count(*) from clientes;

--2 - Quantos clientes estão cadastrados em cada plano?
select descricao, count(clientes.id) as total
from clientes
right join planos on planos_id = planos.id
group by planos_id, descricao;


--3 - Quais os artistas que estão no sistema?
select * from artistas;

--4 - Quais são os planos, valores e limites de downloads?
select descricao, valor, limite as limite_downloads from planos;


--5 - Quantos artistas tem cada gravadora?
select g.nome, count(*) as total_artistas
from artistas a
inner join gravadoras g on a.gravadoras_id = g.id
group by gravadoras_id,  g.nome;

--6 - Qual gravadora tem mais artistas?
select g.nome, count(a.id) as total_artistas
from artistas a
inner join gravadoras g on a.gravadoras_id = g.id
group by gravadoras_id,  g.nome
order by 2 desc
limit 1;

--7 - Quais são as músicas do “Mano Lima”?
select m.nome 
from musicas m
inner join musicas_has_artistas ma on m.id = ma.musicas_id
where ma.artistas_id = 1;

--8 - Quais são as músicas da gravadora ACIT?
select m.nome
from musicas m
inner join musicas_has_artistas ma on ma.musicas_id = m.id
where ma.artistas_id in(select a.id from artistas a where gravadoras_id = 2)
group by m.id;


--9 - Quantas músicas tem cada gênero?
select g.descricao, count(m.id)
from musicas m
right join generos g on m.generos_id = g.id
group by generos_id,g.descricao;

--10 - Quantas músicas cada cliente baixou?
select c.login, count(mc.id)
from clientes c
left join musicas_has_clientes mc on mc.clientes_id = c.id
group by c.login;


--11 - Qual o gênero mais baixado pelos clientes do plano Light?
select g.descricao, count(g.id)
from generos g
inner join musicas m on g.id = m.generos_id
inner join musicas_has_clientes ma on ma.musicas_id = m.id
where ma.clientes_id in(select c.id from clientes c where c.planos_id = 1)
group by m.generos_id, g.descricao
order by 2 desc
limit 1;


--12 - Quais são os artistas e respectivas gravadoras com mais músicas baixadas no plano Light?
select a.nome, g.nome, count(*) as total
from artistas a 
inner join gravadoras g on g.id = a.gravadoras_id
inner join musicas_has_artistas ma on ma.artistas_id = a.id
inner join musicas_has_clientes mc on ma.musicas_id = mc.musicas_id
where mc.clientes_id in(select c.id from clientes c where c.planos_id = 1)
group by a.id, a.nome, g.nome
order by 3 desc
limit 5;


--13 - Quanto vou receber desses clientes que estão cadastrados em cada plano? Nome plano, quantidade de clientes, valor.
select p.descricao, count(c.id) * p.valor as valor_total_plano
from clientes c
right join planos p on p.id = c.planos_id
group by c.planos_id, p.descricao, p.valor;


--14 - Qual o faturamento da minha empresa?
select sum(p.valor) 
from planos p
inner join clientes c on c.planos_id = p.id;




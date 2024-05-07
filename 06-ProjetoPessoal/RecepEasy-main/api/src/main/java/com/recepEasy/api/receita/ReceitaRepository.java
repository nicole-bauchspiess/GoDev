package com.recepEasy.api.receita;

import com.recepEasy.api.receita.dto.DadosDetalhamentoReceita;
import com.recepEasy.api.usuario.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface ReceitaRepository extends JpaRepository<Receita, Long> {

    Page<DadosDetalhamentoReceita> findByAtivoTrueAndPrivadoFalseOrderByHoraPublicacaoDesc(Pageable paginacao);

    @Query("""
            select r
            from Receita r
            where usuario = :usuario
            and ativo = true
            order by horaPublicacao desc
            """)
    Page<DadosDetalhamentoReceita> findReceitasByUsuario(Usuario usuario,Pageable paginacao);

    @Query("""
            select r
            from Receita r
            where usuario = :usuario
            and privado = false
            and ativo = true
            order by qtdCurtidas desc
            """)
    Page<DadosDetalhamentoReceita> findReceitasByUsuarioPrivadoFalse(Usuario usuario,Pageable paginacao);



    @Query(value = """
            select r
            from Receita r
            where privado = false
            and ativo = true
            order by qtdCurtidas desc
            """)
    Page<DadosDetalhamentoReceita>  findReceitasMaisCurtidas(@Param("pageSize") int pageSize, Pageable pageable);

}

package med.voll.api.domain.repository;
import med.voll.api.domain.model.Paciente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    Page<Paciente> findAllByAtivoTrue(Pageable paginacao);


    @Query("""
         select p.ativo
         from Paciente p
         where p.id = :id
      """)
    boolean findAtivoById(Long id);


}

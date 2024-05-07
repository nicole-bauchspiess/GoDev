import br.com.alura.leilao.dao.LeilaoDao;
import br.com.alura.leilao.model.Leilao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

public class HelloWorldMockito {


    @Test
    void hello(){
       LeilaoDao mock = Mockito.mock(LeilaoDao.class); //cria um mock da classe leilaoDao
        //simula comportamento de uma classe , nao executa o metodo em si. Se o metodo retorna algo: mock retorna a lista vazia porque nao chega a ir no bd consultar
        List<Leilao> todos = mock.buscarTodos();
        Assertions.assertTrue((todos.isEmpty()));
    }



}

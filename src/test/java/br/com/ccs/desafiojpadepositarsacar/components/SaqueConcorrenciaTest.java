package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import br.com.ccs.desafiojpadepositarsacar.utils.RunnableFutureTestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@TestPropertySource("/application-test.properties")
public class SaqueConcorrenciaTest {

    @Autowired
    private SaqueComponent saqueComponent;
    @Autowired
    private UsuarioRepository repository;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        repository.deleteAllInBatch();
        usuario = EntityTestFactory.getUsuario();
    }

    @Test
    void testeSacarPessimistaConcorrenteSucesso() {
        usuario = repository.save(usuario);
        assertDoesNotThrow(() -> RunnableFutureTestHelper.run(10, 15, () -> saqueComponent.sacarPessimista(usuario, BigDecimal.valueOf(10.00))));

        usuario = repository.findById(usuario.getId()).get();

        assertEquals(BigDecimal.valueOf(900.00).setScale(2), usuario.getSaldo().setScale(2));
    }

    @Test
    void testeSacarOtimistaConcorrenteSucesso() {
        usuario = repository.save(usuario);
        assertDoesNotThrow(() -> RunnableFutureTestHelper.run(10, 15, () -> saqueComponent.sacarOtimista(usuario, BigDecimal.valueOf(10.00))));

        usuario = repository.findById(usuario.getId()).get();

        assertEquals(BigDecimal.valueOf(900.00).setScale(2), usuario.getSaldo().setScale(2));
    }
}

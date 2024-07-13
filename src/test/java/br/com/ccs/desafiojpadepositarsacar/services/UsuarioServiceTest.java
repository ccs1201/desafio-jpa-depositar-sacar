package br.com.ccs.desafiojpadepositarsacar.services;

import br.com.ccs.desafiojpadepositarsacar.constants.DesafioJpaConstants;
import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.exceptions.DesafioJpaServiceException;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import br.com.ccs.desafiojpadepositarsacar.utils.messageUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class UsuarioServiceTest {

    @Autowired
    private UsuarioService service;
    @Autowired
    private UsuarioRepository usuarioRepository;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAllInBatch();
        usuario = EntityTestFactory.getUsuario();
    }

    @Test
    void testUsuarioNaoEncontrado() {
        var exception = assertThrows(DesafioJpaServiceException.class, () -> service.findById(1));
        assertEquals(messageUtil.getMessage(DesafioJpaConstants.ERRO_USUARIO_NAO_ENCONTRADO), exception.getMessage());
    }

    @Test
    void testUsuarioSalvoSucesso() {
        var actual = service.save(usuario);
        assertEquals(1, actual.getId());
        assertEquals(usuario.getNome(), actual.getNome());
        assertEquals(usuario.getSaldo(), actual.getSaldo());
    }

    @Test
    void testUsuarioSalvoComNomeRepetido() {
        service.save(usuario);

        var usuario2 = Usuario.builder()
                .nome(usuario.getNome())
                .saldo(BigDecimal.ZERO)
                .build();

        assertThrows(Exception.class, () -> service.save(usuario2));
    }

    @Test
    void testUsuarioSalvoComSaldoNegativo() {
        usuario.setSaldo(BigDecimal.valueOf(-1));
        assertThrows(Exception.class, () -> service.save(usuario));
    }
}
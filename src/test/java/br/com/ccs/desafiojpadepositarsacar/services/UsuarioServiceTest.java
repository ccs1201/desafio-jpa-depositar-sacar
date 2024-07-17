package br.com.ccs.desafiojpadepositarsacar.services;

import br.com.ccs.desafiojpadepositarsacar.constants.MessageConstants;
import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.exceptions.ServiceException;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import br.com.ccs.desafiojpadepositarsacar.utils.TranslationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource("/application-test.properties")
class UsuarioServiceTest {

    @Autowired
    private UsuarioService service;
    @Autowired
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository.deleteAllInBatch();
    }

    @Test
    void testUsuarioNaoEncontrado() {
        var exception = assertThrows(ServiceException.class, () -> service.findById(1));
        assertEquals(TranslationUtil.getMessage(MessageConstants.ERRO_USUARIO_NAO_ENCONTRADO), exception.getMessage());
    }

    @Test
    void testUsuarioSalvoSucesso() {
        var usuario = EntityTestFactory.getUsuario();
        var actual = service.save(usuario);
        assertNotNull(actual.getId());
        assertEquals(usuario.getNome(), actual.getNome());
        assertEquals(usuario.getSaldo(), actual.getSaldo());
    }

    @Test
    void testUsuarioSalvoComNomeRepetido() {
        var usuario = EntityTestFactory.getUsuario();
        service.save(usuario);

        var usuario2 = Usuario.builder()
                .nome(usuario.getNome())
                .saldo(BigDecimal.ZERO)
                .build();

        assertThrows(Exception.class, () -> service.save(usuario2));
    }

    @Test
    void testUsuarioSalvoComSaldoNegativo() {
        var usuario = EntityTestFactory.getUsuario();
        usuario.setSaldo(BigDecimal.valueOf(-1));
        assertThrows(Exception.class, () -> service.save(usuario));
    }
}
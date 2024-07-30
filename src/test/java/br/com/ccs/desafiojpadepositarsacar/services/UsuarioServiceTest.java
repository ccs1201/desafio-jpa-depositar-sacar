package br.com.ccs.desafiojpadepositarsacar.services;

import br.com.ccs.desafiojpadepositarsacar.constants.MessageConstants;
import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.exceptions.ServiceException;
import br.com.ccs.desafiojpadepositarsacar.factories.EntityTestFactory;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import br.com.ccs.desafiojpadepositarsacar.utils.TestContainerBase;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static br.com.ccs.desafiojpadepositarsacar.utils.TranslationUtil.getFormattedMessage;
import static br.com.ccs.desafiojpadepositarsacar.utils.TranslationUtil.getMessage;
import static org.junit.jupiter.api.Assertions.*;

class UsuarioServiceTest extends TestContainerBase {

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
        assertEquals(getMessage(MessageConstants.ERRO_USUARIO_NAO_ENCONTRADO), exception.getMessage());
    }

    @Test
    void testUsuarioSalvoSucesso() {
        var usuario = EntityTestFactory.getUsuario();
        var actual = service.save(usuario);
        assertNotNull(actual.getId());
        actual = service.findById(actual.getId());
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

        var ex = assertThrows(ServiceException.class, () -> service.save(usuario2));

        assertEquals(getFormattedMessage(MessageConstants.ERRO_USUARIO_NOME_JA_CADASTRADO, usuario2.getNome()), ex.getMessage());
    }

    @Test
    void testUsuarioSalvoComSaldoNegativo() {
        var usuario = EntityTestFactory.getUsuario();
        usuario.setSaldo(BigDecimal.valueOf(-1));
        var ex = assertThrows(ConstraintViolationException.class, () -> service.save(usuario));
        assertEquals("deve ser maior ou igual a 0", ex.getConstraintViolations().iterator().next().getMessage());
    }
}
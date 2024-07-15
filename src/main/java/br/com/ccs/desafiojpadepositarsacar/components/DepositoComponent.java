package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.constants.MessageConstants;
import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.entities.UsuarioVersion;
import br.com.ccs.desafiojpadepositarsacar.exceptions.DepositoException;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioService;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioVersionService;
import br.com.ccs.desafiojpadepositarsacar.utils.TranslationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DepositoComponent {

    private final UsuarioService usuarioService;
    private final UsuarioVersionService usuarioVersionService;


    @Transactional
    public void depositarPessimista(Usuario usuario, BigDecimal valor) {
        log.debug("Buscando usuário pelo id: {}.", usuario.getId());
        usuario = usuarioService.findByIdLockPessimista(usuario.getId());
        log.debug("Adicionando valor ao saldo do usuário.");
        usuario.setSaldo(usuario.getSaldo().add(valor));
        log.debug("Saldo atualizado {}", usuario.getSaldo());
        log.debug("Salvando usuário.");
        usuarioService.save(usuario);
        log.debug("Usuario Salvo com Sucesso. {}", usuario);
        log.debug("Deposito efetuado com sucesso.");
    }

    @Transactional
    public void depositarOtimista(UsuarioVersion usuario, BigDecimal valor) {
        try {
            log.debug("Buscando usuário pelo id: {}.", usuario.getId());
            usuario = usuarioVersionService.findById(usuario.getId());
            log.debug("Adicionando valor ao saldo do usuário.");
            usuario.setSaldo(usuario.getSaldo().add(valor));
            log.debug("Saldo atualizado {}", usuario.getSaldo());
            log.debug("Salvando usuário.");
            usuarioVersionService.save(usuario);
            log.debug("Usuario Salvo com Sucesso. {}", usuario);
            log.debug("Deposito efetuado com sucesso.");
        } catch (ObjectOptimisticLockingFailureException e) {
            log.error(e.getMessage());
            throw new DepositoException(TranslationUtil.getMessage(MessageConstants.ERRO_AO_DEPOSITAR), e);
        }
    }
}

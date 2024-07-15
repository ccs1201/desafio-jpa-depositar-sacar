package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.entities.UsuarioVersion;
import br.com.ccs.desafiojpadepositarsacar.exceptions.SaqueException;
import br.com.ccs.desafiojpadepositarsacar.exceptions.TransacaoFinanceiraException;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioService;
import br.com.ccs.desafiojpadepositarsacar.services.UsuarioVersionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static br.com.ccs.desafiojpadepositarsacar.validator.TransacaoFinanceiraValidator.validarValorTransacao;

@Component
@Slf4j
@RequiredArgsConstructor
@Lazy
public class SaqueComponent {

    private final UsuarioService usuarioService;
    private final UsuarioVersionService usuarioVersionService;

    @Transactional
    public void sacarPessimista(Usuario usuario, final BigDecimal valor) {
        log.debug("Iniciando saque Pessimista");
        try {
            log.debug("Buscando usuário pelo id: {}.", usuario.getId());
            usuario = usuarioService.findByIdLockPessimista(usuario.getId());
            log.debug("Usuario encontrado: {}", usuario);
            validarValorTransacao(usuario.getSaldo(), valor);
            log.debug("Retirando valor do saldo do usuário.");
            usuario.setSaldo(usuario.getSaldo().subtract(valor));
            log.debug("Saldo atualizado {}", usuario.getSaldo());
            log.debug("Salvando usuário.");
            usuarioService.save(usuario);
            log.debug("Usuario Salvo com Sucesso. {}", usuario);
            log.debug("Saque efetuado com sucesso.");
        } catch (TransacaoFinanceiraException e) {
            log.error(e.getMessage());
            throw new SaqueException(e.getMessage());
        }
    }


    @Transactional
    public void sacarOtimista(UsuarioVersion usuario, final BigDecimal valor) {
        log.debug("Iniciando saque Otimista");
        try {
            log.debug("Buscando usuário pelo id: {}.", usuario.getId());
            usuario = usuarioVersionService.findById(usuario.getId());
            log.debug("Usuario encontrado: {}", usuario);
            log.debug("Retirando valor do saldo do usuário.");
            validarValorTransacao(usuario.getSaldo(), valor);
            usuario.setSaldo(usuario.getSaldo().subtract(valor));
            log.debug("Saldo atualizado {}", usuario.getSaldo());
            log.debug("Salvando usuário.");
            usuario = usuarioVersionService.save(usuario);
            log.debug("Usuario Salvo com Sucesso. {}", usuario);
            log.debug("Saque efetuado com sucesso.");
        } catch (TransacaoFinanceiraException | ObjectOptimisticLockingFailureException e) {
            log.error(e.getMessage());
            throw new SaqueException(e.getMessage(), e);
        }
    }
}

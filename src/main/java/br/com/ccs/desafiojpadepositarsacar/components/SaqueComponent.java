package br.com.ccs.desafiojpadepositarsacar.components;

import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.exceptions.DesafioJpaSaqueException;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

import static br.com.ccs.desafiojpadepositarsacar.validator.SaldoValidator.validarValorTransacao;

@Component
@Slf4j
@RequiredArgsConstructor
public class SaqueComponent {

    private final UsuarioRepository repository;

    @Transactional
    void sacarPessimista(Usuario usuario, BigDecimal valor) {
        try {
            repository.findByIdLockPessimista(usuario.getId());
            validarValorTransacao(usuario.getSaldo(), valor);
            usuario.setSaldo(usuario.getSaldo().subtract(valor));
            repository.save(usuario);
        } catch (DesafioJpaSaqueException e) {
            log.error(e.getMessage());
            throw new DesafioJpaSaqueException(e.getMessage());
        }
    }

    @Transactional
    void sacarOtimista(Usuario usuario, BigDecimal valor) {
        try {
            repository.findById(usuario.getId());
            validarValorTransacao(usuario.getSaldo(), valor);
            usuario.setSaldo(usuario.getSaldo().subtract(valor));
            repository.save(usuario);
        } catch (DesafioJpaSaqueException e) {
            log.error(e.getMessage());
            throw new DesafioJpaSaqueException(e.getMessage());
        }
    }


}

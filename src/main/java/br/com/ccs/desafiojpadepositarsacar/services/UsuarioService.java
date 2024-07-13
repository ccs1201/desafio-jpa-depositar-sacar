package br.com.ccs.desafiojpadepositarsacar.services;

import br.com.ccs.desafiojpadepositarsacar.constants.DesafioJpaConstants;
import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.exceptions.DesafioJpaServiceException;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioRepository;
import br.com.ccs.desafiojpadepositarsacar.utils.messageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario save(Usuario usuario) {
        try {
            return usuarioRepository.save(usuario);
        } catch (Exception e) {
            log.error("Erro ao salvar usuário.", e);
                throw new DesafioJpaServiceException(
                        messageUtil.getFormattedMessage(DesafioJpaConstants.ERRO_USUARIO_NOME_JA_CADASTRADO, usuario.getNome()));
        }
    }

    @Transactional(readOnly = true)
    public Usuario findById(Integer id) {
        return usuarioRepository.findById(id).orElseThrow(
                () -> new DesafioJpaServiceException(
                        messageUtil.getMessage(DesafioJpaConstants.ERRO_USUARIO_NAO_ENCONTRADO)));
    }
}

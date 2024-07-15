package br.com.ccs.desafiojpadepositarsacar.services;

import br.com.ccs.desafiojpadepositarsacar.constants.MessageConstants;
import br.com.ccs.desafiojpadepositarsacar.entities.UsuarioVersion;
import br.com.ccs.desafiojpadepositarsacar.exceptions.ServiceException;
import br.com.ccs.desafiojpadepositarsacar.repostitories.UsuarioVersionRepository;
import br.com.ccs.desafiojpadepositarsacar.utils.TranslationUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioVersionService {

    private final UsuarioVersionRepository usuarioVersionRepository;

    @Transactional
    public UsuarioVersion save(UsuarioVersion usuario) {
        try {
            return usuarioVersionRepository.saveAndFlush(usuario);
        } catch (DataIntegrityViolationException e) {
            log.error("Erro ao salvar usuário.", e);
            throw new ServiceException(
                    TranslationUtil.getFormattedMessage(MessageConstants.ERRO_USUARIO_NOME_JA_CADASTRADO, usuario.getNome()));
        }
    }

    @Transactional(readOnly = true)
    public UsuarioVersion findById(Integer id) {
        return usuarioVersionRepository.findById(id).orElseThrow(
                () -> new ServiceException(
                        TranslationUtil.getMessage(MessageConstants.ERRO_USUARIO_NAO_ENCONTRADO)));
    }
}

package br.com.ccs.desafiojpadepositarsacar.constants;

import lombok.Getter;

@Getter
public enum DesafioJpaConstants {

    ERRO_USUARIO_NAO_ENCONTRADO("Usuário não encontrado."),
    ERRO_USUARIO_NOME_JA_CADASTRADO("Usuário com nome %s já cadastrado."),
    ERRO_AO_SALVAR_USUARIO("Ocorreu um erro ao salvar o usuário."),
    ERRO_SALDO_INSUFICIENTE("Saldo insuficiente."),
    ERRO_VALOR_TRANSACAO_INVALIDO("Valor saque inválido, o valor do saque deve ser maior que zero.");

    private final String mensagem;

    DesafioJpaConstants(String mensagem) {
        this.mensagem = mensagem;
    }
}

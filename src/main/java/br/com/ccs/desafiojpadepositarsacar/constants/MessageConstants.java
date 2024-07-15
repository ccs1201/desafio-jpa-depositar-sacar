package br.com.ccs.desafiojpadepositarsacar.constants;

import lombok.Getter;

@Getter
public enum MessageConstants {

    ERRO_USUARIO_NAO_ENCONTRADO("Usuário não encontrado."),
    ERRO_USUARIO_NOME_JA_CADASTRADO("Usuário com nome %s já cadastrado."),
    ERRO_AO_SALVAR_USUARIO("Ocorreu um erro ao salvar o usuário."),
    ERRO_SALDO_INSUFICIENTE("Saldo insuficiente."),
    ERRO_VALOR_TRANSACAO_INVALIDO("Valor saque inválido, o valor do saque deve ser maior que zero."),
    ERRO_AO_DEPOSITAR("Ocorreu um erro inesperado ao tentar efetuar o deposito, por favor aguarde alguns instantes e tente novamente.");

    private final String mensagem;

    MessageConstants(String mensagem) {
        this.mensagem = mensagem;
    }
}

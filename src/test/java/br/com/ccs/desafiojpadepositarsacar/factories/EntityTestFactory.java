package br.com.ccs.desafiojpadepositarsacar.factories;

import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import br.com.ccs.desafiojpadepositarsacar.entities.UsuarioVersion;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;
import java.math.RoundingMode;

@UtilityClass
public class EntityTestFactory {

    public static Usuario getUsuario() {
        return Usuario.builder()
                .nome("John Doe da Silva")
                .saldo(BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    public static Usuario getUsuario(BigDecimal saldo) {
        return Usuario.builder()
                .nome("John Doe da Silva")
                .saldo(saldo.setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    public static UsuarioVersion getUsuarioVersion() {
        return UsuarioVersion.builder()
                .nome("John Doe da Silva")
                .saldo(BigDecimal.valueOf(1000).setScale(2, RoundingMode.HALF_UP))
                .build();
    }

    public static UsuarioVersion getUsuarioVersion(BigDecimal saldo) {
        return UsuarioVersion.builder()
                .nome("John Doe da Silva")
                .saldo(saldo.setScale(2, RoundingMode.HALF_UP))
                .build();
    }
}

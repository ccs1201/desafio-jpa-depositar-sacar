package br.com.ccs.desafiojpadepositarsacar.factories;

import br.com.ccs.desafiojpadepositarsacar.entities.Usuario;
import lombok.experimental.UtilityClass;

import java.math.BigDecimal;

@UtilityClass
public class EntityTestFactory {

    public static Usuario getUsuario() {
        return Usuario.builder()
                .nome("John Doe da Silva")
                .saldo(BigDecimal.valueOf(1000))
                .build();
    }

    public static Usuario getUsuario(BigDecimal saldo) {
        return Usuario.builder()
                .nome("John Doe da Silva")
                .saldo(saldo)
                .build();
    }
}

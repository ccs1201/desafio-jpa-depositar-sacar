package br.com.ccs.desafiojpadepositarsacar.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

@Entity
@DynamicUpdate
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotEmpty
    @Size(min = 3, max = 100)
    @Column(nullable = false, unique = true, length = 150)
    private String nome;

    @PositiveOrZero
    @Column(precision = 12, scale = 2, nullable = false)
    private BigDecimal saldo;

    @Version
    @Setter(AccessLevel.NONE)
    private Short version;
}

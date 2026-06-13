package com.gilberto.usuario.business.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TelefoneDTO {
    private String numero;
    private String ddd;
}

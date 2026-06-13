package com.gilberto.usuario.business;

import com.gilberto.usuario.business.converter.UsuarioConverter;
import com.gilberto.usuario.business.dto.UsuarioDTO;
import com.gilberto.usuario.entity.Usuario;
import com.gilberto.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private  final UsuarioConverter usuarioConverter;

    public UsuarioDTO salvar (UsuarioDTO usuarioDTO){
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuario);
    }
}

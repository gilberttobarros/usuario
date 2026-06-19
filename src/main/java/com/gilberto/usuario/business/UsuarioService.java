package com.gilberto.usuario.business;

import com.gilberto.usuario.business.converter.UsuarioConverter;
import com.gilberto.usuario.business.dto.EnderecoDTO;
import com.gilberto.usuario.business.dto.TelefoneDTO;
import com.gilberto.usuario.business.dto.UsuarioDTO;
import com.gilberto.usuario.entity.Endereco;
import com.gilberto.usuario.entity.Telefone;
import com.gilberto.usuario.entity.Usuario;
import com.gilberto.usuario.infrastructure.security.JwtUtil;
import com.gilberto.usuario.infrastructure.security.exceptions.ConflictException;
import com.gilberto.usuario.infrastructure.security.exceptions.ResourceNotFoundException;
import com.gilberto.usuario.repository.EnderecoRepository;
import com.gilberto.usuario.repository.TelefoneRepository;
import com.gilberto.usuario.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private  final UsuarioConverter usuarioConverter;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EnderecoRepository enderecoRepository;
    private final TelefoneRepository telefoneRepository;

    public UsuarioDTO salvar (UsuarioDTO usuarioDTO){
        emailExiste(usuarioDTO.getEmail());
        usuarioDTO.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuario = usuarioConverter.paraUsuario(usuarioDTO);
        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public void emailExiste(String email) {
        try {
            boolean existe = verificaEmailExistente(email);
            if (existe) {
                throw new ConflictException("Email já cadastrado " + email);
            }
        } catch (ConflictException e) {
            throw new ConflictException("Email já cadastrado ", e.getCause());
        }
    }

    public boolean verificaEmailExistente(String email) {
        return usuarioRepository.existsByEmail(email);
    }

    public UsuarioDTO buscarUsuarioPorPorEmail (String email) {
        try {
            return usuarioConverter.paraUsuarioDTO(usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new ResourceNotFoundException("Email nao encontrado")));
        } catch (ResourceNotFoundException e) {
            throw new ResourceNotFoundException("Email não encontrado" + email);
        }

    }

    public void deletaUsuarioPorEmail (String email){
        usuarioRepository.deleteByEmail(email);
    }

    public UsuarioDTO atualizaDadosUsuario (String token, UsuarioDTO usuarioDTO){
        //aqui buscamos o email do usuario através do token(tirar a obrigatoriedade do email)
        String email = jwtUtil.extrairEmailToken(token.substring(7));

        //fazendo a criptografia de senha
        usuarioDTO.setSenha(usuarioDTO.getSenha() != null? passwordEncoder.encode(usuarioDTO.getSenha()) : null );

        //busca os dados do usuario no banco de dados
        Usuario usuarioEntity = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Email nao encontrado"));

        //mesclou os dados que recebemos na requisição dto com os dados do banco de dados
        Usuario usuario = usuarioConverter.updateUsuario(usuarioDTO, usuarioEntity);

        return usuarioConverter.paraUsuarioDTO(usuarioRepository.save(usuario));
    }

    public EnderecoDTO atualizaEnderecoUsuario (Long id, EnderecoDTO enderecoDTO){
        Endereco entity = enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID nao encontrado"));
        Endereco endereco = usuarioConverter.updateEndereco(enderecoDTO, entity);
        return usuarioConverter.paraEnderecoDTO(enderecoRepository.save(endereco));
    }

    public TelefoneDTO  atualizaTelefoneUsuario (Long id, TelefoneDTO telefoneDTO){
        Telefone telefoneEntity = telefoneRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("ID nao encontrado"));
        Telefone telefone = usuarioConverter.updateTelefone(telefoneDTO, telefoneEntity);
        return usuarioConverter.paraTelefoneDTO(telefoneRepository.save(telefone));
    }
}

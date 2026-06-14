package com.gilberto.usuario.controller;

import com.gilberto.usuario.business.UsuarioService;
import com.gilberto.usuario.business.dto.UsuarioDTO;
import com.gilberto.usuario.entity.Usuario;
import com.gilberto.usuario.infrastructure.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
@RequiredArgsConstructor
public class UsuarioContoller {
    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<UsuarioDTO> salvarUsuario (@RequestBody UsuarioDTO usuarioDTO){
        return ResponseEntity.ok(usuarioService.salvar(usuarioDTO));
    }

    @PostMapping("/login")
    public String login (@RequestBody UsuarioDTO usuarioDTO){
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(usuarioDTO.getEmail()
                        ,usuarioDTO.getSenha())
                );
                return "Bearer "+ jwtUtil.generateToken(authentication.getName());
    }

    @GetMapping
    public ResponseEntity<Usuario>buscarUsuarioPorEmail (@RequestParam ("email") String email){
        return ResponseEntity.ok(usuarioService.buscarUsuarioPorPorEmail(email));
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<Void>deletarUsuarioPorEmail(@PathVariable String email){
        usuarioService.deletaUsuarioPorEmail(email);
        return ResponseEntity.ok().build();
    }

}

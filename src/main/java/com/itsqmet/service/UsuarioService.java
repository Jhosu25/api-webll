package com.itsqmet.service;

import com.itsqmet.role.Role;
import com.itsqmet.component.JwtUtil;
import com.itsqmet.entity.Usuario;
import com.itsqmet.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UsuarioService implements UserDetailsService {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public List<Usuario> getUsuarios() {
        return usuarioRepository.findAll();
    }

    public Usuario getUsuarioById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }

    public Usuario createUsuario(Usuario usuario) {

        // Validar que no exista el email
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new RuntimeException("El email ya está registrado");
        }

        // Encriptar contraseña
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        // Asignar rol por defecto si no viene
        if (usuario.getRol() == null) {
            usuario.setRol(Role.ROLE_EMPLEADO);
        }

        return usuarioRepository.save(usuario);
    }

    public Usuario updateUsuario(Long id, Usuario usuario) {

        Usuario usuarioExistente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Actualizar nombre
        usuarioExistente.setNombre(usuario.getNombre());

        // Actualizar email
        usuarioExistente.setEmail(usuario.getEmail());

        // Actualizar rol SOLO si viene en el request
        if (usuario.getRol() != null) {
            usuarioExistente.setRol(usuario.getRol());
        }

        // Actualizar contraseña SOLO si viene
        if (usuario.getPassword() != null && !usuario.getPassword().trim().isEmpty()) {
            usuarioExistente.setPassword(passwordEncoder.encode(usuario.getPassword()));
        }

        return usuarioRepository.save(usuarioExistente);
    }

    public void deleteUsuario(Long id) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuarioRepository.delete(usuario);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return User.builder()
                .username(usuario.getEmail())
                .password(usuario.getPassword())
                .authorities(usuario.getRol().name())
                .build();
    }

    // NUEVO
    // LOGIN - TOKEN
    public Map<String, String> autenticar(Usuario loginUsuario) {
        Optional<Usuario> usuarioEncontrado = usuarioRepository.findByEmail(loginUsuario.getEmail());
        if (usuarioEncontrado.isPresent()) {
            Usuario usuario = usuarioEncontrado.get();
            // Verificamos la contraseña
            if (passwordEncoder.matches(loginUsuario.getPassword(), usuario.getPassword())) {
                String token = jwtUtil.generarToken(usuario.getEmail(), usuario.getRol().name());
                Map<String, String> response = new HashMap<>();
                response.put("token", token);
                response.put("email", usuario.getEmail());
                response.put("rol", usuario.getRol().name());
                return response;
            }
        }
        // Si usuario no existe o contraseña inválida
        return null;
    }
}
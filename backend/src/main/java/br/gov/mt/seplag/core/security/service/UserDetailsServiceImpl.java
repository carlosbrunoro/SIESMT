package br.gov.mt.seplag.core.security.service;

import br.gov.mt.seplag.core.exception.DomainException;
import br.gov.mt.seplag.core.message.MessageService;
import br.gov.mt.seplag.entity.Usuario;
import br.gov.mt.seplag.service.usuario.UsuarioService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UsuarioService usuarioService;
    private final MessageService messageService;

    public UserDetailsServiceImpl(final UsuarioService usuarioService,
                                  final MessageService messageService) {
        this.messageService = messageService;
        this.usuarioService = usuarioService;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(final String username) {
        final Usuario usuario = usuarioService.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(messageService.toLocale("auth.user.not.found", username)));

        switch (usuario.getStatus()) {
            case INATIVO -> throw DomainException.unauthorized("auth.user.inactive");
            case BLOQUEADO -> throw DomainException.unauthorized("auth.user.blocked");
            default -> {
                // ATIVO → segue o fluxo normal
            }
        }

        final Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));

        return User.builder()
            .username(usuario.getUsername())
            .password(usuario.getSenha())
            .authorities(authorities)
            .build();
    }

}
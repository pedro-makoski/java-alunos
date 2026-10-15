package com.alunos.scholl.domain.user.model;

import com.alunos.scholl.domain.BaseEntity;
import com.alunos.scholl.domain.professor.model.Professor;
import com.alunos.scholl.infra.security.Authorities;
import jakarta.persistence.*;
import lombok.*;
import org.jspecify.annotations.Nullable;
import org.springframework.security.config.core.GrantedAuthorityDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;

    String email;

    String senha;

    LocalDate dataNascimento;

    String nome;

    String sobrenome;

    @OneToOne(mappedBy = "userRelated", fetch = FetchType.EAGER)
    Professor professor;

    @Override
    @Transient
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = (professor == null) ? Authorities.PROFESSOR.name : Authorities.DONO.name;
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    @Transient
    public @Nullable String getPassword() {
        return this.senha;
    }

    @Override
    @Transient
    public String getUsername() {
        return this.id.toString();
    }

    @Override
    public boolean isEnabled() {
        return super.getIsDeleted();
    }
}

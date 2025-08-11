package ru.otus.hw.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.FetchType;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Data;
import lombok.ToString;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class CustomUser implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Стратегия генерации идентификаторов
    @Column(name = "id")
    private long id;


    @Column(name = "Username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "Password", nullable = false, length = 100)
    private String password;

    @Column(name = "EMail", nullable = false, length = 50)
    @Email(message = "Email should be valid")
    private String email;

    @Column(name = "inform_about_tasks")
    private boolean informAboutTasks;


    @Fetch(FetchMode.SUBSELECT)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private List<Role> roles;
/*
    @Fetch(FetchMode.SUBSELECT)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
    @JoinTable(name = "users_products",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products;

 */



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.getName()))
                .toList();
    }
}


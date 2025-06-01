package com.myproject.simpleonlineshop.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "t_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Cart cart;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    // if you care about history of orders, don't use CascadeType.REMOVE
    private List<Order> orders;

    @ManyToMany( fetch = FetchType.EAGER, cascade = {CascadeType.DETACH, CascadeType.PERSIST, CascadeType.PERSIST, CascadeType.REFRESH})
    //Detaching removes the entity from the current persistence context (e.g., EntityManager), and changes won't be tracked.
    //When you refresh the parent entity (reload from DB), all child entities are also refreshed.
    //Useful if you suspect data is stale or changed in the database.
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), // id field of  User Table
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id") // this id is field of Role table
    )
    private Collection<Role> roles = new HashSet<>();
}

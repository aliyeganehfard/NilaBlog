package com.nila.blog.database.model

import jakarta.persistence.*

@Entity
@Table(name = "USERS")
data class User(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "username", nullable = false, unique = true)
    var username: String = "",

    @Column(name = "email", nullable = false, unique = true)
    var email: String = "",

    @Column(name = "password", nullable = false, unique = true)
    var password: String = "",

    @Lob
    @Column(name = "profile_pictures")
    var profilePictures: String? = null,

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var posts: List<BlogPost> = mutableListOf(),

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: List<Comment> = mutableListOf(),

    )

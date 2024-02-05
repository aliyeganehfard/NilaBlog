package com.nila.blog.database.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "blog_post")
data class BlogPost(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "title", nullable = false)
    var title: String = "",

    @Column(name = "content", nullable = false)
    var content: String = "",

    @Column(name = "created_at", nullable = false)
    var createdAt: Date? = null,

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var user: User? = null,

    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    var comments: List<Comment> = mutableListOf(),
) {

    @PrePersist
    fun prePersist() {
        this.createdAt = Date();
    }
}

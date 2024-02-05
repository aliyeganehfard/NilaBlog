package com.nila.blog.database.model

import jakarta.persistence.*
import java.util.*

@Entity
@Table(name = "comment")
data class Comment(

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "text", nullable = false)
    var text: String = "",

    @Column(name = "created_at", nullable = false)
    var createdAt: Date? = null,

    @JoinColumn(name = "user_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var user: User? = null,

    @JoinColumn(name = "post_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.PERSIST, CascadeType.MERGE])
    var post: BlogPost? = null,
) {

    @PrePersist
    fun prePersist() {
        this.createdAt = Date();
    }
}

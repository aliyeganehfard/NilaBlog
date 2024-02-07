package com.nila.blog.database.model

import com.nila.blog.database.model.converter.ListConverter
import com.nila.blog.database.model.enums.PostCategory
import jakarta.persistence.*
import org.hibernate.Hibernate
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

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    var category: PostCategory? = null,

    @Convert(converter = ListConverter::class)
    @Column(name = "keywords", nullable = false)
    var keywords: List<String> = mutableListOf(),

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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BlogPost

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }
}

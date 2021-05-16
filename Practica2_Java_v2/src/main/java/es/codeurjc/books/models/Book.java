package es.codeurjc.books.models;

import lombok.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;


@Entity
@Table(name = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "comments")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String summary;

    @Column(nullable = false)
    private String author;

    @Column(nullable = false)
    private String publisher;

    @Column(name = "publication_year", nullable = false)
    private int publicationYear;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book", cascade = CascadeType.ALL)
    private Collection<Comment> comments = Collections.emptyList();

    public float getScore() {
        float score = 0;
        for (Comment comment : this.comments) {
            score += comment.getScore();
        }
        return this.comments.isEmpty() ? score : score / this.comments.size();
    }

}

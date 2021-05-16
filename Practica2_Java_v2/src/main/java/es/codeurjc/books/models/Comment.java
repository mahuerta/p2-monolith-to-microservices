package es.codeurjc.books.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "comments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String comment;

    private float score;

    @ManyToOne
    private Book book;

    @ManyToOne
    private User user;

    @Column(name = "user_ms_id")
    private Long userMSId;
}

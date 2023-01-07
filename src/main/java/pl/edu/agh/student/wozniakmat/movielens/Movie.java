package pl.edu.agh.student.wozniakmat.movielens;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@ToString
@Entity
@Table(name = "movies")
public class Movie {
    @Id
    @Getter
    @Setter
    private Long id;
    @Getter
    @Setter
    private String title;

    @OneToMany(mappedBy = "movieGenreId.movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Getter
    @Setter
    private
    Set<MovieGenre> genreList = new HashSet<>();

    @OneToMany(mappedBy = "movie")

    @Getter
    @Setter
    private Set<Tag> tags = new HashSet<>();

    @OneToMany(mappedBy = "ratingId.movie")
    @Getter
    @Setter
    private Set<Rating> ratings = new HashSet<>();
}
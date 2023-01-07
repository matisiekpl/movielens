package pl.edu.agh.student.wozniakmat.movielens;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "ratings")

public class Rating {

    @Data
    @Embeddable
    public static class RatingId implements Serializable {
        @ManyToOne
        private Movie movie;
        @ManyToOne
        private User user;

        public void setUser(User user) {
            this.user = user;
        }

        public void setMovie(Movie movie) {
            this.movie = movie;
        }

        public RatingId(User user, Movie movie) {
            this.user = user;
            this.movie = movie;
        }

        public RatingId() {

        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Rating.RatingId)) return false;
            Rating.RatingId other = (Rating.RatingId) o;
            return Objects.equals(other.getUser(), this.getUser()) &&
                    Objects.equals(this.getMovie(), other.getMovie());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getUser().getId(), getMovie().getId());
        }

        public Movie getMovie() {
            return movie;
        }

        public User getUser() {
            return user;
        }
    }

    @EmbeddedId
    RatingId ratingId = new RatingId();

    void setUser(User user) {
        ratingId.setUser(user);
    }

    void setMovie(Movie movie) {
        ratingId.setMovie(movie);
    }

    @Getter
    @Setter
    double rating;
    @Getter
    @Setter
    Date date;
}
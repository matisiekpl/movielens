package pl.edu.agh.student.wozniakmat.movielens;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")

public class User {
    @Id
    @Getter
    @Setter
    Long id;
    @Getter
    @Setter
    String forename;
    @Getter
    @Setter
    String surname;
    @Getter
    @Setter
    String email;

    @OneToMany(mappedBy = "user")
    private Set<Tag> tags = new HashSet<>();
    @OneToMany(mappedBy = "ratingId.user")
    @Getter
    private Set<Rating> ratings = new HashSet<>();

    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append(forename);
        b.append(" ");
        b.append(surname);
        b.append(String.format("(%s,id=%d,ratings=%d, tags=%d)", email, id, ratings.size(), tags.size()));
        return b.toString();
    }

}
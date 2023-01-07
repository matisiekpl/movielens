package pl.edu.agh.student.wozniakmat.movielens;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "tags")
public class Tag {

    @Id
    @Getter
    @Setter
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @ToString.Exclude
    @ManyToOne
    @Getter
    @Setter
    User user;

    @ToString.Exclude
    @ManyToOne
    @Getter
    @Setter
    Movie movie;

    @Getter
    @Setter
    String tag;
    @Getter
    @Setter
    Date date;
}
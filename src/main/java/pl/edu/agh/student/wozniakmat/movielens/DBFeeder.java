package pl.edu.agh.student.wozniakmat.movielens;

import jakarta.persistence.Query;
import lombok.var;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import pl.edu.agh.student.wozniakmat.lab06.BadValueException;
import pl.edu.agh.student.wozniakmat.lab06.CSVReader;
import pl.edu.agh.student.wozniakmat.lab06.HeaderMissingException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class DBFeeder {
    static void feedUsers() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        Transaction t = ses.beginTransaction();

        CSVReader reader = null;
        try {
            reader = new CSVReader("users.csv");
            while (reader.next()) {
                User user = new User();
                user.setId(reader.getLong("userId"));
                user.setForename(reader.get("foreName"));
                user.setSurname(reader.get("surName"));
                user.setEmail(reader.get("email"));
                ses.save(user);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HeaderMissingException e) {
            e.printStackTrace();
        } catch (BadValueException e) {
            e.printStackTrace();
        }
        t.commit();
        ses.close();
    }

    static void feedMovies() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        Transaction t = ses.beginTransaction();

        CSVReader reader = null;
        try {
            reader = new CSVReader("movies.csv");
            while (reader.next()) {
                Movie movie = new Movie();
                movie.setId(reader.getLong("movieId"));
                movie.setTitle(reader.get("title"));
                Set<MovieGenre> movieGenreSet = new HashSet<>();
                for (String genre : reader.get("genres").split("\\|")) {
                    MovieGenre movieGenre = new MovieGenre();
                    movieGenre.setGenre(genre);
                    movieGenre.setMovie(movie);
                    movieGenreSet.add(movieGenre);
                }
                movie.setGenreList(movieGenreSet);
                ses.save(movie);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HeaderMissingException e) {
            e.printStackTrace();
        } catch (BadValueException e) {
            e.printStackTrace();
        }
        t.commit();
        ses.close();
    }

    static void feedTags() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        Transaction t = ses.beginTransaction();

        CSVReader reader = null;
        try {
            reader = new CSVReader("tags.csv");
            while (reader.next()) {
                Tag tag = new Tag();
                tag.setTag(reader.get("tag"));
                tag.setDate(new Date(reader.getLong("timestamp") * 1000));
                tag.setUser(ses.get(User.class, reader.getLong("userId")));
                tag.setMovie(ses.get(Movie.class, reader.getLong("movieId")));
                ses.merge(tag);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HeaderMissingException e) {
            e.printStackTrace();
        } catch (BadValueException e) {
            e.printStackTrace();
        }
        t.commit();
        ses.close();
    }

    static void feedRatings() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        Transaction t = ses.beginTransaction();

        CSVReader reader = null;
        try {
            reader = new CSVReader("ratings.csv");
            while (reader.next()) {
                Rating rating = new Rating();
                rating.setDate(new Date(reader.getLong("timestamp") * 1000));
                rating.setRating(reader.getDouble("rating"));
                rating.setUser(ses.get(User.class, reader.getLong("userId")));
                rating.setMovie(ses.get(Movie.class, reader.getLong("movieId")));
                ses.merge(rating);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (HeaderMissingException e) {
            e.printStackTrace();
        } catch (BadValueException e) {
            e.printStackTrace();
        }
        t.commit();
        ses.close();
    }

    static void deleteAll() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        Transaction t = ses.beginTransaction();

        Query query = ses.createQuery("delete Rating");
        int result = query.executeUpdate();
        if (result > 0) {
            System.out.println("Ratings were removed");
        }

        query = ses.createQuery("delete Tag");
        result = query.executeUpdate();
        if (result > 0) {
            System.out.println("Tags were removed");
        }

        query = ses.createQuery("delete MovieGenre ");
        result = query.executeUpdate();
        if (result > 0) {
            System.out.println("MovieGenres were removed");
        }

        query = ses.createQuery("delete Movie ");
        result = query.executeUpdate();
        if (result > 0) {
            System.out.println("Movies were removed");
        }

        query = ses.createQuery("delete User ");
        result = query.executeUpdate();
        if (result > 0) {
            System.out.println("Users were removed");
        }
        t.commit();
        ses.close();
    }

    static void check() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        for (var cls : Arrays.asList("User", "Movie", "MovieGenre", "Tag", "Rating")) {
            Query query = ses.createQuery("select count(*) from " + cls);
            Long count = (Long) query.getResultList().get(0);
            System.out.println(String.format("%s:%d", cls, count));
        }
        ses.close();
    }

    public static void main(String[] args) {
        deleteAll();
        feedMovies();
        feedUsers();
        feedTags();
        feedRatings();
        check();
    }
}

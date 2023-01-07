package pl.edu.agh.student.wozniakmat.movielens;

import jakarta.persistence.Query;
import lombok.var;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DbQueries {
    static void sampleQuery() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
//        Transaction t = ses.beginTransaction();
        long start = System.nanoTime();
        Query q = ses.createQuery("from Movie m where (select avg(r.rating) from Rating as r where r.ratingId.movie = m group by r.ratingId.movie) >= 4.6 and size(m.ratings) >= 5");
        List<Movie> movies = q.getResultList();
        long end = System.nanoTime();
        System.out.println("Czas DB:" + (end - start) / 1e6);
        System.out.println(movies.size());
        for (var m : movies) {
            System.out.println(m);
        }
//        t.commit();
        ses.close();
    }

    static void sampleQueryAsStream() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
//        Transaction t = ses.beginTransaction();
        long start = System.nanoTime();
        Query q = ses.createQuery("from Movie");
        List<Movie> movies = q.getResultList();
        movies = movies.stream().filter(m -> {
            return
                    m.getRatings().stream().mapToDouble(r -> r.getRating()).average().orElse(0.0) >= 4.6
                            && m.getRatings().size() >= 5;
        }).collect(Collectors.toList());
        long end = System.nanoTime();
        System.out.println("Czas stream:" + (end - start) / 1e6);

        System.out.println(movies.size());
        for (var m : movies) {
            System.out.println(m);
        }
//        t.commit();
        ses.close();
    }

    static void mostActiveUsers() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        long start = System.nanoTime();
        Query q = ses.createQuery("from User u order by size(u.ratings) desc limit 10");
        List<User> users = q.getResultList();
        long end = System.nanoTime();
        System.out.println("Czas DB:" + (end - start) / 1e6);
        System.out.println(users.size());
        for (var u : users) {
            System.out.println(u);
        }
        ses.close();
    }

    static void mostActiveUsersAsStream() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        long start = System.nanoTime();
        Query q = ses.createQuery("from User");
        List<User> users = q.getResultList();
        users = users.stream()
                .sorted(Comparator.comparing(u -> ((User) u).getRatings().size()).reversed())
                .limit(10)
                .collect(Collectors.toList());
        long end = System.nanoTime();
        System.out.println("Czas stream:" + (end - start) / 1e6);
        System.out.println(users.size());
        for (var m : users) {
            System.out.println(m);
        }
        ses.close();
    }

    static void dramaMovies() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        long start = System.nanoTime();
        Query q = ses.createQuery("from Movie m where (select count(*) from MovieGenre g where g.movieGenreId.movie=m and g.movieGenreId.genre='Drama')>0");
        q.setMaxResults(100);
        List<Movie> movies = q.getResultList();
        long end = System.nanoTime();
        System.out.println("Czas DB:" + (end - start) / 1e6);
        System.out.println(movies.size());
        for (var m : movies) {
            System.out.println(m);
        }
        ses.close();
    }

    static void dramaMoviesAsStream() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        long start = System.nanoTime();
        Query q = ses.createQuery("from Movie");
        List<Movie> movies = q.getResultList();
        movies = movies.stream()
                .filter(movie -> movie.getGenreList().stream().anyMatch(g -> {
                    return Objects.equals(g.movieGenreId.getGenre(), "Drama");
                }))
                .limit(100)
                .collect(Collectors.toList());
        long end = System.nanoTime();
        System.out.println("Czas stream:" + (end - start) / 1e6);
        System.out.println(movies.size());
        for (var m : movies) {
            System.out.println(m);
        }
        ses.close();
    }

    static void taggedByRichardOliver() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        long start = System.nanoTime();
        Query q = ses.createQuery("from Movie m join m.tags t join t.user u where u.forename='Richard' and u.surname='Oliver'");
        q.setMaxResults(100);
        List movies = q.getResultList();
        long end = System.nanoTime();
        System.out.println("Czas DB:" + (end - start) / 1e6);
        System.out.println(movies.size());
        for (int i = 0; i < movies.size(); i++) {
            System.out.println(movies.get(i));
        }
        ses.close();
    }

    static void taggedByRichardOliverAsStream() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
        long start = System.nanoTime();
        Query q = ses.createQuery("from Movie m");
        List movies = q.getResultList();
        movies = (List) movies.stream().filter(m ->
                ((Movie) m).getTags().stream().anyMatch(t ->
                        Objects.equals(t.getUser().forename, "Richard") && Objects.equals(t.getUser().surname, "Oliver")
                )).collect(Collectors.toList());
        long end = System.nanoTime();
        System.out.println("Czas stream:" + (end - start) / 1e6);
        System.out.println(movies.size());
        for (int i = 0; i < movies.size(); i++) {
            System.out.println(movies.get(i));
        }
        ses.close();
    }

}

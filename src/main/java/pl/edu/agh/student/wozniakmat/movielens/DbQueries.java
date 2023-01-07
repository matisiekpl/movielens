package pl.edu.agh.student.wozniakmat.movielens;

import jakarta.persistence.Query;
import lombok.var;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.List;

public class DbQueries {
    static void sampleQuery() {
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        Session ses = sessionFactory.openSession();
//        Transaction t = ses.beginTransaction();
        long start = System.nanoTime();
        Query q = ses.createQuery("from Movie m where (select avg(r.rating) from Rating as r where r.ratingId.movie = m group by r.ratingId.movie) >= 4.6 and m.ratings.size >= 5");
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

    public static void main(String[] args) {
        sampleQuery();
    }
}

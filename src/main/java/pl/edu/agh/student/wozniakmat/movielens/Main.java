package pl.edu.agh.student.wozniakmat.movielens;

import static pl.edu.agh.student.wozniakmat.movielens.DBFeeder.*;
import static pl.edu.agh.student.wozniakmat.movielens.DbQueries.*;

public class Main {
    public static void main(String[] args) {
        deleteAll();
        feedMovies();
        feedUsers();
        feedTags();
        feedRatings();
        check();

//        sampleQuery();
//        sampleQueryAsStream();
//
//        mostActiveUsers();
//        mostActiveUsersAsStream();

//        dramaMovies();
//        dramaMoviesAsStream();

        taggedByRichardOliver();
        taggedByRichardOliverAsStream();
    }
}

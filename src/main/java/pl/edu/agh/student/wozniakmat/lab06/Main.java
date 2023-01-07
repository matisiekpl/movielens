package pl.edu.agh.student.wozniakmat.lab06;

import java.io.IOException;
import java.util.Locale;

public class Main {
    public static void main(String[] args) throws IOException, BadValueException, HeaderMissingException {
        CSVReader reader = new CSVReader("titanic-part.csv",",",false);
        while(reader.next()){
            int id = reader.getInt("PassengerId");
            String name = reader.get("Name");
            double fare = reader.getDouble("Fare");
//            int id = reader.getInt(0);
//            String name = reader.get(3);
//            double fare = reader.getDouble(9);

            System.out.printf(Locale.US,"%d %s %f\n",id, name, fare);
        }
//        CSVReader reader = new CSVReader("elec.csv", ",", true);
//        while (reader.next()) {
//            System.out.println(reader.getDouble("period"));
//            System.out.println(reader.get("class"));
//        }
    }
}
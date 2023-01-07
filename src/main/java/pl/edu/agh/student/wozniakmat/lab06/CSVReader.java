package pl.edu.agh.student.wozniakmat.lab06;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {
    BufferedReader reader;
    String delimiter;
    boolean hasHeader;

    int fieldsCount;

    List<String> columnLabels = new ArrayList<>();
    Map<String, Integer> columnLabelsToInt = new HashMap<>();

    public CSVReader(String filename) throws IOException {
        this(filename, ",", true);
    }

    public CSVReader(String filename, String delimiter) throws IOException {
        this(filename, delimiter, true);
    }

    public CSVReader(String filename, String delimiter, boolean hasHeader) throws IOException {
        this(new FileReader(filename), delimiter, hasHeader);
    }

    public CSVReader(Reader r, String delimiter, boolean hasHeader) throws IOException {
        reader = new BufferedReader(r);
        this.delimiter = delimiter;
        this.hasHeader = hasHeader;
        if (hasHeader) parseHeader();
    }

    void parseHeader() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return;
        }
        String[] header = line.split(delimiter);
        for (int i = 0; i < header.length; i++) {
            columnLabels.add(header[i]);
            columnLabelsToInt.put(header[i], i);
        }
    }

    String[] current;

    public boolean next() {
        String line;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            return false;
        }
        if (line == null) return false;
        current = line.split(delimiter + "(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        if (fieldsCount < current.length) fieldsCount = current.length;
        return true;
    }

    public String get(String label) throws HeaderMissingException {
        if (!hasHeader) throw new HeaderMissingException();
        if (!columnLabels.contains(label)) throw new HeaderMissingException();
        if (current.length <= columnLabelsToInt.get(label)) return "";
        return current[columnLabelsToInt.get(label)];
    }

    public String get(int i) throws HeaderMissingException {
        if (fieldsCount <= i) throw new HeaderMissingException();
        if (current.length <= i) return "";
        return current[i];
    }

    public int getInt(String label) throws BadValueException, HeaderMissingException {
        if (get(label).length() == 0) throw new BadValueException();
        try {
            return Integer.parseInt(get(label));
        } catch (Exception e) {
            throw new BadValueException();
        }
    }

    public int getInt(int i) throws BadValueException, HeaderMissingException {
        if (get(i).length() == 0) throw new BadValueException();
        try {
            return Integer.parseInt(get(i));
        } catch (Exception e) {
            throw new BadValueException();
        }
    }

    public double getDouble(String label) throws BadValueException, HeaderMissingException {
        if (get(label).length() == 0) throw new BadValueException();
        try {
            return Double.parseDouble(get(label));
        } catch (Exception e) {
            throw new BadValueException();
        }
    }

    public double getDouble(int i) throws BadValueException, HeaderMissingException {
        if (get(i).length() == 0) throw new BadValueException();
        try {
            return Double.parseDouble(get(i));
        } catch (Exception e) {
            throw new BadValueException();
        }
    }

    public long getLong(String label) throws HeaderMissingException, BadValueException {
        if (get(label).length() == 0) throw new BadValueException();
        try {
            return Long.parseLong(get(label));
        } catch (Exception e) {
            throw new BadValueException();
        }
    }

    public long getLong(int i) throws BadValueException {
        try {
            return Long.parseLong(get(i));
        } catch (Exception e) {
            throw new BadValueException();
        }
    }

    public boolean isMissing(int i) throws HeaderMissingException {
        if (i >= fieldsCount) return false;
        return get(i).length() == 0;
    }

    public boolean isMissing(String label) throws HeaderMissingException {
        if (!columnLabels.contains(label)) return false;
        return isMissing(columnLabelsToInt.get(label));
    }

    public List<String> getColumnLabels() {
        return columnLabels;
    }

    public int getColumnCount() {
        return fieldsCount;
    }

    public int getRecordLength() {
        int sum = 0;
        for (String item : current) sum += item.length();
        return sum;
    }

    public LocalTime getTime(int i, String format) throws HeaderMissingException, BadValueException {
        if (get(i).length() == 0) throw new BadValueException();
        return LocalTime.parse(get(i), DateTimeFormatter.ofPattern(format));
    }

    public LocalTime getTime(String label, String format) throws HeaderMissingException, BadValueException {
        if (get(label).length() == 0) throw new BadValueException();
        return LocalTime.parse(get(label), DateTimeFormatter.ofPattern(format));
    }

    public LocalDate getDate(int i, String format) throws HeaderMissingException, BadValueException {
        if (get(i).length() == 0) throw new BadValueException();
        return LocalDate.parse(get(i), DateTimeFormatter.ofPattern(format));
    }

    public LocalDate getDate(String label, String format) throws HeaderMissingException, BadValueException {
        if (get(label).length() == 0) throw new BadValueException();
        return LocalDate.parse(get(label), DateTimeFormatter.ofPattern(format));
    }

}
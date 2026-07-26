package Junit;

import java.util.HashMap;
import java.util.Map;

public class Day7_DatabaseService {

    private Map<String, String> records = new HashMap<>();

    public void connect() {
        System.out.println("Database connection established");
    }

    public void disconnect() {
        System.out.println("Database connection closed");
    }

    public void save(String key, String value) {
        records.put(key, value);
    }

    public String find(String key) {
        return records.get(key);
    }
}

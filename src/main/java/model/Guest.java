package model;

import java.util.List;
import java.util.UUID;

public class Guest {
    private UUID id;
    private String name;
    private String surname;

    public Guest(UUID id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Guest(String name, String surname) {
        this.name = name;
        this.surname = surname;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

//    public String toString() {
//        return id + " " + name + " " + surname;
//    }

    @Override
    public String toString() {
        String result = "{";
        if (id != null) result += "\"id\": \"" + id + "\",";
        result += "\"name\": \"" + name + "\",";
        result += "\"surname\": \"" + surname + "\"";
        result += "}";
        return result;
    }

    public static String guestsToString(List<Guest> guests) {
        StringBuilder jsonString = new StringBuilder("[");
        for (int i = 0; i < guests.size(); i++) {
            jsonString.append(guests.get(i).toString());
            if (i < guests.size() - 1) {
                jsonString.append(",");
            }
        }
        jsonString.append("]");
        return jsonString.toString();
    }
}

package model;

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

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String toString() {
        return id + " " + name + " " + surname;
    }
}

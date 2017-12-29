package com.instigatemobile.imessenger.models;

public class Persons {
    private String personName;
    private String personSurname;
    private int personImageId;

    public Persons(String personName, String personSurname, int personImageId) {
        this.personName = personName;
        this.personSurname = personSurname;
        this.personImageId = personImageId;
    }

    public String getPersonName() {
        return personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonSurname() {
        return personSurname;
    }

    public void setPersonSurname(String personSurname) {
        this.personSurname = personSurname;
    }

    public int getPersonImage() {
        return personImageId;
    }

    public void setPersonImage(int personImageId) {
        this.personImageId = personImageId;
    }
}
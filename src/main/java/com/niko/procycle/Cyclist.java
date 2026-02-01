package com.niko.procycle;

public class Cyclist {
    private String name;
    private int debut;
    private String team;
    private int wins;
    private String gender;
    private String specialty;
    private String nationality;

public Cyclist(String name, int debut, String team, int wins, String gender, String specialty, String nationality){
    this.name = name;
    this.debut = debut;
    this.team = team;
    this.wins = wins;
    this.gender = gender;
    this.specialty = specialty;
    this.nationality = nationality;
    }

public String getName(){
    return name;
}
public int getDebut(){
    return debut;
}
public String getTeam(){
    return team;
}
public int getWins(){
    return wins;
}
public String getGender(){
    return gender;
}
public String getSpecialty(){
    return specialty;
}
public String getNationality(){
    return nationality;
}
public String toString(){
        return getName() + " is the cyclist";
    }

public static void main(String[] args){
    Cyclist pogacar = new Cyclist("Tadej Pogacar",2019, "UAE-Team Emiretes", 105, "Male", "Everything", "Slovinia");
    System.out.println(pogacar.getName());
    System.out.println(pogacar.getDebut());
    System.out.println(pogacar.getTeam());
    System.out.println(pogacar.getWins());
    System.out.println(pogacar.getGender());
    System.out.println(pogacar.getSpecialty());
    System.out.println(pogacar.getNationality());
}


}
package com.niko.procycle;

import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDate;


public class GameService {
    ArrayList<Cyclist> cyclists = new ArrayList<>();

    public GameService() {
        cyclists.add(new Cyclist("Tadej Pogacar",2019, "UAE-Team Emiretes", 105, "Male", "All Arounder", "Slovinia"));
        cyclists.add(new Cyclist("Wout van Aert",2018, "Visma-Lease a Bike", 53, "Male", "Classics", "Belgium"));
        cyclists.add(new Cyclist("Jonas Vingegaard",2019, "Visma-Lease a Bike", 105, "Male", "Climber", "Denmark"));
        cyclists.add(new Cyclist("Marianne Vos",2006, "Visma-Lease a Bike", 250, "Female", "All Around", "Netherlands"));
        cyclists.add(new Cyclist("Tom Pidcock", 2021, "36.5 pro cycling", 10, "Male", "All Arounder", "United Kingdoms"));
        cyclists.add(new Cyclist("Demi vollering",2019, "FDJ-Suez", 55, "Female", "General Classification", "France"));
        cyclists.add(new Cyclist("Mathieu van der Poel",2019, "Aplecin-Premier Tech", 55, "Male", "Classics", "Netherlands"));
        cyclists.add(new Cyclist("Remco Evenepoel",2019, "Red Bull-Bora-Hansgrohe", 68, "Male", "Time Trial", "Belgium"));
    }

    public Cyclist getDailyCyclist(){
        LocalDate today = LocalDate.now();
        int seed = today.getYear() * 1000 + today.getDayOfYear();
        Random random = new Random(seed);
        int randomIndex = random.nextInt(cyclists.size());
        return cyclists.get(randomIndex);
    }
     public Cyclist getRandomCyclist(){
        Random random = new Random();
        int randomIndex = random.nextInt(cyclists.size());
        return cyclists.get(randomIndex);
    }

    public static void main(String[] args){
        GameService list = new GameService();
        System.out.println(list.getDailyCyclist());
        System.out.println(list.getRandomCyclist());
    }


}



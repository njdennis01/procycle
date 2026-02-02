package com.niko.procycle;

import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDate;
import java.util.Arrays;

public class GameService {
    ArrayList<Cyclist> cyclists = new ArrayList<>();
    ArrayList<Guess> Guesses = new ArrayList<>();

    public GameService() {
        cyclists.add(new Cyclist("Tadej Pogacar",2019, "UAE-Team Emiretes", 105, "Male", "All Arounder", "Slovinia"));
        cyclists.add(new Cyclist("Wout van Aert",2018, "Visma-Lease a Bike", 53, "Male", "Classics", "Belgium"));
        cyclists.add(new Cyclist("Jonas Vingegaard",2019, "Visma-Lease a Bike", 40, "Male", "Climber", "Denmark"));
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

    public String[] compareGuess(Cyclist guess, Cyclist answer){
        String[] result = new String[6];
        if (guess.getDebut() == answer.getDebut())
            result[0] = "Green";
        else if (guess.getDebut() >= (answer.getDebut() - 2) && guess.getDebut() <= (answer.getDebut()+ 2))
            result[0] = "Yellow";
            //Need to figure out how to add if it is more or less
        else result[0] = "Black";


        if (guess.getTeam().equals(answer.getTeam()))
            result[1] = "Green";
        else result[1] = "Black";


        if (guess.getWins() == answer.getWins())
            result[2] = "Green";
        else if (guess.getWins() >= (answer.getWins() - 5) && guess.getWins() <= (answer.getWins()+ 5))
            result[2] = "Yellow";
            //add more or less
        else result[2] = "Black";


        if (guess.getGender().equals(answer.getGender()))
            result[3] = "Green";
        else result[3] = "Black";


        if (guess.getSpecialty().equals(answer.getSpecialty()))
            result[4] = "Green";
        else result[4] = "Black";


        if (guess.getNationality().equals(answer.getNationality()))
            result[5] = "Green";
        else result[5] = "Black";
            //will add geographic location yellow later, not sure how to yet!
        System.out.println(Arrays.toString(result));
        return result;
    }

    public Cyclist findCyclistByName(String guess){
        for (Cyclist i: cyclists){
            if (i.getName().equals(guess)){
                System.out.println(i);
                return i;
            }
        }
        return null;
    }

    public ArrayList<Guess> getGuesses(){
        return Guesses;
    }

    public ArrayList<Guess> guessHistory(Guess oldGuess){
        Guesses.add(0, oldGuess);
        return Guesses;
    }
    public static void main(String[] args){
        GameService list = new GameService();
        System.out.println(list.getDailyCyclist());
        System.out.println(list.getRandomCyclist());
        Cyclist pogi = new Cyclist("Tadej Pogacar",2019, "UAE-Team Emiretes", 105, "Male", "All Arounder", "Slovinia");
        list.compareGuess(pogi, list.getDailyCyclist());
        list.compareGuess(pogi, list.getRandomCyclist());
        list.findCyclistByName("Wout van Aert");
    }


}



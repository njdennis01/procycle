package com.niko.procycle;

import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDate;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;

public class GameService {
    ArrayList<Cyclist> cyclists = new ArrayList<>();
    ArrayList<Guess> Guesses = new ArrayList<>();
    Cyclist currentAnswer;
    String mode;

    public GameService() {
        try {
            createCyclists();
        } catch (Exception e) {
            System.out.println("Error loading cyclists: " + e.getMessage());
        }
        mode = "Daily";
        currentAnswer = getDailyCyclist();
    }

    public void createCyclists() throws Exception {
        InputStream is = getClass().getClassLoader().getResourceAsStream("cyclists.csv");
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        br.readLine();                
        String line = br.readLine();
        while (line != null){
            String[] parts = line.split(",");
            cyclists.add(new Cyclist(parts[0], Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]), parts[4], parts[5], parts[6]));
            line = br.readLine();
        }
    }

    public Cyclist getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswerToRandom() {
        currentAnswer = getRandomCyclist();
    }
    
    public String getMode(){
        return mode;
    }

    public String setMode(){
        mode = "Unlimited";
        return mode;
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
            result[0] = "#F0C040";
            //Need to figure out how to add if it is more or less
        else result[0] = "Black";


        if (guess.getTeam().equals(answer.getTeam()))
            result[1] = "Green";
        else result[1] = "Black";


        if (guess.getWins() == answer.getWins())
            result[2] = "Green";
        else if (guess.getWins() >= (answer.getWins() - 5) && guess.getWins() <= (answer.getWins()+ 5))
            result[2] = "#F0C040";
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
    public void clearHistory(){
        Guesses.clear();
    }
    public static void main(String[] args){
    }


}



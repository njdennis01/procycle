package com.niko.procycle;
import java.util.ArrayList;
import java.util.Random;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.Serializable;

public class GameService implements Serializable  {
    private static final long serialVersionUID = 1L;
    ArrayList<Cyclist> cyclists = new ArrayList<>();
    ArrayList<Guess> Guesses = new ArrayList<>();
    Cyclist currentAnswer;
    String mode;
    String difficulty;
    String genderMode;
    String guessMode;
    ArrayList<String> northAmerica = new ArrayList<>(Arrays.asList("USA", "Canada", "Mexico", "Guatemala", "Honduras", "El Salvador", "Nicaragua", "Costa Rica", "Panama", "Belize", "Cuba", "Jamaica", "Haiti", "Dominican Republic", "Puerto Rico", "Trinidad and Tobago", "Bahamas", "Barbados", "Grenada", "Saint Lucia", "Saint Vincent and the Grenadines", "Antigua and Barbuda", "Dominica", "Saint Kitts and Nevis"));
    ArrayList<String> southAmerica = new ArrayList<>(Arrays.asList("Colombia", "Ecuador", "Brazil", "Argentina", "Chile", "Venezuela", "Uruguay", "Peru", "Bolivia", "Paraguay", "Guyana", "Suriname", "French Guiana"));
    ArrayList<String> africa = new ArrayList<>(Arrays.asList("South Africa", "Eritrea", "Ethiopia", "Algeria", "Morocco", "Rwanda", "Namibia", "Egypt", "Nigeria", "Kenya", "Ghana", "Tanzania", "Uganda", "Cameroon", "Ivory Coast", "Senegal", "Zimbabwe", "Zambia", "Botswana", "Mozambique", "Angola", "Tunisia", "Libya", "Sudan", "South Sudan", "DR Congo", "Mali", "Burkina Faso", "Niger", "Chad", "Somalia", "Madagascar", "Malawi", "Mauritius", "Togo", "Benin", "Sierra Leone", "Liberia", "Central African Republic", "Gabon", "Congo", "Equatorial Guinea", "Guinea", "Guinea-Bissau", "Gambia", "Lesotho", "Eswatini", "Djibouti", "Comoros", "Cape Verde", "Sao Tome and Principe", "Seychelles"));
    ArrayList<String> oceania = new ArrayList<>(Arrays.asList("Australia", "New Zealand", "Fiji", "Papua New Guinea", "Samoa", "Tonga", "Vanuatu", "Solomon Islands", "Micronesia", "Kiribati", "Marshall Islands", "Palau", "Nauru", "Tuvalu"));
    ArrayList<String> asia = new ArrayList<>(Arrays.asList("Japan", "Kazakhstan", "Uzbekistan", "Iran", "Israel", "China", "South Korea", "North Korea", "Taiwan", "India", "Pakistan", "Bangladesh", "Indonesia", "Philippines", "Vietnam", "Thailand", "Malaysia", "Singapore", "Myanmar", "Cambodia", "Laos", "Nepal", "Sri Lanka", "Afghanistan", "Iraq", "Saudi Arabia", "UAE", "Qatar", "Kuwait", "Bahrain", "Oman", "Yemen", "Jordan", "Lebanon", "Syria", "Turkey", "Georgia", "Armenia", "Azerbaijan", "Turkmenistan", "Tajikistan", "Kyrgyzstan", "Mongolia", "Bhutan", "Maldives", "Brunei", "Timor-Leste", "Palestine", "Cyprus"));
    ArrayList<String> westernEurope = new ArrayList<>(Arrays.asList("Great Britain", "Ireland", "France", "Belgium", "Netherlands", "Luxembourg", "Monaco", "Andorra"));
    ArrayList<String> easternEurope = new ArrayList<>(Arrays.asList("Romania", "Bulgaria", "Moldova", "Ukraine", "Belarus", "Russia"));
    ArrayList<String> northernEurope = new ArrayList<>(Arrays.asList("Denmark", "Norway", "Sweden", "Finland", "Iceland", "Estonia", "Latvia", "Lithuania"));
    ArrayList<String> southernEurope = new ArrayList<>(Arrays.asList("Spain", "Portugal", "Italy", "San Marino", "Malta", "Greece", "Cyprus", "Albania", "North Macedonia", "Croatia", "Bosnia and Herzegovina", "Montenegro", "Serbia", "Kosovo", "Andorra"));
    ArrayList<String> centralEurope = new ArrayList<>(Arrays.asList("Germany", "Austria", "Switzerland", "Poland", "Czech Republic", "Slovakia", "Hungary", "Liechtenstein", "Slovenia"));

    public GameService() {
        try {
            createCyclists();
        } catch (Exception e) {
            System.out.println("Error loading cyclists: " + e.getMessage());
        }
        genderMode = "Both";
        mode = "Daily";
        difficulty = "Hard";
        guessMode = "Limited";
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
            cyclists.add(new Cyclist(parts[0], Integer.parseInt(parts[1]), parts[2], Integer.parseInt(parts[3]), parts[4], parts[5], parts[6], parts[7]));
            line = br.readLine();
        }
    }

    public boolean matchesDifficulty(Cyclist c){
        if(difficulty.equals("Hard")){
            return true;
        }
        if(difficulty.equals("Medium")){
            if(c.getDifficulty().equals("Medium") || c.getDifficulty().equals("Easy") || c.getDifficulty().equals("Noob")){
                return true;
            }
        }
        if(difficulty.equals("Easy")){
            if(c.getDifficulty().equals("Easy") || c.getDifficulty().equals("Noob")){
                return true;
            }
        }
        if(difficulty.equals("Noob")){
            if(c.getDifficulty().equals("Noob")){
                return true;
            }
        }
        return false;
    }

    public boolean matchesGenderMode(Cyclist c){
        if(genderMode.equals("Both")){
            return true;
        }
        if(genderMode.equals("Men")){
            if(c.getGender().equals("Male"))
                return true;
        }
        if(genderMode.equals("Women")){
            if(c.getGender().equals("Female"))
                return true;
        }
        return false;
    }

    public ArrayList<Cyclist> getFilteredList(){
        ArrayList<Cyclist> filteredCyclists = new ArrayList<>();
        for (Cyclist c : cyclists) {
            if (matchesDifficulty(c) && matchesGenderMode(c)) {
                filteredCyclists.add(c);
        }
    }
        return filteredCyclists;
    }

    public String setNoob(){
        difficulty = "Noob";
        return difficulty;
    }

    public String setEasy(){
        difficulty = "Easy";
        return difficulty;
    }

    public String setMedium(){
        difficulty = "Medium";
        return difficulty;
    }

    public String setHard(){
        difficulty = "Hard";
        return difficulty;
    }

    public String getDifficulty(){
        return difficulty;
    }

    public String setMen(){
        genderMode = "Men";
        return genderMode;
    }

    public String setWomen(){
        genderMode = "Women";
        return genderMode;
    }

    public String setBoth(){
        genderMode = "Both";
        return genderMode;
    }

    public String getGenderMode(){
        return genderMode;
    }

    public String setLimited(){
        guessMode = "Limited";
        return guessMode;
    }

    public String setInfinite(){
        guessMode = "Infinite";
        return guessMode;
    }

    public String getGuessMode(){
        return guessMode;
    }

    public ArrayList<String> getListOfNames(){
        ArrayList<String> listOfNames = new ArrayList<>();
        for (Cyclist i: getFilteredList()){
            listOfNames.add(i.getName());
        }
        Collections.sort(listOfNames);
        return listOfNames;
    }

    public Cyclist getCurrentAnswer() {
        return currentAnswer;
    }

    public void setCurrentAnswerToRandom() {
        currentAnswer = getRandomCyclist();
    }

    public void setCurrentAnswerToDaily(){
        currentAnswer = getDailyCyclist();
    }
    
    public String getMode(){
        return mode;
    }

    public String setUnlimitedMode(){
        mode = "Unlimited";
        return mode;
    }

    public String setDailyMode(){
        mode = "Daily";
        return mode;
    }

    public Cyclist getDailyCyclist() {
        LocalDate today = LocalDate.now(ZoneId.of("America/Chicago"));
        
        // Use year + month as seed to shuffle the list
        int seed = today.getYear() * 100 + today.getMonthValue();
        Random random = new Random(seed);
        
        // Copy and shuffle the full list
        List<Cyclist> shuffledList = new ArrayList<>(getFilteredList());
        Collections.shuffle(shuffledList, random);
        
        // Use day of month as index (day 1 = index 0, day 2 = index 1, etc.)
        int dayIndex = today.getDayOfMonth() - 1;
        return shuffledList.get(dayIndex);
    }

    public Cyclist getRandomCyclist(){
        Random random = new Random();
        int randomIndex = random.nextInt(getFilteredList().size());
        return getFilteredList().get(randomIndex);
    }

    public ArrayList<String> getRegion(){
        if (southernEurope.contains(currentAnswer.getNationality()))
            return southernEurope;
        if (centralEurope.contains(currentAnswer.getNationality()))
            return centralEurope;
        if (easternEurope.contains(currentAnswer.getNationality()))
            return easternEurope;
        if (northernEurope.contains(currentAnswer.getNationality()))
            return northernEurope;
        if (westernEurope.contains(currentAnswer.getNationality()))
            return westernEurope;
        if (oceania.contains(currentAnswer.getNationality()))
            return oceania;
        if (northAmerica.contains(currentAnswer.getNationality()))
            return northAmerica;
        if (southAmerica.contains(currentAnswer.getNationality()))
            return southAmerica;
        if (africa.contains(currentAnswer.getNationality()))
            return africa;
        if (asia.contains(currentAnswer.getNationality()))
            return asia;
    return null;
    }

    public String[] compareGuess(Cyclist guess, Cyclist answer){
        String[] result = new String[6];
        if (guess.getDebut() == answer.getDebut())
            result[0] = "Green";
        else if (guess.getDebut() >= (answer.getDebut() - 3) && guess.getDebut() <= (answer.getDebut()+ 3))
            result[0] = "#F0C040";
            //Need to figure out how to add if it is more or less
        else result[0] = "dimgray";


        if (guess.getTeam().equals(answer.getTeam()))
            result[1] = "Green";
        else result[1] = "dimgray";


        if (guess.getWins() == answer.getWins())
            result[2] = "Green";
        else if (guess.getWins() >= (answer.getWins() - 5) && guess.getWins() <= (answer.getWins() + 5))
            result[2] = "#F0C040";
        else result[2] = "dimgray";


        if (guess.getGender().equals(answer.getGender()))
            result[3] = "Green";
        else result[3] = "dimgray";


        if (guess.getSpecialty().equals(answer.getSpecialty()))
            result[4] = "Green";
        else result[4] = "dimgray";


        if (guess.getNationality().equals(answer.getNationality()))
            result[5] = "Green";
        else if (getRegion() != null && getRegion().contains(guess.getNationality()))
            result[5] = "#F0C040";
        else result[5] = "dimgray";
            
        System.out.println(Arrays.toString(result));

        return result;
    }

    public String[] getArrows(Cyclist guess, Cyclist answer){
        String[] arrows = new String[2];
        if(guess.getDebut() > answer.getDebut())
            arrows[0] = "↓";
        if(guess.getDebut() < answer.getDebut()) 
            arrows[0] = "↑";
        if(guess.getWins() > answer.getWins())
            arrows[1] = "↓";
        if(guess.getWins() < answer.getWins())
            arrows[1] = "↑";
        return arrows;
    }

    public Cyclist findCyclistByName(String guess){
        for (Cyclist i: getFilteredList()){
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

    public boolean isWon() {
        if (getGuesses().isEmpty()) return false;
        Guess lastGuess = getGuesses().get(0);
        return lastGuess.getGuessedCyclist().getName().equals(getCurrentAnswer().getName());
    }
    
    private boolean manuallyRevealed = false;

    public void setManuallyRevealed(boolean revealed) {
        manuallyRevealed = revealed;
    }

    public boolean isRevealed() {
        return manuallyRevealed || (getGuesses().size() >= 10 && !isWon() && getGuessMode().equals("Limited"));
    }


}



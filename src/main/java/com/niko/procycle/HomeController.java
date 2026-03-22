package com.niko.procycle;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.ui.Model;

@Controller
public class HomeController {
    GameService theData = new GameService();



    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        model.addAttribute("guessHistory", theData.getGuesses());
        model.addAttribute("won", theData.isWon());
        model.addAttribute("revealed", theData.isRevealed());
        if (theData.isRevealed()) {
            Cyclist answer = theData.getCurrentAnswer();
            model.addAttribute("revealedName", answer.getName());
            model.addAttribute("revealedDebut", answer.getDebut());
            model.addAttribute("revealedTeam", answer.getTeam());
            model.addAttribute("revealedWins", answer.getWins());
            model.addAttribute("revealedGender", answer.getGender());
            model.addAttribute("revealedSpecialty", answer.getSpecialty());
            model.addAttribute("revealedNationality", answer.getNationality());
        }
        return "home";
    }

    ArrayList<Cyclist> alreadyGuessedList = new ArrayList<>();

    @PostMapping("/guessAjax")
    @ResponseBody
    public Map<String, Object> handleGuessAjax(@RequestParam String guess) {
        Cyclist guessedCyclist = theData.findCyclistByName(guess);

        if (guessedCyclist == null) {
            return Map.of("error", "Cyclist not found, try again!");
        }
        if (alreadyGuessedList.contains(guessedCyclist)) {
            return Map.of("repeat", "Already Guessed.");
        }

        Cyclist answerCyclist = theData.getCurrentAnswer();
        String[] arrows = theData.getArrows(guessedCyclist, answerCyclist);
        String[] colors = theData.compareGuess(guessedCyclist, answerCyclist);


        Guess aGuess = new Guess(guessedCyclist, colors, arrows);
        theData.guessHistory(aGuess);

        alreadyGuessedList.add(guessedCyclist);
        Map<String, Object> response = new HashMap<>();
        response.put("name", guessedCyclist.getName());
        response.put("colors", colors);
        response.put("arrows", arrows);
        response.put("debut", guessedCyclist.getDebut());
        response.put("team", guessedCyclist.getTeam());
        response.put("wins", guessedCyclist.getWins());
        response.put("gender", guessedCyclist.getGender());
        response.put("specialty", guessedCyclist.getSpecialty());
        response.put("nationality", guessedCyclist.getNationality());
        response.put("won", theData.isWon());
        response.put("revealed", theData.isRevealed());
        if (theData.isRevealed()) {
            response.put("revealedName", answerCyclist.getName());
            response.put("revealedDebut", answerCyclist.getDebut());
            response.put("revealedTeam", answerCyclist.getTeam());
            response.put("revealedWins", answerCyclist.getWins());
            response.put("revealedGender", answerCyclist.getGender());
            response.put("revealedSpecialty", answerCyclist.getSpecialty());
            response.put("revealedNationality", answerCyclist.getNationality());
        }
        return response;
    }

    @PostMapping("/UnlimitedAjax")
    @ResponseBody
    public Map<String, Object> unlimitedModeAjax(){
        theData.setUnlimitedMode();
        theData.setCurrentAnswerToRandom();
        theData.clearHistory();
        Map<String, Object> response = new HashMap<>();
        response.put("difficulty", theData.getDifficulty());
        response.put("genderMode", theData.getGenderMode());
        response.put("guessMode", theData.getGuessMode());
        response.put("listOfNames", theData.getListOfNames());
        response.put("guessMode", theData.getGuessMode());
        return response;
    }

    @PostMapping("/dailyAjax")
    @ResponseBody
    public Map<String, Object> dailyModeAjax(){
        theData.setDailyMode();
        theData.setHard();
        theData.setBoth();
        theData.setLimited();
        theData.setCurrentAnswerToDaily();
        theData.clearHistory();
        Map<String, Object> response = new HashMap<>();
        response.put("listOfNames", theData.getListOfNames());
        response.put("guessMode", theData.getGuessMode());
        return response;
    }

    @PostMapping("/revealAjax")
    @ResponseBody
    public Map<String, Object> revealAjax(){
        Cyclist answer = theData.getCurrentAnswer();
        theData.setManuallyRevealed(true);
        Map<String, Object> response = new HashMap<>();
        response.put("revealedName", answer.getName());
        response.put("revealedDebut", answer.getDebut());
        response.put("revealedTeam", answer.getTeam());
        response.put("revealedWins", answer.getWins());
        response.put("revealedGender", answer.getGender());
        response.put("revealedSpecialty", answer.getSpecialty());
        response.put("revealedNationality", answer.getNationality());
        return response;
    }

    @PostMapping("/Noob")
    @ResponseBody
    public Map<String, Object> noob(){
        theData.setNoob();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Easy")
    @ResponseBody
    public Map<String, Object> easy(){
        theData.setEasy();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Medium")
    @ResponseBody
    public Map<String, Object> medium(){
        theData.setMedium();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Hard")
    @ResponseBody
    public Map<String, Object> hard(){
        theData.setHard();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Both")
    @ResponseBody
    public Map<String, Object> both(){
        theData.setBoth();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Men")
    @ResponseBody
    public Map<String, Object> Men(){
        theData.setMen();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Women")
    @ResponseBody
    public Map<String, Object> Women(){
        theData.setWomen();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Limited")
    @ResponseBody
    public Map<String, Object> limited(){
        theData.setLimited();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Infinite")
    @ResponseBody
    public Map<String, Object> infinite(){
        theData.setInfinite();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }
}
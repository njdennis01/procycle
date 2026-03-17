package com.niko.procycle;

import java.util.ArrayList;
import java.util.Map;
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
        return "home";
    }

    @PostMapping("/guess")
    public String handleGuess(@RequestParam String guess, Model model) {
        Cyclist guessedCyclist = theData.findCyclistByName(guess);
        if (guessedCyclist == null) {
            model.addAttribute("error", "Cyclist not found, try again!");
            model.addAttribute("guessHistory", theData.getGuesses());
            model.addAttribute("listOfNames", theData.getListOfNames());
            model.addAttribute("mode", theData.getMode());
            model.addAttribute("difficulty", theData.getDifficulty());
            model.addAttribute("genderMode", theData.getGenderMode());
            model.addAttribute("guessMode", theData.getGuessMode());
            return "home";
        }
        Cyclist answerCyclist = theData.getCurrentAnswer();
        String[] arrows = theData.getArrows(guessedCyclist, answerCyclist);
        String[] colors = theData.compareGuess(guessedCyclist, answerCyclist);
        model.addAttribute("guessedCyclist", guessedCyclist);
        model.addAttribute("colors", colors);
        model.addAttribute("arrows", arrows);
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        Guess aGuess = new Guess(guessedCyclist, colors, arrows);
        ArrayList<Guess> guessHistory = theData.guessHistory(aGuess);
        model.addAttribute("guessHistory", guessHistory);
        if (guessedCyclist.getName().equals(answerCyclist.getName())){
            model.addAttribute("won", true);
        }
        if (theData.getGuesses().size() >= 10 && !guessedCyclist.getName().equals(answerCyclist.getName()) && theData.getGuessMode().equals("Limited")) {
            model.addAttribute("revealedCyclist", theData.getCurrentAnswer());
            model.addAttribute("revealed", true);
        }
        return "home";
    }

    @PostMapping("/guessAjax")
    @ResponseBody
    public Map<String, Object> handleGuessAjax(@RequestParam String guess) {
        Cyclist guessedCyclist = theData.findCyclistByName(guess);
        if (guessedCyclist == null) {
            return Map.of("error", "Cyclist not found, try again!");
        }
        Cyclist answerCyclist = theData.getCurrentAnswer();
        String[] arrows = theData.getArrows(guessedCyclist, answerCyclist);
        String[] colors = theData.compareGuess(guessedCyclist, answerCyclist);
        Guess aGuess = new Guess(guessedCyclist, colors, arrows);
        ArrayList<Guess> guessHistory = theData.guessHistory(aGuess);
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
        response.put("won", guessedCyclist.getName().equals(answerCyclist.getName()));
        response.put("revealed", theData.getGuesses().size() >= 10 && !guessedCyclist.getName().equals(answerCyclist.getName()) && theData.getGuessMode().equals("Limited"));
        if (theData.getGuesses().size() >= 10 && !guessedCyclist.getName().equals(answerCyclist.getName()) && theData.getGuessMode().equals("Limited")) {
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

    @PostMapping("/Unlimited")
    public String unlimitedMode(Model model){
        theData.setUnlimitedMode();
        theData.setCurrentAnswerToRandom();
        theData.setInfinite();
        theData.clearHistory();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }

    @PostMapping("/daily")
    public String dailyMode(Model model){
        theData.setDailyMode();
        theData.setHard();
        theData.setBoth();
        theData.setLimited();
        theData.setCurrentAnswerToDaily();
        theData.clearHistory();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }
    @PostMapping("/reveal")
    public String revealAnswer(Model model){
        model.addAttribute("revealedCyclist", theData.getCurrentAnswer());
        model.addAttribute("revealed", true);
        model.addAttribute("guessHistory", theData.getGuesses());
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";

    }
    @PostMapping("/Noob")
    public String noob(Model model){
        theData.setNoob();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }
    @PostMapping("/Easy")
    public String easy(Model model){
        theData.setEasy();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }
    @PostMapping("/Medium")
    public String medium(Model model){
        theData.setMedium();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }
    @PostMapping("/Hard")
    public String hard(Model model){
        theData.setHard();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }

    @PostMapping("/Both")
    public String both(Model model){
        theData.setBoth();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }

    @PostMapping("/Men")
    public String men(Model model){
        theData.setMen();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }

    @PostMapping("/Women")
    public String women(Model model){
        theData.setWomen();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }

    @PostMapping("/Limited")
    public String limited(Model model){
        theData.setLimited();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }

    @PostMapping("/Infinite")
    public String infinite(Model model){
        theData.setInfinite();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        model.addAttribute("listOfNames", theData.getListOfNames());
        model.addAttribute("mode", theData.getMode());
        model.addAttribute("difficulty", theData.getDifficulty());
        model.addAttribute("genderMode", theData.getGenderMode());
        model.addAttribute("guessMode", theData.getGuessMode());
        return "home";
    }
}
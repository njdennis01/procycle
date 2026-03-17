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

    @PostMapping("/revealAjax")
    @ResponseBody
    public Map<String, Object> revealAjax(){
        Cyclist answer = theData.getCurrentAnswer();
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

    @PostMapping("/Noob2")
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

    @PostMapping("/Noob")
    @ResponseBody
    public Map<String, Object> noob(){
        theData.setNoob();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Easy2")
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

    @PostMapping("/Easy")
    @ResponseBody
    public Map<String, Object> easy(){
        theData.setEasy();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Medium2")
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

    @PostMapping("/Medium")
    @ResponseBody
    public Map<String, Object> medium(){
        theData.setMedium();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Hard2")
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

    @PostMapping("/Hard")
    @ResponseBody
    public Map<String, Object> hard(){
        theData.setHard();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Both2")
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

    @PostMapping("/Both")
    @ResponseBody
    public Map<String, Object> both(){
        theData.setBoth();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Men2")
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

    @PostMapping("/Men")
    @ResponseBody
    public Map<String, Object> Men(){
        theData.setMen();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Women2")
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

    @PostMapping("/Women")
    @ResponseBody
    public Map<String, Object> Women(){
        theData.setWomen();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Limited2")
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

    @PostMapping("/Limited")
    @ResponseBody
    public Map<String, Object> limited(){
        theData.setLimited();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }

    @PostMapping("/Infinite2")
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

    @PostMapping("/Infinite")
    @ResponseBody
    public Map<String, Object> infinite(){
        theData.setInfinite();
        theData.clearHistory();
        theData.setCurrentAnswerToRandom();
        return Map.of("success", true, "listOfNames", theData.getListOfNames());
    }
}
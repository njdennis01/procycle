package com.niko.procycle;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import jakarta.servlet.http.HttpSession;

import org.springframework.ui.Model;
import java.time.LocalDate;
import java.time.ZoneId;



@Controller
public class HomeController {
    private GameService getDailyGame(HttpSession session) {
        GameService dailyGame = (GameService) session.getAttribute("dailyGame");
        if (dailyGame == null) {
            dailyGame = new GameService();
            dailyGame.setCurrentAnswerToDaily();
            session.setAttribute("dailyGame", dailyGame);
        }
        return dailyGame;
    }

    private GameService getUnlimitedGame(HttpSession session) {
        GameService unlimitedGame = (GameService) session.getAttribute("unlimitedGame");
        if (unlimitedGame == null) {
            unlimitedGame = new GameService();
            unlimitedGame.setCurrentAnswerToRandom();
            session.setAttribute("unlimitedGame", unlimitedGame);
        }
        return unlimitedGame;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Cyclist> getDailyAlreadyGuessed(HttpSession session) {
        ArrayList<Cyclist> list = (ArrayList<Cyclist>) session.getAttribute("dailyAlreadyGuessed");
        if (list == null) {
            list = new ArrayList<>();
            session.setAttribute("dailyAlreadyGuessed", list);
        }
        return list;
    }

    @SuppressWarnings("unchecked")
    private ArrayList<Cyclist> getUnlimitedAlreadyGuessed(HttpSession session) {
        ArrayList<Cyclist> list = (ArrayList<Cyclist>) session.getAttribute("unlimitedAlreadyGuessed");
        if (list == null) {
            list = new ArrayList<>();
            session.setAttribute("unlimitedAlreadyGuessed", list);
        }
        return list;
    }

    private String getCurrentMode(HttpSession session) {
        String mode = (String) session.getAttribute("currentMode");
        if (mode == null) {
            mode = "Daily";
            session.setAttribute("currentMode", mode);
        }
        return mode;
    }

    private LocalDate getLastResetDate(HttpSession session) {
        return (LocalDate) session.getAttribute("lastResetDate");
    }







    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        GameService dailyGame = getDailyGame(session);
        LocalDate lastResetDate = getLastResetDate(session);
        LocalDate today = LocalDate.now(ZoneId.of("America/Chicago"));

        // If date has changed, reset the daily game state
        if (lastResetDate == null || !lastResetDate.equals(today)) {
            session.setAttribute("lastResetDate", today);
            dailyGame.clearHistory();
            dailyGame.setManuallyRevealed(false);
            getDailyAlreadyGuessed(session).clear();
            dailyGame.setCurrentAnswerToDaily();
        }

        String currentMode = getCurrentMode(session);
        GameService activeGame = currentMode.equals("Daily") ? dailyGame : getUnlimitedGame(session);
        getUnlimitedAlreadyGuessed(session).clear();

        model.addAttribute("listOfNames", activeGame.getListOfNames());
        model.addAttribute("mode", currentMode);
        model.addAttribute("difficulty", activeGame.getDifficulty());
        model.addAttribute("genderMode", activeGame.getGenderMode());
        model.addAttribute("guessMode", activeGame.getGuessMode());
        model.addAttribute("guessHistory", activeGame.getGuesses());
        model.addAttribute("won", activeGame.isWon());
        model.addAttribute("revealed", activeGame.isRevealed());
        if (activeGame.isRevealed()) {
            Cyclist answer = activeGame.getCurrentAnswer();
            model.addAttribute("revealedName", answer.getName());
            model.addAttribute("revealedDebut", answer.getDebut());
            model.addAttribute("revealedTeam", answer.getTeam());
            model.addAttribute("revealedWins", answer.getWins());
            model.addAttribute("revealedGender", answer.getGender());
            model.addAttribute("revealedSpecialty", answer.getSpecialty());
            model.addAttribute("revealedNationality", answer.getNationality());
            activeGame.setCurrentAnswerToRandom();
            activeGame.setManuallyRevealed(false);
            
        }
        if (activeGame.isWon()) {
            activeGame.setCurrentAnswerToRandom();
        }

        return "home";
    }


    @PostMapping("/guessAjax")
    @ResponseBody
    public Map<String, Object> handleGuessAjax(@RequestParam String guess, HttpSession session) {
        String currentMode = getCurrentMode(session);
        GameService activeGame = currentMode.equals("Daily") ? getDailyGame(session) : getUnlimitedGame(session);
        ArrayList<Cyclist> activeAlreadyGuessed = currentMode.equals("Daily") ? getDailyAlreadyGuessed(session) : getUnlimitedAlreadyGuessed(session);
        Cyclist guessedCyclist = activeGame.findCyclistByName(guess);

        if (guessedCyclist == null) {
            return Map.of("error", "Cyclist not found, try again!");
        }
        if (activeAlreadyGuessed.contains(guessedCyclist)) {
            return Map.of("repeat", "Already Guessed.");
        }

        Cyclist answerCyclist = activeGame.getCurrentAnswer();
        String[] arrows = activeGame.getArrows(guessedCyclist, answerCyclist);
        String[] colors = activeGame.compareGuess(guessedCyclist, answerCyclist);
        
        Guess aGuess = new Guess(guessedCyclist, colors, arrows);
        activeGame.guessHistory(aGuess);
        
        activeAlreadyGuessed.add(guessedCyclist);
        

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
        response.put("won", activeGame.isWon());
        response.put("revealed", activeGame.isRevealed());
        if (activeGame.isRevealed()) {
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
    public Map<String, Object> unlimitedModeAjax(HttpSession session){
        session.setAttribute("currentMode", "Unlimited");
        GameService unlimitedGame = getUnlimitedGame(session);
        unlimitedGame.setUnlimitedMode();
        unlimitedGame.setCurrentAnswerToRandom();
        unlimitedGame.clearHistory();
        unlimitedGame.setManuallyRevealed(false);
        getUnlimitedAlreadyGuessed(session).clear();
        Map<String, Object> response = new HashMap<>();
        response.put("difficulty", unlimitedGame.getDifficulty());
        response.put("genderMode", unlimitedGame.getGenderMode());
        response.put("guessMode", unlimitedGame.getGuessMode());
        response.put("listOfNames", unlimitedGame.getListOfNames());
        response.put("guessMode", unlimitedGame.getGuessMode());
        return response;
    }

    @PostMapping("/dailyAjax")
    @ResponseBody
    public Map<String, Object> dailyModeAjax(HttpSession session){
        session.setAttribute("currentMode", "Daily");
    GameService dailyGame = getDailyGame(session);
        dailyGame.setDailyMode();
        dailyGame.setHard();
        dailyGame.setBoth();
        dailyGame.setLimited();
        dailyGame.setCurrentAnswerToDaily();
        dailyGame.setManuallyRevealed(false);
        ArrayList<Cyclist> dailyAlreadyGuessed = getDailyAlreadyGuessed(session);
        dailyAlreadyGuessed.clear();
        for (Guess g : dailyGame.getGuesses()) {
            dailyAlreadyGuessed.add(g.getGuessedCyclist());
        }
        Map<String, Object> response = new HashMap<>();
        response.put("listOfNames", dailyGame.getListOfNames());
        response.put("guessMode", dailyGame.getGuessMode());
        return response;
    }

    @PostMapping("/revealAjax")
    @ResponseBody
    public Map<String, Object> revealAjax(HttpSession session){
        String currentMode = getCurrentMode(session);
        GameService activeGame = currentMode.equals("Daily") ? getDailyGame(session) : getUnlimitedGame(session);
        Cyclist answer = activeGame.getCurrentAnswer();
        activeGame.setManuallyRevealed(true);
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
    public Map<String, Object> noob(HttpSession session){
        GameService unlimitedGame = getUnlimitedGame(session);
        unlimitedGame.setNoob();
        unlimitedGame.clearHistory();
        unlimitedGame.setCurrentAnswerToRandom();
        unlimitedGame.setManuallyRevealed(false);
        getUnlimitedAlreadyGuessed(session).clear();
        return Map.of("success", true, "listOfNames", unlimitedGame.getListOfNames());
    }

    @PostMapping("/Easy")
    @ResponseBody
    public Map<String, Object> easy(HttpSession session){
        GameService unlimitedGame = getUnlimitedGame(session);
        unlimitedGame.setEasy();
        unlimitedGame.clearHistory();
        unlimitedGame.setCurrentAnswerToRandom();
        unlimitedGame.setManuallyRevealed(false);
        getUnlimitedAlreadyGuessed(session).clear();
        return Map.of("success", true, "listOfNames", unlimitedGame.getListOfNames());
    }

    @PostMapping("/Medium")
    @ResponseBody
    public Map<String, Object> medium(HttpSession session){
        GameService unlimitedGame = getUnlimitedGame(session);
        unlimitedGame.setMedium();
        unlimitedGame.clearHistory();
        unlimitedGame.setCurrentAnswerToRandom();
        unlimitedGame.setManuallyRevealed(false);
        getUnlimitedAlreadyGuessed(session).clear();
        return Map.of("success", true, "listOfNames", unlimitedGame.getListOfNames());
    }

    @PostMapping("/Hard")
    @ResponseBody
    public Map<String, Object> hard(HttpSession session){
        GameService unlimitedGame = getUnlimitedGame(session);
        unlimitedGame.setHard();
        unlimitedGame.clearHistory();
        unlimitedGame.setCurrentAnswerToRandom();
        unlimitedGame.setManuallyRevealed(false);
        getUnlimitedAlreadyGuessed(session).clear();
        return Map.of("success", true, "listOfNames", unlimitedGame.getListOfNames());
    }

    @PostMapping("/Both")
    @ResponseBody
    public Map<String, Object> both(HttpSession session){
        GameService unlimitedGame = getUnlimitedGame(session);
        unlimitedGame.setBoth();
        unlimitedGame.clearHistory();
        unlimitedGame.setCurrentAnswerToRandom();
        unlimitedGame.setManuallyRevealed(false);
        getUnlimitedAlreadyGuessed(session).clear();
        return Map.of("success", true, "listOfNames", unlimitedGame.getListOfNames());
    }

    @PostMapping("/Men")
    @ResponseBody
    public Map<String, Object> Men(HttpSession session){
        GameService unlimitedGame = getUnlimitedGame(session);
        unlimitedGame.setMen();
        unlimitedGame.clearHistory();
        unlimitedGame.setCurrentAnswerToRandom();
        unlimitedGame.setManuallyRevealed(false);
        getUnlimitedAlreadyGuessed(session).clear();
        return Map.of("success", true, "listOfNames", unlimitedGame.getListOfNames());
    }

    @PostMapping("/Women")
    @ResponseBody
    public Map<String, Object> Women(HttpSession session){
        GameService unlimitedGame = getUnlimitedGame(session);
        unlimitedGame.setWomen();
        unlimitedGame.clearHistory();
        unlimitedGame.setCurrentAnswerToRandom();
        unlimitedGame.setManuallyRevealed(false);
        getUnlimitedAlreadyGuessed(session).clear();
        return Map.of("success", true, "listOfNames", unlimitedGame.getListOfNames());
    }

    @PostMapping("/Limited")
    @ResponseBody
    public Map<String, Object> limited(HttpSession session){
        GameService unlimitedGame = getUnlimitedGame(session);
        unlimitedGame.setLimited();
        unlimitedGame.clearHistory();
        unlimitedGame.setCurrentAnswerToRandom();
        unlimitedGame.setManuallyRevealed(false);
        getUnlimitedAlreadyGuessed(session).clear();
        return Map.of("success", true, "listOfNames", unlimitedGame.getListOfNames());
    }

    @PostMapping("/Infinite")
    @ResponseBody
    public Map<String, Object> infinite(HttpSession session){
        GameService unlimitedGame = getUnlimitedGame(session);
        unlimitedGame.setInfinite();
        unlimitedGame.clearHistory();
        unlimitedGame.setCurrentAnswerToRandom();
        unlimitedGame.setManuallyRevealed(false);
        getUnlimitedAlreadyGuessed(session).clear();
        return Map.of("success", true, "listOfNames", unlimitedGame.getListOfNames());
    }
}
package com.niko.procycle;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    GameService theData = new GameService();



    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("listOfNames", theData.getListOfNames());
        return "home";
    }

    @PostMapping("/guess")
    public String handleGuess(@RequestParam String guess, Model model) {
        Cyclist guessedCyclist = theData.findCyclistByName(guess);
        if (guessedCyclist == null) {
            model.addAttribute("error", "Cyclist not found, try again!");
            model.addAttribute("guessHistory", theData.getGuesses());
            return "home";
        }
        Cyclist answerCyclist = theData.getCurrentAnswer();
        String[] arrows = theData.getArrows(guessedCyclist, answerCyclist);
        String[] colors = theData.compareGuess(guessedCyclist, answerCyclist);
        model.addAttribute("guessedCyclist", guessedCyclist);
        model.addAttribute("colors", colors);
        model.addAttribute("arrows", arrows);
        model.addAttribute("listOfNames", theData.getListOfNames());

        Guess aGuess = new Guess(guessedCyclist, colors, arrows);
        ArrayList<Guess> guessHistory = theData.guessHistory(aGuess);
        model.addAttribute("guessHistory", guessHistory);
        if (guessedCyclist.getName().equals(answerCyclist.getName())){
            model.addAttribute("won", true);
        }
        return "home";
    }
    @PostMapping("/Unlimited")
    public String unlimitedMode(Model model){
        theData.setMode();
        theData.setCurrentAnswerToRandom();
        theData.clearHistory();
        model.addAttribute("listOfNames", theData.getListOfNames());
        return "home";
    }
}
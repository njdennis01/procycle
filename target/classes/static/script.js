
let guessCount = 0;

const flags = {
    "Belgium": "be",
    "Netherlands": "nl",
    "France": "fr",
    "Great Britain": "gb",
    "Italy": "it",
    "USA": "us",
    "Norway": "no",
    "Denmark": "dk",
    "Australia": "au",
    "Canada": "ca",
    "Slovenia": "si",
    "Germany": "de",
    "Austria": "at",
    "Switzerland": "ch",
    "Spain": "es",
    "Hungary": "hu",
    "Colombia": "co",
    "Portugal": "pt",
    "Russia": "ru",
    "Croatia": "hr",
    "Poland": "pl",
    "Ecuador": "ec",
    "Ireland": "ie",
    "Estonia": "ee",
    "New Zealand": "nz",
    "Luxembourg": "lu",
    "Monaco": "mc",
    "Eritrea": "er",
    "Latvia": "lv",
    "Czech Republic": "cz",
    "Sweden": "se",
    "Venezuela": "ve",
    "Kazakhstan": "kz",
    "Israel": "il",
    "Slovakia": "sk",
    "South Africa": "za",
    "Mexico": "mx",
    "China": "cn",
    "Argentina": "ar",
    "Lithuania": "lt",
    "Finland": "fi",
    "Mauritius": "mu",
    "Belarus": "by",
    "Serbia": "rs",
    "Cuba": "cu",
    "Japan": "jp"
};

function getFlag(nationality) {
    return flags[nationality] || "";
}

var playButton = document.getElementById("playButton");
var splashScreen = document.getElementById("splashScreen");
if (sessionStorage.getItem("hasPlayed") !== "true") {
    splashScreen.style.display = "flex";
}

playButton.addEventListener("click", function() {
    splashScreen.style.opacity = "0";
    setTimeout(function() { splashScreen.style.display = "none";}, 300);
    sessionStorage.setItem("hasPlayed", "true");
});

const homeButton = document.getElementById("homeButton");

if (homeButton) {
    homeButton.addEventListener("click", function() {
        fetch('/dailyAjax', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            gameMode = "Daily";
            resetGame();
            updateNamesList(data.listOfNames);
            sessionStorage.removeItem('hasPlayed');
            sessionStorage.removeItem('menuOpen');
            // show splash screen
            splashScreen.style.display = "flex";
            splashScreen.style.opacity = "1";
            // close the menu
            sidebar.classList.remove("active");
            overlay.style.display = "none";
            document.body.style.overflow = "";
        });
    });
}

var menubutton = document.getElementById("menubutton");
var sidebar = document.getElementById("sidebar");
var closebutton = document.getElementById("closebutton");
var overlay = document.getElementById("overlay");

if (sessionStorage.getItem("menuOpen") == "true") {
    sidebar.classList.add("no-transition");
    sidebar.offsetHeight;
    sidebar.classList.add("active");
    overlay.style.display = "flex";
    requestAnimationFrame(function() {
        requestAnimationFrame(function() {
            sidebar.classList.remove("no-transition");
        });
    });
}   
menubutton.addEventListener("click", function() {
    sidebar.classList.add("active");
    sessionStorage.setItem("menuOpen", "true");
    overlay.style.display = "flex";
    document.body.style.overflow = "hidden";
});
closebutton.addEventListener("click", function() {
    sessionStorage.removeItem("menuOpen");
    sidebar.classList.remove("active");
    overlay.style.display = "none";
    document.body.style.overflow = "";
});

var directionsbutton = document.getElementById("directionsbutton");
var directionsPage = document.getElementById("directionsPage");
var close2button = document.getElementById("close2button");
const page1 = document.getElementById("page1");
const page2 = document.getElementById("page2");

if (sessionStorage.getItem("directionsOpen") == "true") {
    directionsPage.classList.add("active");
    overlay.style.display = "flex";
}   
directionsbutton.addEventListener("click", function() {
    directionsPage.classList.add("active");
    sessionStorage.setItem("directionsOpen", "true");
    overlay.style.display = "flex";
    document.body.style.overflow = "hidden";
    page1.style.display = "block";
    page2.style.display = "none";
    howtoplaybutton.classList.add("active-page-btn");
    attributesbutton.classList.remove("active-page-btn");
});
close2button.addEventListener("click", function() {
    directionsPage.classList.remove("active");
    sessionStorage.removeItem("directionsOpen");
    overlay.style.display = "none";
    document.body.style.overflow = "";
});

const howtoplaybutton = document.getElementById("howtoplaybutton");
const attributesbutton = document.getElementById("attributesbutton");

howtoplaybutton.addEventListener("click", function() {
    page1.style.display = "block";
    page2.style.display = "none";
    howtoplaybutton.classList.add("active-page-btn");
    attributesbutton.classList.remove("active-page-btn");
});

attributesbutton.addEventListener("click", function() {
    page1.style.display = "none";
    page2.style.display = "block";
    attributesbutton.classList.add("active-page-btn");
    howtoplaybutton.classList.remove("active-page-btn");
});

var input = document.getElementById("guessInput");
var suggestions = document.getElementById("suggestions");
if (sessionStorage.getItem("hasPlayed") !== "true") {
    splashScreen.style.display = "flex";
}

if (input) {
    input.addEventListener("input", function() {
        var text = input.value.toLowerCase();
        suggestions.innerHTML = "";
            
        if (text.length == 0) {
            suggestions.classList.remove("visible");
            return;
        }
        suggestions.classList.add("visible");
        let matches = 0;
        for (var i = 0; i < cyclists.length; i++) {
        if (cyclists[i].toLowerCase().startsWith(text) || 
            cyclists[i].split(" ").some(function(part) { return part.toLowerCase().startsWith(text); })) {
                var div = document.createElement("div");
                div.textContent = cyclists[i];
                div.className = "suggestion-item";
                div.addEventListener("click", function() {
                    input.value = this.textContent;
                    suggestions.innerHTML = "";
                    suggestions.classList.remove("visible");
                    input.focus();
                });
                suggestions.appendChild(div);
                matches++;
            }
        }
        if (matches > 0) {
            suggestions.classList.add("visible"); // show if matches found
        } else {
            suggestions.classList.remove("visible"); // hide if no matches
        }
        input.addEventListener("keydown", function(e) {
            if (e.key === "Enter" && suggestions.firstChild) {
                input.value = suggestions.firstChild.textContent;
                suggestions.innerHTML = "";
                suggestions.classList.remove("visible");
            }
        });
    });
}

function updateTimer() {
    const now = new Date();
    const midnight = new Date();
    midnight.setHours(24, 0, 0, 0); // next midnight
    
    const diff = midnight - now;
    
    const hours = Math.floor(diff / (1000 * 60 * 60));
    const minutes = Math.floor((diff % (1000 * 60 * 60)) / (1000 * 60));
    const seconds = Math.floor((diff % (1000 * 60)) / 1000);

    document.getElementById("timer").innerHTML = hours + ":" + String(minutes).padStart(2, '0') + ":" + String(seconds).padStart(2, '0');
}

setInterval(updateTimer, 1000);
updateTimer();

const guessForm = document.getElementById("guessForm");

function resetGame() {
    document.getElementById("guessContainer").innerHTML = "";
    document.getElementById("wonMessage").style.display = "none";
    document.getElementById("revealedMessage").style.display = "none";
    document.getElementById("legend").style.display = "none";
    document.getElementById("bottomBorder").style.display = "none";
    guessCount = 0;
    if (guessForm) guessForm.style.display = "block";
    document.getElementById("revealSection").style.display = "none";

    // guess tracker
    if (guessMode === "Limited") {
        document.getElementById("guessTracker").textContent = "Guess 1 of 10";
        document.getElementById("guessTracker").style.display = "block";
    } else {
        document.getElementById("guessTracker").style.display = "none";
    }

    // settings visibility
    if (gameMode === "Unlimited") {
        document.getElementById("settings").style.display = "block";
        document.getElementById("difficulties").style.display = "block";
        document.getElementById("unlimitedButtonDiv").style.display = "none";
        document.getElementById("dailyButtonDiv").style.display = "inline";
        document.getElementById("dailyIntro").style.display = "none";
        document.getElementById("unlimitedWelcome").style.display = "block";
        document.getElementById("dailyWelcome").style.display = "none";
    } else {
        document.getElementById("settings").style.display = "none";
        document.getElementById("difficulties").style.display = "none";
        document.getElementById("unlimitedButtonDiv").style.display = "inline";
        document.getElementById("dailyButtonDiv").style.display = "none";
        document.getElementById("unlimitedWelcome").style.display = "none";
        document.getElementById("dailyWelcome").style.display = "block";
    }
}

function updateNamesList(newList) {
    cyclists = newList;
}

function updateDifficultyButtons(selectedClass, selectedColor) {
    document.querySelectorAll(".noobButton, .easyButton, .mediumButton, .hardButton")
        .forEach(function(btn) { btn.style.backgroundColor = "black"; });
    document.querySelectorAll("." + selectedClass)
        .forEach(function(btn) { btn.style.backgroundColor = selectedColor; });
}

function updateGenderButtons(selectedClass) {
    document.querySelectorAll(".bothButton, .menButton, .womenButton")
        .forEach(function(btn) { btn.style.backgroundColor = "black"; });
    document.querySelectorAll("." + selectedClass)
        .forEach(function(btn) { btn.style.backgroundColor = "#36a73e"; });
}

function updateGuessModeButtons(selectedClass) {
    document.querySelectorAll(".limitedButton, .infiniteButton")
        .forEach(function(btn) { btn.style.backgroundColor = "black"; });
    document.querySelectorAll("." + selectedClass)
        .forEach(function(btn) { btn.style.backgroundColor = "#36a73e"; });
}

function getDifficultyColor(difficulty) {
    if (difficulty === "Noob") return "#36a73e";
    if (difficulty === "Easy") return "#F0C040";
    if (difficulty === "Medium") return "#007bbf";
    if (difficulty === "Hard") return "#da2430";
}

document.querySelectorAll(".unlimitedButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/UnlimitedAjax', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            gameMode = "Unlimited";
            guessMode = data.guessMode;
            updateNamesList(data.listOfNames);
            updateDifficultyButtons(data.difficulty.toLowerCase() + "Button", getDifficultyColor(data.difficulty));
            updateGenderButtons(data.genderMode.toLowerCase() + "Button");
            updateGuessModeButtons(data.guessMode === "Limited" ? "limitedButton" : "infiniteButton");
            sidebar.classList.remove("active");
            overlay.style.display = "none";
            sessionStorage.removeItem('menuOpen');
            document.body.style.overflow = "";
            resetGame();
        });
    });
});

document.querySelectorAll(".dailyButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/dailyAjax', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            gameMode = "Daily";
            guessMode = data.guessMode;
            updateNamesList(data.listOfNames);
            sidebar.classList.remove("active");
            overlay.style.display = "none";
            sessionStorage.removeItem('menuOpen');
            document.body.style.overflow = "";
            resetGame();
        });
    });
});

document.querySelectorAll(".noobButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/Noob', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            document.querySelectorAll(".noobButton").forEach(function(btn) {
                updateDifficultyButtons("noobButton", "#36a73e");
            });
            updateNamesList(data.listOfNames);
            resetGame();
        });
    });
});

document.querySelectorAll(".easyButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/Easy', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            document.querySelectorAll(".easyButton").forEach(function(btn) {
                updateDifficultyButtons("easyButton", "#F0C040");
            });
            updateNamesList(data.listOfNames);
            resetGame();
        });
    });
});

document.querySelectorAll(".mediumButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/Medium', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            document.querySelectorAll(".mediumButton").forEach(function(btn) {
                updateDifficultyButtons("mediumButton", "#007bbf");
            });
            updateNamesList(data.listOfNames);
            resetGame();
        });
    });
});

document.querySelectorAll(".hardButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/Hard', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            document.querySelectorAll(".hardButton").forEach(function(btn) {
                updateDifficultyButtons("hardButton", "#da2430");
            });
            updateNamesList(data.listOfNames);
            resetGame();
        });
    });
});

document.querySelectorAll(".bothButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/Both', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            updateGenderButtons("bothButton");
            updateNamesList(data.listOfNames);
            resetGame();
        });
    });
});

document.querySelectorAll(".menButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/Men', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            updateGenderButtons("menButton");
            updateNamesList(data.listOfNames);
            resetGame();
        });
    });
});

document.querySelectorAll(".womenButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/Women', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            updateGenderButtons("womenButton");
            updateNamesList(data.listOfNames);
            resetGame();
        });
    });
});

document.querySelectorAll(".limitedButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/Limited', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            guessMode = "Limited"; 
            updateGuessModeButtons("limitedButton");
            updateNamesList(data.listOfNames);
            resetGame();
        });
    });
});

document.querySelectorAll(".infiniteButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/Infinite', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            guessMode = "Infinite"; 
            updateGuessModeButtons("infiniteButton")
            updateNamesList(data.listOfNames);
            resetGame();
        });
    });
});

document.querySelectorAll(".revealButton").forEach(function(button) {
    button.addEventListener("click", function() {
        fetch('/revealAjax', { method: 'POST' })
        .then(function(response) { return response.json(); })
        .then(function(data) {
            document.getElementById("revealedMessage").style.display = "block";
            document.getElementById("revealedName").textContent = data.revealedName;
            document.getElementById("revealedProDebut").textContent = data.revealedDebut;
            document.getElementById("revealedTeam").textContent = data.revealedTeam;
            document.getElementById("revealedWins").textContent = data.revealedWins;
            document.getElementById("revealedGender").textContent = data.revealedGender;
            document.getElementById("revealedSpecialty").textContent = data.revealedSpecialty;
            document.getElementById("revealedNationality").textContent = data.revealedNationality;
            const revealedFlagEl = document.getElementById("revealedFlag");
            revealedFlagEl.innerHTML = "";
            revealedFlagEl.appendChild(getFlag(data.revealedNationality));
            animateRevealedTiles()

            document.getElementById("guessTracker").style.display = "none";
            document.getElementById("revealSection").style.display = "none";
            document.getElementById("legend").style.display = "none";
            guessForm.style.display = "none";
            sidebar.classList.remove("active");
            overlay.style.display = "none";
            sessionStorage.removeItem('menuOpen');
            document.body.style.overflow = "";
        });
    });
});

function animateRevealedTiles() {
    document.querySelectorAll(".revealed-tile").forEach(function(tile) {
        tile.classList.remove("flip-in");
        tile.classList.remove("flip-out");
        tile.style.backgroundColor = "Black";
        requestAnimationFrame(function() {
            requestAnimationFrame(function() {
                tile.classList.add("flip-out");
                setTimeout(function() {
                    tile.style.backgroundColor = "Green";
                    tile.classList.remove("flip-out");
                    tile.classList.add("flip-in");
                }, 300);
            });
        });
    });
}

function getFlag(nationality) {
    const code = flags[nationality];
    if (!code) return "";
    const img = document.createElement("img");
    img.src = "https://flagcdn.com/w80/" + code + ".png";
    img.style.width = nationality === "Switzerland" ? "50px" : "80px";
    return img;
}

function addGuessRow(data) {
    const container = document.getElementById("guessContainer");
    
    const row = document.createElement("div");
    row.style.cssText = "display: flex; flex-wrap: wrap; justify-content: center; width: 600px; margin: auto; margin-bottom: 20px;";
    
    // Cyclist name
    const name = document.createElement("p");
    name.textContent = data.name;
    name.style.cssText = "font-size: 35px; font-weight: 700; color: black; margin-bottom: 20px; margin-top: 20px; width: 100%; text-align: center;";
    row.appendChild(name);
    
    const attributes = [
        { label: "Pro Debut", value: data.debut + " " + (data.arrows[0] ? data.arrows[0] : "") },
        { label: "Team", value: data.team },
        { label: "Pro Wins", value: data.wins + " " + (data.arrows[1] ? data.arrows[1] : "") },
        { label: "Gender", value: data.gender },
        { label: "Specialty", value: data.specialty },
        { label: "Nationality", value: data.nationality }
    ];

    attributes.forEach(function(attr, index) {
        const box = document.createElement("div");
        box.classList.add("tile");
        box.style.cssText = "width: 150px; height: 120px; padding: 10px; margin: 5px; text-align: center; color: white; border-radius: 20px; display: flex; align-items: center; justify-content: center; flex-direction: column;";
        box.style.backgroundColor = "dimgray";

        const label = document.createElement("p");
        if (index != 5){
            label.textContent = attr.label;
            label.style.fontWeight = "600";
            label.style.margin = "10px";
            label.style.fontSize = "20px";
        } else{
            label.appendChild(getFlag(data.nationality));
            label.style.marginBottom = "5px";
            label.style.marginTop = "10px";
        }
        const value = document.createElement("p");
        value.textContent = attr.value;
        if (index != 5 && index != 1){
            value.style.fontSize = "25px";
            value.style.margin = "10px";
        }
        if (index == 5){
            value.style.fontWeight = "600";
            value.style.marginTop = "0px";
            value.style.marginBottom = "5px";
            value.style.fontSize = "20px";
        }
        if (index == 1){
            value.style.marginTop = "0px";
            value.style.fontSize = "20px";
        }
        
        box.appendChild(label);
        box.appendChild(value);
        row.appendChild(box);
        requestAnimationFrame(function() {
            requestAnimationFrame(function() {
                if (data.colors[index] !== "dimgray") {
                    box.classList.add("flip-out");
                    setTimeout(function() {
                        box.style.backgroundColor = data.colors[index];
                        box.classList.remove("flip-out");
                        box.classList.add("flip-in");
                    }, 300);
                }
            });
        });
    });
    container.prepend(row);
}

if (guessForm) {
    guessForm.addEventListener("submit", function(event) {
        event.preventDefault();
        const guess = document.getElementById("guessInput").value;
        fetch('/guessAjax', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: 'guess=' + encodeURIComponent(guess)
        })
        .then(function(response) {
            return response.json();
        })
        .then(function(data) {
            console.log(data);
            if (data.won) {
                document.getElementById("wonMessage").style.display = "block";
                document.getElementById("guessCountText").textContent = "Guesses: " + (guessCount + 1);
            }
            if (data.revealed) {
                document.getElementById("revealedMessage").style.display = "block";
                document.getElementById("revealedName").textContent = data.revealedName;
                document.getElementById("revealedProDebut").textContent = data.revealedDebut;
                document.getElementById("revealedTeam").textContent = data.revealedTeam;
                document.getElementById("revealedWins").textContent = data.revealedWins;
                document.getElementById("revealedGender").textContent = data.revealedGender;
                document.getElementById("revealedSpecialty").textContent = data.revealedSpecialty;
                document.getElementById("revealedNationality").textContent = data.revealedNationality;
                const revealedFlagEl = document.getElementById("revealedFlag");
                revealedFlagEl.innerHTML = "";
                revealedFlagEl.appendChild(getFlag(data.revealedNationality));
                animateRevealedTiles()
            }

            const errorMessage = document.getElementById("errorMessage");
            const repeatMessage = document.getElementById("repeatMessage");
            if (data.error) {
                errorMessage.textContent = data.error;
                errorMessage.style.display = "block";
                repeatMessage.style.display = "none";
                document.getElementById("guessInput").value = "";
                return;
            }
            if (data.repeat) {
                repeatMessage.textContent = data.repeat;
                repeatMessage.style.display = "block";
                errorMessage.style.display = "none";
                document.getElementById("guessInput").value = "";
                return;
            } 
            else {
                errorMessage.style.display = "none";
                repeatMessage.style.display = "none";
                document.getElementById("guessInput").value = "";
                addGuessRow(data);
                guessCount++;
                if (guessMode === "Limited") {
                    document.getElementById("guessTracker").style.display = "block";
                    document.getElementById("guessTracker").textContent = "Guess " + (guessCount + 1) + " of 10";
                } else {
                    document.getElementById("guessTracker").style.display = "none";
                }
                document.getElementById("revealSection").style.display = "block";
                document.getElementById("legend").style.display = "flex";
                document.getElementById("dailyIntro").style.display = "none";
                if (guessCount >= 2) {
                    document.getElementById("bottomBorder").style.display = "block";
                }
                if (data.won || data.revealed){
                    document.getElementById("guessTracker").style.display = "none";
                    document.getElementById("revealSection").style.display = "none";
                    document.getElementById("legend").style.display = "none";
                    guessForm.style.display = "none";
                }
            }
        })
    });
}


//Reload Backup Below

if (gameMode === "Daily") {
    document.getElementById("dailyWelcome").style.display = "block";
    document.getElementById("unlimitedWelcome").style.display = "none";
}
if (gameMode === "Unlimited") {
    document.getElementById("dailyWelcome").style.display = "none";
    document.getElementById("unlimitedWelcome").style.display = "block";
}


updateDifficultyButtons(difficulty.toLowerCase() + "Button", getDifficultyColor(difficulty));
updateGenderButtons(genderMode.toLowerCase() + "Button");
updateGuessModeButtons(guessMode === "Limited" ? "limitedButton" : "infiniteButton");

initialGuesses.reverse().forEach(function(guess) {
    addGuessRow({
        name: guess.guessedCyclist.name,
        colors: guess.colors,
        arrows: guess.arrows,
        debut: guess.guessedCyclist.debut,
        team: guess.guessedCyclist.team,
        wins: guess.guessedCyclist.wins,
        gender: guess.guessedCyclist.gender,
        specialty: guess.guessedCyclist.specialty,
        nationality: guess.guessedCyclist.nationality
    });
    guessCount++;
});
if (guessCount > 0) {
    document.getElementById("legend").style.display = "flex";
    document.getElementById("revealSection").style.display = "block";
}
if (guessCount > 0 || gameMode == "Unlimited"){
    document.getElementById("dailyIntro").style.display = "none";
}
if (guessMode == "Limited"){
    document.getElementById("guessTracker").style.display = "block";
    document.getElementById("guessTracker").textContent = "Guess " + (guessCount + 1) + " of 10";
}
if (guessCount >= 2) {
    document.getElementById("bottomBorder").style.display = "block";
}
if (won) {
    document.getElementById("wonMessage").style.display = "block";
    document.getElementById("guessCountText").textContent = "Guesses: " + guessCount;
    document.getElementById("guessTracker").style.display = "none";
    document.getElementById("revealSection").style.display = "none";
    document.getElementById("legend").style.display = "none";
    guessForm.style.display = "none";
}
if (revealed) {
    document.getElementById("revealedMessage").style.display = "block";
    document.getElementById("revealedName").textContent = revealedName;
    document.getElementById("revealedProDebut").textContent = revealedDebut;
    document.getElementById("revealedTeam").textContent = revealedTeam;
    document.getElementById("revealedWins").textContent = revealedWins;
    document.getElementById("revealedGender").textContent = revealedGender;
    document.getElementById("revealedSpecialty").textContent = revealedSpecialty;
    document.getElementById("revealedNationality").textContent = revealedNationality;
    const revealedFlagEl = document.getElementById("revealedFlag");
    revealedFlagEl.innerHTML = "";
    revealedFlagEl.appendChild(getFlag(revealedNationality));
    animateRevealedTiles()

    document.getElementById("revealedMessage").style.display = "block";
    document.getElementById("guessTracker").style.display = "none";
    document.getElementById("revealSection").style.display = "none";
    document.getElementById("legend").style.display = "none";
    guessForm.style.display = "none";
}
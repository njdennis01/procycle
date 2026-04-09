const today = new Date();
const month = String(today.getMonth() + 1).padStart(2, '0');
const day = String(today.getDate()).padStart(2, '0');
const dateString = month + "/" + day;


let shareResult = "PROCYCLE " + dateString + " ";
let guessCount = 0;
let savedGuesses = [];

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
            loadGameState()
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


var statsbutton = document.getElementById("statsbutton");
var statistics = document.getElementById("statistics");
var close3button = document.getElementById("close3button");

if (sessionStorage.getItem("statisticsOpen") == "true") {
    statistics.classList.add("active");
    overlay.style.display = "flex";
    displayStats()
}   
statsbutton.addEventListener("click", function() {
    statistics.classList.add("active");
    sessionStorage.setItem("statisticsOpen", "true");
    overlay.style.display = "flex";
    document.body.style.overflow = "hidden";
    displayStats(); 
});
close3button.addEventListener("click", function() {
    statistics.classList.remove("active");
    sessionStorage.removeItem("statisticsOpen");
    overlay.style.display = "none";
    document.body.style.overflow = "";
});






if (sessionStorage.getItem("hasPlayed") !== "true") {
    splashScreen.style.display = "flex";
}

var input = document.getElementById("guessInput");
var suggestions = document.getElementById("suggestions");

if (input) {
    input.addEventListener("input", function() {
        var text = input.value.toLowerCase();
        var parts = text.split(" ").filter(function(p) { return p.length > 0; });
        suggestions.innerHTML = "";
            
        if (text.length == 0) {
            suggestions.classList.remove("visible");
            return;
        }

        suggestions.classList.add("visible");

        var firstNameMatches = [];
        var lastNameMatches = [];

        for (var i = 0; i < cyclists.length; i++) {
            var nameParts = cyclists[i].toLowerCase().split(" ");
            if (parts.length == 1){
                if (nameParts[0].startsWith(parts[0])) {
                    firstNameMatches.push(cyclists[i]);
                } else if (nameParts.some(function(part) { return part.startsWith(parts[0]); })) {
                    lastNameMatches.push(cyclists[i]);
                }
            }
            else {
                // multiple words — check if all parts match somewhere in the name
                var allMatch = parts.every(function(part) {
                    return nameParts.some(function(p) { return p.startsWith(part); });
                });
                if (allMatch) firstNameMatches.push(cyclists[i]);
            }
        }

        var allMatches = firstNameMatches.concat(lastNameMatches);

        for (var i = 0; i < allMatches.length; i++) {
            var div = document.createElement("div");
            div.textContent = allMatches[i];
            div.className = "suggestion-item";
            div.addEventListener("click", function() {
                input.value = this.textContent;
                suggestions.innerHTML = "";
                suggestions.classList.remove("visible");
                input.focus();
            });
            suggestions.appendChild(div);
        }

        if (allMatches.length > 0) {
            suggestions.classList.add("visible");
        } else {
            suggestions.classList.remove("visible");
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
    console.log("guessCount before resetGame:", guessCount);
    document.getElementById("guessContainer").innerHTML = "";
    document.getElementById("wonMessage").style.display = "none";
    document.getElementById("revealedMessage").style.display = "none";
    document.getElementById("legend").style.display = "none";
    document.getElementById("bottomBorder").style.display = "none";
    document.getElementById("revealSection").style.display = "none";
    document.getElementById("errorMessage").style.display = "none";
    document.getElementById("repeatMessage").style.display = "none";
    document.getElementById("guessInput").value = "";
    shareResult = "PROCYCLE " + dateString + " ";

    guessCount = 0;

    if (guessForm) guessForm.style.display = "block";

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
        document.getElementById("dailyButtonDiv").style.display = "block";
        document.getElementById("dailyWelcome").style.display = "none";
        document.getElementById("unlimitedWelcome").style.display = "block";
    } else {
        document.getElementById("settings").style.display = "none";
        document.getElementById("difficulties").style.display = "none";
        document.getElementById("unlimitedButtonDiv").style.display = "block";
        document.getElementById("dailyButtonDiv").style.display = "none";
        document.getElementById("dailyWelcome").style.display = "block";
        document.getElementById("unlimitedWelcome").style.display = "none";
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
            console.log("after resetGame in dailyButton:", guessCount);
            loadGameState()
            console.log("after loadGameState in dailyButton:", guessCount);
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

            saveGameState("revealed", {
                name: data.revealedName,
                debut: data.revealedDebut,
                team: data.revealedTeam,
                wins: data.revealedWins,
                gender: data.revealedGender,
                specialty: data.revealedSpecialty,
                nationality: data.revealedNationality
            });
            updateStats("lost")
        });
    });
});

function animateRevealedTiles() {
    document.querySelectorAll(".revealed-tile").forEach(function(tile) {
        tile.classList.remove("flip-in");
        tile.classList.remove("flip-out");
        tile.style.backgroundColor = "dimgray";
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

function copyToClipboard(text) {
    navigator.clipboard.writeText(text).then(function() {
        msg = document.getElementById("copyMessage");
        msg.style.display = "block";
        msg.classList.remove("fade-out");

        setTimeout(function() {
            msg.classList.add("fade-out");
            setTimeout(function() {
                msg.style.display = "none";
                msg.classList.remove("fade-out");
            }, 500);
        }, 2000);
    });
}

document.querySelectorAll(".shareButton").forEach(function(button) {
    button.addEventListener("click", function() {
        copyToClipboard(shareResult);
    })
});


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
                addGuessRow(data);
                savedGuesses.push(data); 
                guessCount++;
                saveGameState("won");
                shareResult += "🟩";
                updateStats("won");

                document.getElementById("wonMessage").style.display = "block";
                document.getElementById("guessTracker").style.display = "none";
                document.getElementById("revealSection").style.display = "none";
                document.getElementById("legend").style.display = "none";
                if (gameMode == "Unlimited"){
                    document.getElementById("shareButtonSection").style.display = "none";
                }
                if (gameMode == "Daily"){
                    document.getElementById("shareButtonSection").style.display = "block";
                }

                guessForm.style.display = "none";
                guesses = document.getElementById("guessCountText");
                guesses.textContent = (guessCount);
                if (guessCount < 3){
                    guesses.style.color = "green";
                }
                else if (guessCount < 7){
                    guesses.style.color = "#F0C040";
                }
                else {
                    guesses.style.color = "red";
                }
                return;
            }
            
            if (data.revealed) {
                addGuessRow(data);
                savedGuesses.push(data); 
                guessCount++;
                saveGameState("revealed", {
                    name: data.revealedName,
                    debut: data.revealedDebut,
                    team: data.revealedTeam,
                    wins: data.revealedWins,
                    gender: data.revealedGender,
                    specialty: data.revealedSpecialty,
                    nationality: data.revealedNationality
                });
                updateStats("lost")

                document.getElementById("revealedMessage").style.display = "block";
                document.getElementById("revealedName").textContent = data.revealedName;
                document.getElementById("revealedProDebut").textContent = data.revealedDebut;
                document.getElementById("revealedTeam").textContent = data.revealedTeam;
                document.getElementById("revealedWins").textContent = data.revealedWins;
                document.getElementById("revealedGender").textContent = data.revealedGender;
                document.getElementById("revealedSpecialty").textContent = data.revealedSpecialty;
                document.getElementById("revealedNationality").textContent = data.revealedNationality;
                document.getElementById("guessTracker").style.display = "none";
                document.getElementById("revealSection").style.display = "none";
                document.getElementById("legend").style.display = "none";
                guessForm.style.display = "none";
                const revealedFlagEl = document.getElementById("revealedFlag");
                revealedFlagEl.innerHTML = "";
                revealedFlagEl.appendChild(getFlag(data.revealedNationality));
                animateRevealedTiles()
                return;
            }

            const errorMessage = document.getElementById("errorMessage");
            const repeatMessage = document.getElementById("repeatMessage");
            if (data.repeat) {
                repeatMessage.textContent = data.repeat;
                repeatMessage.style.display = "block";
                errorMessage.style.display = "none";
                document.getElementById("guessInput").value = "";
                return;
            }
            if (data.error) {
                errorMessage.textContent = data.error;
                errorMessage.style.display = "block";
                repeatMessage.style.display = "none";
                document.getElementById("guessInput").value = "";
                return;
            }
            else {
                errorMessage.style.display = "none";
                repeatMessage.style.display = "none";
                document.getElementById("guessInput").value = "";

                addGuessRow(data);

                savedGuesses.push(data);

                shareResult += "⬜";
                guessCount++;

                saveGameState("inProgress");

                if (guessMode === "Limited") {
                    document.getElementById("guessTracker").style.display = "block";
                    document.getElementById("guessTracker").textContent = "Guess " + (guessCount + 1) + " of 10";
                } else {
                    document.getElementById("guessTracker").style.display = "none";
                }
                document.getElementById("revealSection").style.display = "block";
                document.getElementById("legend").style.display = "flex";
                if (guessCount >= 2) {
                    document.getElementById("bottomBorder").style.display = "block";
                }
            }
        })
    });
}























function saveGameState(currentGameState, revealedData = null) {
    if (gameMode !== "Daily") return;

    localStorage.setItem("procycleDaily", JSON.stringify({
        date: dateString,
        guesses: savedGuesses,
        guessCount: guessCount,
        shareResult: shareResult,
        gameState: currentGameState || "inProgress",
        revealedData: revealedData
    }));
}

function loadGameState() {
    const saved = localStorage.getItem("procycleDaily");
    if (!saved){ 
        console.log("returning early - no saved data");
        return;
    }
    const state = JSON.parse(saved);
    console.log("revealedData:", state.revealedData);

    //Clear if old Date
    if (state.date !== dateString) {
        console.log("returning early - date mismatch");
        localStorage.removeItem("procycleDaily"); 
        return;
    }

    if (state.gameState === "revealed" && state.revealedData) {
        document.getElementById("revealedName").textContent = state.revealedData.name;
        document.getElementById("revealedProDebut").textContent = state.revealedData.debut;
        document.getElementById("revealedTeam").textContent = state.revealedData.team;
        document.getElementById("revealedWins").textContent = state.revealedData.wins;
        document.getElementById("revealedGender").textContent = state.revealedData.gender;
        document.getElementById("revealedSpecialty").textContent = state.revealedData.specialty;
        document.getElementById("revealedNationality").textContent = state.revealedData.nationality;
        const revealedFlagEl = document.getElementById("revealedFlag");
        revealedFlagEl.innerHTML = "";
        revealedFlagEl.appendChild(getFlag(state.revealedData.nationality));
        animateRevealedTiles()
    }

    // Restore variables
    guessCount = state.guessCount;
    shareResult = state.shareResult;
    savedGuesses = state.guesses;


    // Rebuild each row
    state.guesses.forEach(function(guess) {
        addGuessRow(guess);
    });

    // Restores Guess Tracker
    if (guessMode === "Limited") {
        document.getElementById("guessTracker").textContent = "Guess " + (guessCount + 1) + " of 10";
        document.getElementById("guessTracker").style.display = "block";
    }

    // Restores legend and bottom border
    if (state.guesses.length > 0) {
        document.getElementById("legend").style.display = "flex";
        document.getElementById("revealSection").style.display = "block";
    }
    if (state.guesses.length > 1) {
        document.getElementById("bottomBorder").style.display = "block";
    }

    if (state.gameState === "won") {
        document.getElementById("wonMessage").style.display = "block";
        guessForm.style.display = "none";
        document.getElementById("guessTracker").style.display = "none";
        document.getElementById("revealSection").style.display = "none";
    }
    if (state.gameState === "revealed") {
        document.getElementById("revealedMessage").style.display = "block";
        guessForm.style.display = "none";
        document.getElementById("guessTracker").style.display = "none";
        document.getElementById("revealSection").style.display = "none";
    }
    if (guessCount >= 10) {
        guessForm.style.display = "none";
        document.getElementById("guessTracker").style.display = "none";
        document.getElementById("revealSection").style.display = "none";
    }

    console.log("guessCount after loadGameState:", guessCount);
}


function initStats() {
    if (!localStorage.getItem("procycleDailyStats")) {
        localStorage.setItem("procycleDailyStats", JSON.stringify({
            gamesPlayed: 0,
            wins: 0,
            currentStreak: 0,
            maxStreak: 0,
            guessDistribution: {1:0, 2:0, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, 10:0}
        }));
    }
    if (!localStorage.getItem("procycleUnlimitedStats")) {
        localStorage.setItem("procycleUnlimitedStats", JSON.stringify({
            gamesPlayed: 0,
            wins: 0,
            guessDistribution: {1:0, 2:0, 3:0, 4:0, 5:0, 6:0, 7:0, 8:0, 9:0, "10+":0}
        }));
    }
}


function updateStats(result) {
    console.log("updateStats called:", result);
    const key = gameMode === "Daily" ? "procycleDailyStats" : "procycleUnlimitedStats";
    const stats = JSON.parse(localStorage.getItem(key));

    stats.gamesPlayed++;

    if (result === "won") {
        stats.wins++;
        const distKey = guessCount >= 10 ? "10+" : guessCount;
        stats.guessDistribution[distKey] = (stats.guessDistribution[distKey] || 0) + 1;
        if (gameMode === "Daily") {
            stats.currentStreak++;
            if (stats.currentStreak > stats.maxStreak) {
                stats.maxStreak = stats.currentStreak;
            }
        }
    } else {
        if (gameMode === "Daily") {
            stats.currentStreak = 0;
        }
}

    localStorage.setItem(key, JSON.stringify(stats));
}


function displayStats() {
    if (gameMode === "Daily") {
        const dailyStats = JSON.parse(localStorage.getItem("procycleDailyStats"));
        const unlimitedStats = JSON.parse(localStorage.getItem("procycleUnlimitedStats"));

        // Daily stats
        document.getElementById("gamesPlayed").textContent = dailyStats.gamesPlayed;
        document.getElementById("totalWins").textContent = dailyStats.wins;
        document.getElementById("winRate").textContent = dailyStats.gamesPlayed === 0 ? "0%" : Math.round((dailyStats.wins / dailyStats.gamesPlayed) * 100) + "%";
        document.getElementById("currentStreak").textContent = dailyStats.currentStreak;
        document.getElementById("maxStreak").textContent = dailyStats.maxStreak;

        // Guess distribution
        const maxCount = Math.max(...Object.values(dailyStats.guessDistribution));

        const distribution = document.getElementById("guessDistribution");
        distribution.innerHTML = "";

        for (let i = 1; i <= 10; i++) {
            const count = dailyStats.guessDistribution[i] || 0;
            const width = maxCount === 0 ? 0 : Math.round((count / maxCount) * 100);
            
            distribution.innerHTML += `
                <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 5px;">
                    <p style="margin: 0; width: 15px;">${i}</p>
                    <div style="border-radius: 5px; background-color: gray; width: ${width}%; height: 25px; min-width: 10px;">
                    </div>
                    <p style="margin: 0;">${count === 0 ? '' : count}</p>
                </div>
            `;
        }

        document.getElementById("dailyStats").style.display = "block";
        document.getElementById("unlimitedStats").style.display = "none";
    }

    else {
         const unlimitedStats = JSON.parse(localStorage.getItem("procycleUnlimitedStats"));

        // Unlimited stats
        document.getElementById("unlimitedGamesPlayed").textContent = unlimitedStats.gamesPlayed;
        document.getElementById("unlimitedTotalWins").textContent = unlimitedStats.wins;
        document.getElementById("unlimitedWinRate").textContent = unlimitedStats.gamesPlayed === 0 ? "0%" : Math.round((unlimitedStats.wins / unlimitedStats.gamesPlayed) * 100) + "%";

        //Guess Distribution
        const maxCount = Math.max(...Object.values(unlimitedStats.guessDistribution));
        const distribution = document.getElementById("unlimitedGuessDistribution");
        distribution.innerHTML = "";
        const keys = [1, 2, 3, 4, 5, 6, 7, 8, 9, "10+"];
        keys.forEach(function(key) {
            const count = unlimitedStats.guessDistribution[key];
            const width = maxCount === 0 ? 0 : Math.round((count / maxCount) * 100);
            distribution.innerHTML += `
                <div style="display: flex; align-items: center; gap: 10px; margin-bottom: 5px;">
                    <p style="margin: 0; width: 25px;">${key}</p>
                    <div style="border-radius: 5px; background-color: gray; width: ${width - 10}%; height: 25px; min-width: 10px;"></div>
                    <p style="margin: 0;">${count === 0 ? '' : count}</p>
                </div>`;
        });

        document.getElementById("dailyStats").style.display = "none";
        document.getElementById("unlimitedStats").style.display = "block";
    }
}



updateDifficultyButtons(difficulty.toLowerCase() + "Button", getDifficultyColor(difficulty));
updateGenderButtons(genderMode.toLowerCase() + "Button");
updateGuessModeButtons(guessMode === "Limited" ? "limitedButton" : "infiniteButton");

initStats()
loadGameState();
    
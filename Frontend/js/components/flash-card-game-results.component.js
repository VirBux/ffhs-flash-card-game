class FlashCardGameResults {
  // HTML Elements
  #htmlElement = TemplateStringParser.parse(/*html*/ `
  <section id="results-main-container" class="container mt-3">
    <div class="row justify-content-center">
        <!-- Title above answer buttons -->
        <div class="col-11 col-md-6">
            <div class="d-flex justify-content-start mb-3">
                <label id="sco" class="label-title">Score-Board</label>
            </div>
        </div>
    </div>
    <section class="row justify-content-center scoreboard">
        <div class="col-11 col-md-6">
            <div class="mb-3">
                <table class="table">
                    <thead>
                        <tr>
                            <th scope="col">Rang</th>
                            <th scope="col">Player Name</th>
                            <th scope="col">Punkte</th>
                        </tr>
                    </thead>
                    <tbody>
                        <!-- Content injects here -->
                    </tbody>
                </table>
                <!-- Button aligned with the table -->
                <button class="btn btn-primary mt-3" id="back-to-lobby">zurück zur Lobby</button>
            </div>
        </div>
    </section>
</section>
`);

  // HTML Elements
  #appHtmlElement = document.querySelector("#app");
  #tBodyElement = this.#htmlElement.querySelector("tbody");
  #backToLobbyButton = this.#htmlElement.querySelector("#back-to-lobby");

  get htmlElement() {
    return this.#htmlElement;
  }

  constructor() {
    Dev.log("FlashCardGameResults loaded");
    this.#backToLobbyButton.addEventListener("click", () => {
      this.#popComponentHtmlElements();
      game.fromGameBackToLobby();
    });
  }

  /**
   * @param {Player[]} players
   */
  showResults(players) {
    this.#addComponentHtmlElements();
    let tBodyInner = "";

    players.sort((a, b) => b.points - a.points);

    let playerRank = 1;
    players.forEach((player, index) => {
      tBodyInner += `
        <tr>
          <th scope="row">${playerRank}</th>
          <td>${player.userName}</td>
          <td>${player.points}</td>
        </tr>
      `;
      playerRank++;
    });
    this.#tBodyElement.innerHTML = tBodyInner;
  }

  #addComponentHtmlElements() {
    this.#appHtmlElement.appendChild(this.#htmlElement);
  }

  #popComponentHtmlElements() {
    this.#appHtmlElement.removeChild(this.#htmlElement);
  }
}

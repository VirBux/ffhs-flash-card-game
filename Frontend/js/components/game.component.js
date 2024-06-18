class GameComponent extends Observer {
  // HTML Elements
  #htmlElement = TemplateStringParser.parse(/*html*/ `
    <section id="main-container" class="container mt-3">
      <div class="text-center">
        <p><span class="badge text-bg-light">Flashcards geladen: <span id="flash-card-count"></span></span></p>
        <button class="btn btn-primary" id="starten">
            Spiel starten
        </button>
        <p id="game-is-running-info"><span class="badge text-bg-danger">Du kannst das Spiel erst starten, wenn das aktuelle Spiel beendet wird.</span></p>
      </div>
    </section>
  `);

  // HTML Elements
  #appHtmlElement = document.querySelector("#app");
  #flashCardCountHtmlElement =
    this.#htmlElement.querySelector("#flash-card-count");
  #startGameButton = this.#htmlElement.querySelector("#starten");
  #gameIsRunningInfo = this.#htmlElement.querySelector("#game-is-running-info");

  // Services
  #gameService = gameService;

  // Components
  #flashCardManagerComponent = new FlashCardManagerComponent();
  #lobbyComponent = new LobbyComponent();
  #flashCardGameResults = new FlashCardGameResults();
  #chatComponent = new ChatComponent();

  // Attributes
  #gameIsRunning = false;

  constructor() {
    super();
    Dev.log("GameComponent loaded");
    this.#gameService.addObserver(this);
    this.#initHtmlElement();
  }

  addChatMessage(chat) {
    this.#chatComponent.addChatMessage(chat);
  }

  /**
   * @param {FlashCard} flashCard
   */
  showNextFlashCard(flashCard) {
    if (this.#gameIsRunning === false) {
      this.#startGame();
      this.#flashCardManagerComponent.loadFirstFlashCard(flashCard);
    } else {
      this.#flashCardManagerComponent.loadNextFlashCard(flashCard);
    }
  }

  /**
   * @param {Player[]} players
   */
  loadLobbyUsers(players) {
    this.#lobbyComponent.addPlayers(players);
  }

  /**
   * @param {Player} player
   */
  userLeftTheLobby(player) {
    this.#lobbyComponent.removePlayer(player);
  }

  /**
   * @param {FlashCard[]} flashCards
   */
  loadFlashCards(flashCards) {
    this.#flashCardCountHtmlElement.innerHTML = flashCards.length.toString();
    if (this.#flashCardManagerComponent.totalFlashCardsCount === 0) {
      this.#flashCardManagerComponent.setTotalFlashCardsCount(
        flashCards.length
      );
    }
  }

  /**
   * @param {boolean} answerIsCorrect
   */
  handleIncomingAnswerResult(answerIsCorrect) {
    if (answerIsCorrect === true)
      this.#flashCardManagerComponent.handleCorrectAnswer();
    else this.#flashCardManagerComponent.handleWrongAnswer();
  }

  /**
   * @param {Player[]} players
   */
  finishGame(players) {
    this.#gameIsRunning = false;
    this.#toggleStartButtonActiveState(this.#gameIsRunning);
    this.#flashCardManagerComponent.popComponentHtmlElements();
    this.#flashCardGameResults.showResults(players);
  }

  fromGameBackToLobby() {
    this.#addComponentHtmlElements();
  }

  /**
   * @param {Game} game
   * Update comes from GameService
   */
  update(game) {
    if (game === null) return;
    if (game.gameIsRunning === false)
      this.#lobbyComponent.removePlayersPlayingInfo();
    this.#gameIsRunning = game.gameIsRunning;
    this.#toggleStartButtonActiveState(this.#gameIsRunning);
  }

  #initHtmlElement() {
    this.#addComponentHtmlElements();
    this.#toggleStartButtonActiveState(this.#gameIsRunning);
    this.#addListeners();
  }

  #addListeners() {
    this.#startGameButton.addEventListener("click", () => {
      this.#gameService.startGame();
    });
  }

  #startGame() {
    this.#gameIsRunning = true;
    this.#toggleStartButtonActiveState(this.#gameIsRunning);
    this.#popComponentHtmlElements();
  }

  #popComponentHtmlElements() {
    this.#htmlElement.parentNode.removeChild(this.#htmlElement);
  }

  #addComponentHtmlElements() {
    this.#appHtmlElement.appendChild(this.#htmlElement);
    this.#htmlElement.appendChild(this.#lobbyComponent.htmlElement);
    this.#appHtmlElement.appendChild(this.#chatComponent.htmlElement);
  }

  /**
   * @param {boolean} isInactive
   */
  #toggleStartButtonActiveState(isInactive) {
    // @ts-ignore
    this.#startGameButton.disabled = isInactive;
    // @ts-ignore
    this.#gameIsRunningInfo.style.display = isInactive ? "block" : "none";
  }
}

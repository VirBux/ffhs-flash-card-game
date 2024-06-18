class FlashCardManagerComponent extends Observer {
  // HTML Elements
  #htmlElement = TemplateStringParser.parse(/*html*/ `
    <section id="main-container" class="row justify-content-center">
      <div>Spieler im Spiel: <span id="players-in-game"></span></div>
      <div id="flash-card-container"></div>
      <div>Flash-Card: <span id="current-flash-card-count"></span> von <span id="total-flash-card-count"></span></div>
      <div>Deine Punkte: <span id="points"></span></div>
    </section>
  `);

  // HTML Elements
  #appHtmlElement = document.querySelector("#app");
  #flashCardContainerHtmlElement = this.#htmlElement.querySelector(
    "#flash-card-container"
  );
  #currentFlashCardCountHtmlElement = this.#htmlElement.querySelector(
    "#current-flash-card-count"
  );
  #totalFlashCardCountHtmlElement = this.#htmlElement.querySelector(
    "#total-flash-card-count"
  );
  #pointsHtmlElement = this.#htmlElement.querySelector("#points");
  #playersInGameHtmlElement =
    this.#htmlElement.querySelector("#players-in-game");

  // Attributes
  #totalFlashCardsCount = 0;
  #currentFlashCardCount = 1;
  #points = 0;

  constructor() {
    super();
    Dev.log("FlashCardManagerComponent loaded");

    playerService.addObserver(this);
  }

  get htmlElement() {
    return this.#htmlElement;
  }

  get totalFlashCardsCount() {
    return this.#totalFlashCardsCount;
  }

  setTotalFlashCardsCount(value) {
    this.#totalFlashCardsCount = value;
    this.#updateView();
  }

  /**
   * @param {FlashCard} flashCard
   */
  loadFirstFlashCard(flashCard) {
    this.#resetViewData();
    this.#flashCardContainerHtmlElement.innerHTML = "";
    const flashcard = new FlashCardComponent(flashCard);
    this.#flashCardContainerHtmlElement.appendChild(flashcard.htmlElement);
    this.#addComponentHtmlElements();
  }

  /**
   * @param {FlashCard} flashCard
   */
  loadNextFlashCard(flashCard) {
    this.#incrementCurrentFlashCardCount();
    this.#flashCardContainerHtmlElement.innerHTML = "";
    const flashcard = new FlashCardComponent(flashCard);
    this.#flashCardContainerHtmlElement.appendChild(flashcard.htmlElement);
  }

  handleCorrectAnswer() {
    toast.message("Deine Antwort war richtig!", "success");
    this.#incrementPoints();
  }

  handleWrongAnswer() {
    toast.message("Deine Antwort war falsch!", "danger");
  }

  /**
   * Update function for the observer pattern
   * @param {Player[]} players
   */
  update(players) {
    // Urs TODO: Schöner gestalten
    let playersList = "";
    players.forEach((player) => {
      if (player.playing) {
        playersList += `
            <li>Name: ${player.userName} Punkte: ${player.points}</li>
          `;
      }
    });

    this.#playersInGameHtmlElement.innerHTML = `<ul>${playersList}</ul>`;
  }

  #incrementPoints() {
    this.#points++;
    this.#updateView();
  }

  #incrementCurrentFlashCardCount() {
    this.#currentFlashCardCount++;
    this.#updateView();
  }

  #addComponentHtmlElements() {
    this.#appHtmlElement.appendChild(this.#htmlElement);
  }

  popComponentHtmlElements() {
    this.#appHtmlElement.removeChild(this.#htmlElement);
  }

  #resetViewData() {
    this.#currentFlashCardCount = 1;
    this.#points = 0;
    this.#updateView();
  }

  #updateView() {
    this.#currentFlashCardCountHtmlElement.innerHTML =
      this.#currentFlashCardCount.toString();
    this.#totalFlashCardCountHtmlElement.innerHTML =
      this.#totalFlashCardsCount.toString();

    this.#pointsHtmlElement.innerHTML = this.#points.toString();
  }
}

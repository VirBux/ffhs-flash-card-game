class FlashCardComponent {
  // HTML Elements
  #htmlElement = TemplateStringParser.parse(/*html*/ `
    <section id="flash-card-manager" class="row justify-content-center flashcard">
      <div class="col-11 col-md-8">
        <div class="mb-3">
          <form class="question-form">
            <div class="form-group">
              <label for="questionInput" id="fra" class="label-title">Frage</label>
              <div class="question-container" id="questionInput"></div>
            </div>
            <div class="mt-5">
              <!-- Title above answer buttons -->
              <label id="ant" class="label-title">Antwort wählen</label>
              <!-- Answer Buttons -->
              <button type="button" data-ansewer-index="0" class="btn btn-outline-secondary mb-2 btn-custom answer-button" id="answer1"></button>
              <button type="button" data-ansewer-index="1" class="btn btn-outline-secondary mb-2 btn-custom answer-button" id="answer2"></button>
              <button type="button" data-ansewer-index="2" class="btn btn-outline-secondary mb-2 btn-custom answer-button" id="answer3"></button>
            </div>
          </form>
        </div>
      </div>
    </section>
  `);

  // Attributes
  #uuid = "";

  // Services
  #flashCardService = flashCardService;

  get htmlElement() {
    return this.#htmlElement;
  }

  /**
   * @param {FlashCard} flashCard
   */
  constructor(flashCard) {
    this.#uuid = flashCard.uuid;
    Dev.log("FlashCardComponent loaded");
    this.#initHtmlElement();
    // @ts-ignore
    this.#htmlElement.querySelector("#questionInput").innerHTML =
      flashCard.question;

    this.#htmlElement.querySelector("#answer1").textContent =
      flashCard.answers[0];
    this.#htmlElement.querySelector("#answer2").textContent =
      flashCard.answers[1];
    this.#htmlElement.querySelector("#answer3").textContent =
      flashCard.answers[2];
  }

  #initHtmlElement() {
    this.#initButtonAnswerButtonsEventListerers();
  }

  #initButtonAnswerButtonsEventListerers() {
    const answerButtons = this.#htmlElement.querySelectorAll(".answer-button");
    answerButtons.forEach((button) => {
      button.addEventListener("click", (event) => {
        const answerIndex = button.getAttribute("data-ansewer-index");
        this.#answerClicked(Number(answerIndex));
        this.#disableAllButtons();
      });
    });
  }

  #disableAllButtons() {
    const answerButtons = this.#htmlElement.querySelectorAll(".answer-button");
    answerButtons.forEach((button) => {
      button.disabled = true;
    });
  }

  /**
   * @param {number} answerIndex
   */
  #answerClicked(answerIndex) {
    const flashCardPlayerAnswer = new FlashCardPlayerAnswer(
      playerService.yourPlayer.uuid,
      this.#uuid,
      answerIndex
    );
    this.#flashCardService.sendFlashCardPlayerAnswer(flashCardPlayerAnswer);
  }
}

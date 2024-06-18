class PlayerComponent {
  // HTML Elements
  #htmlElement = TemplateStringParser.parse(/*html*/ `
    <section id="player-component-section">
      <!-- Spieler Hinzufügen -->
      <section class="row justify-content-center">
        <div class="col-11 col-md-6">
            <div id="spi"><strong>Spieler hinzufügen</strong></div>
  
            <!-- User name section -->
          <div class="input-group mb-3">
            <input
              type="text"
              class="form-control"
              placeholder="Neuer Spielername"
              value=""
              id="new-player-name"
            />
          
            <div class="input-group-append">
              <button
                class="btn btn-outline-secondary"
                type="button"
                id="playerappend"
              >
                Hinzufügen
              </button>
            </div>
          </div>
        </div>
      </section>
      <!-- Spieler Lobby -->
      <section class="row justify-content-center">
        <div class="col-11 col-md-6">
          <div class="mb-3">
            <p id="lo"><strong>Spieler-Lobby</strong></p>
            <ul id="player-list" class="list-group">
              <!-- List entries will be appended here -->
            </ul>
          </div>
        </div>
      </section>
    </section>
  `);

  #playerListWrapperElement = this.#htmlElement.querySelector("#player-list");

  get htmlElement() {
    return this.#htmlElement;
  }

  constructor() {
    Dev.log("PlayerComponent loaded");
    this.#initHtmlElement();
  }

  #initHtmlElement() {
    this.#initPlayerAppendEventListener();
  }

  #initPlayerAppendEventListener() {
    this.#htmlElement
      .querySelector("#playerappend")
      .addEventListener("click", () => {
        const currentPlayerInputValue = this.#currentPlayerInputValue;

        if (currentPlayerInputValue === false) return;

        this.#playerListWrapperElement.appendChild(
          this.#generateListEntry(currentPlayerInputValue)
        );
      });
  }

  get #currentPlayerInputValue() {
    const playerName =
      // @ts-ignore
      this.#htmlElement.querySelector("#new-player-name").value;
    if (playerName.trim() !== "") return playerName;
    alert("Eingabe ist leer. Bitte gebe einen namen ein");
    return false;
  }

  #generateListEntry(playerName) {
    // Create Element
    const liElement = TemplateStringParser.parse(/*html*/ `
      <li class="list-group-item">
        <span>${playerName}</span>
        <button type="button" class="btn btn-light delete-button">Löschen</button>
      </li>
    `);

    return liElement;
  }
}

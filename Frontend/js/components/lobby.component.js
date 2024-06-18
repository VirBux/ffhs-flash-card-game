class LobbyComponent {
  // HTML Elements
  #htmlElement = TemplateStringParser.parse(/*html*/ `
    <section id="player-component-section">
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

  /**
   * @type {Player[]}
   */
  #players = [];

  get htmlElement() {
    return this.#htmlElement;
  }

  constructor() {
    Dev.log("PlayerComponent loaded");
  }

  /**
   * @param {Player[]} players
   */
  addPlayers(players) {
    this.#players = players;
    this.#updatePlayerList();
  }

  /**
   * @param {Player} player
   */
  addPlayer(player) {
    this.#players.push(player);
    this.#updatePlayerList();
  }

  /**
   * @param {Player} player
   */
  removePlayer(player) {
    this.#players = this.#players.filter((p) => p.uuid !== player.uuid);
    this.#updatePlayerList();
    toast.message(`${player.userName} hat die Lobby verlassen.`);
  }

  #updatePlayerList() {
    this.#playerListWrapperElement.innerHTML = "";
    this.#players.forEach((player) => {
      this.#playerListWrapperElement.appendChild(
        this.#generateListEntry(player)
      );
    });
  }

  removePlayersPlayingInfo() {
    this.#htmlElement
      .querySelectorAll(".is-playing-warning")
      .forEach((span) => {
        span.remove();
      });
  }

  /**
   *
   * @param {Player} player
   */
  #generateListEntry(player) {
    const isPlayingSpan = /*html*/ `
      <span class="badge text-bg-warning is-playing-warning">Aktuell in einem Spiel</span>
    `;
    const liElement = TemplateStringParser.parse(/*html*/ `
      <li data-uuid="${player.uuid}" class="list-group-item">
        <span>${player.userName}</span>
        ${player.playing ? isPlayingSpan : ""}
      </li>
    `);

    return liElement;
  }
}

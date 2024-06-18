class PlayerService extends ObserverAndSubject {
  #socketService = null;

  /**
   * @type {string}
   */
  #yourPlayerName = null;
  /**
   * @type {string}
   */
  #yourPlayerUuid = null;

  /**
   * @returns {Player}
   */
  get yourPlayer() {
    return new Player(this.#yourPlayerUuid, this.#yourPlayerName);
  }

  /**
   * @param {SocketService} socketService
   */
  addSocketService(socketService) {
    socketService.addObserver(this);
    this.#socketService = socketService;
  }

  /**
   * @returns {Message}
   */
  getUserNamePrompt() {
    let userName = prompt("Gebe bitte deinen Benutzernamen ein");
    if (userName == null || userName == "") {
      alert("Bitte gebe einen Benutzernamen ein");
      this.getUserNamePrompt();
    } else {
      return new Message(commandNames.addUser, { name: userName });
    }
  }

  /**
   * @param {Player} player
   */
  setYourPlayer(player) {
    this.#yourPlayerName = player.userName;
    this.#yourPlayerUuid = player.uuid;
  }

  /**
   * Incoming message from the server
   * @param {Message} serverMessage
   */
  update(serverMessage) {
    switch (serverMessage.commandName) {
      case serverCommandNames.newUserAddedServer:
        /**
         * @type {Player[]}
         */
        const players = serverMessage.body;
        game.loadLobbyUsers(players);
        break;

      case serverCommandNames.userLeftTheLobbyServer:
        /**
         * @type {Player}
         */
        const player = serverMessage.body;
        game.userLeftTheLobby(player);
        break;
      case serverCommandNames.yourCreatedUserServer:
        /**
         * @type {Player}
         */
        const yourPlayer = serverMessage.body;
        this.setYourPlayer(yourPlayer);
        break;

      case serverCommandNames.currentOnlineUsersServer:
        /**
         * @type {Player[]}
         */
        const allPlayers = serverMessage.body;
        this.notify(allPlayers);
        break;
    }
  }
}

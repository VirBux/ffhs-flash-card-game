class GameService extends Subject {
  #socketService = null;

  /**
   * @param {SocketService} socketService
   */
  addSocketService(socketService) {
    socketService.addObserver(this);
    this.#socketService = socketService;
  }

  startGame() {
    this.#socketService.sendMessage(new Message(commandNames.startGame, null));
  }

  /**
   * Incoming message from the server
   * @param {Message} serverMessage
   */
  update(serverMessage) {
    switch (serverMessage.commandName) {
      case serverCommandNames.noMoreCardsShowResultsServer:
        /**
         * @type {Player[]}
         */
        const allPlayersFinish = serverMessage.body;
        game.finishGame(allPlayersFinish);
        break;

      case serverCommandNames.serverCurrentGame:
        /**
         * @type {Game}
         */
        const currentGame = serverMessage.body;
        this.notify(currentGame);
        break;
    }
  }
}

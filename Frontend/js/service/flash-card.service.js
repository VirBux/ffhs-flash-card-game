class FlashCardService extends Observer {
  #socketService = null;

  /**
   * @param {SocketService} socketService
   */
  addSocketService(socketService) {
    socketService.addObserver(this);
    this.#socketService = socketService;
  }

  /**
   * @param {FlashCardPlayerAnswer} flashCardPlayerAnswer
   */
  sendFlashCardPlayerAnswer(flashCardPlayerAnswer) {
    this.#socketService.sendMessage(
      new Message(commandNames.addAnswer, flashCardPlayerAnswer)
    );
  }

  /**
   * Incoming message from the server
   * @param {Message} serverMessage
   */
  update(serverMessage) {
    switch (serverMessage.commandName) {
      case serverCommandNames.flashCardsServer:
        /**
         * @type {FlashCard[]}
         */
        const flashCards = serverMessage.body;
        game.loadFlashCards(flashCards);
        break;
      case serverCommandNames.nextFlashCardServer:
        /**
         * @type {FlashCard}
         */
        const flashCard = serverMessage.body;
        game.showNextFlashCard(flashCard);
        break;

      case serverCommandNames.answerResultServer:
        /**
         * @type {Object}
         */
        const answerIsCorrect = serverMessage.body.answerIsCorrect;
        game.handleIncomingAnswerResult(answerIsCorrect);
        break;
    }
  }
}

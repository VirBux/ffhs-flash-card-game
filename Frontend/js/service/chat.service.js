class ChatService extends Observer {
  #socketService = null;

  /**
   * @param {SocketService} socketService
   */
  addSocketService(socketService) {
    socketService.addObserver(this);
    this.#socketService = socketService;
  }

  /**
   * Send message to the server
   * @param {string} message
   */
  sendMessage(message) {
    const chat = new Chat(playerService.yourPlayer.userName, message);

    this.#socketService.sendMessage(
      new Message(commandNames.chatMessage, chat)
    );
  }

  /**
   * Incoming message from the server
   * @param {Message} serverMessage
   */
  update(serverMessage) {
    switch (serverMessage.commandName) {
      case serverCommandNames.serverChatMessage:
        /**
         * @type {Chat}
         */
        const chat = serverMessage.body;
        game.addChatMessage(chat);
        break;
    }
  }
}

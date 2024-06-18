class Message {
  /**
   * @param {string} commandName
   *
   */
  commandName;

  /**
   * @param {object} body
   */
  body;

  /**
   *
   * @param {string} commandName
   * @param {object} body
   */
  constructor(commandName, body) {
    this.commandName = commandName;
    this.body = body;
  }

  /**
   * @returns {string}
   */
  get toString() {
    return JSON.stringify(this);
  }
}

const commandNames = {
  addUser: "addUser",
  loadLobbyUsers: "loadLobbyUsers",
  startGame: "startGame",
  addAnswer: "addAnswer",
  getCurrentGame: "getCurrentGame",
  chatMessage: "chatMessage",
};

const serverCommandNames = {
  newUserAddedServer: "newUserAddedServer",
  userLeftTheLobbyServer: "userLeftTheLobbyServer",
  flashCardsServer: "flashCardsServer",
  nextFlashCardServer: "nextFlashCardServer",
  noMoreCardsShowResultsServer: "noMoreCardsShowResultsServer",
  answerResultServer: "answerResultServer",
  yourCreatedUserServer: "yourCreatedUserServer",
  currentOnlineUsersServer: "currentOnlineUsersServer",
  serverCurrentGame: "serverCurrentGame",
  serverChatMessage: "serverChatMessage",
};

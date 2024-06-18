class SocketService extends Subject {
  #baseUrl = "ws://localhost:";
  #port = 3030;
  #socket = new WebSocket(this.#baseUrl + this.#port);
  #playerService = playerService;

  constructor() {
    super();
    this.#socket.onopen = this.onOpen.bind(this);
    this.#socket.onmessage = this.onMessage.bind(this);
    this.#socket.onclose = this.onClose;
    this.#socket.onerror = this.onError;
  }

  onOpen(event) {
    Dev.log("Verbindung erfolgreich hergestellt.");
    const message = this.#playerService.getUserNamePrompt();
    this.sendMessage(message);
  }

  onMessage(event) {
    Dev.log("Nachricht vom Server:", event.data);
    const serverData = JSON.parse(event.data);
    const serverMessage = new Message(serverData.commandName, serverData.body);

    this.notify(serverMessage);

    // Schliessen der Verbindung, wenn die STOP_STRING-Nachricht empfangen wird
    if (event.data === "##") {
      this.#socket.close();
    }
  }

  onClose(event) {
    Dev.log("Verbindung wurde geschlossen.");
  }

  onError(error) {
    console.error("WebSocket-Fehler:", error);
  }

  /**
   * @param {Message} message
   */
  sendMessage(message) {
    if (this.#socket.readyState === WebSocket.OPEN) {
      this.#socket.send(message.toString);
      Dev.log("Nachricht gesendet:", message);
    } else {
      console.error(
        "WebSocket ist nicht offen. Nachricht kann nicht gesendet werden."
      );
    }
  }
}

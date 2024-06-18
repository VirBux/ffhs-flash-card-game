class ChatComponent {
  // HTML Elements
  #htmlElement = TemplateStringParser.parse(/*html*/ `
      <aside id="chat-aside">
        <h2>Chat</h2>
        <div class="row">
          <div class="col-12 chat-container"></div>
          <div class="col-12 mt-2 chat-controlls">
            <input class="form-control" type="text">
            <button class="btn btn-primary" id="send-message-btn">Senden</button>
          </div>
        </div>
      </aside>
    `);

  #chatContainer = this.#htmlElement.querySelector(".chat-container");
  #sendMessageBtn = this.#htmlElement.querySelector("#send-message-btn");
  #inputField = this.#htmlElement.querySelector("input");

  #chatService = chatService;

  get htmlElement() {
    return this.#htmlElement;
  }

  constructor() {
    Dev.log("ChatComponent loaded");
    this.#sendMessageBtn.addEventListener("click", () => this.#sendMessage());
    this.#inputField.addEventListener("keyup", (event) => {
      if (event.key === "Enter") {
        this.#sendMessage();
      }
    });
  }

  /**
   * @param {Chat} chat
   */
  addChatMessage(chat) {
    this.#chatContainer.appendChild(
      TemplateStringParser.parse(
        /*html*/ `<p><b>${chat.userName}</b>: ${chat.message}</p>`
      )
    );
  }

  #sendMessage() {
    this.#chatService.sendMessage(this.#inputField.value);
    this.#inputField.value = "";
  }
}

class Toast {
  /**
   * HTML Element lives once in the DOM
   * New Messages will be displayed in the same toast
   * To show a new message, call the message() method
   */
  #htmlElement = TemplateStringParser.parse(/*html*/ `
    <article class="toast-container position-fixed top-0 start-0 p-3">
      <div id="liveToast" class="toast align-items-center text-bg-secondary border-0" role="alert" aria-live="assertive" aria-atomic="true">
        <div class="d-flex">
          <div class="toast-body">
            <!-- Message here -->
          </div>
          <button type="button" class="btn-close btn-close-white me-2 m-auto" data-bs-dismiss="toast" aria-label="Close"></button>
        </div>
      </div>
    </article>
    
    `);

  #toastBody = this.#htmlElement.querySelector(".toast-body");
  #liveToast = this.#htmlElement.querySelector("#liveToast");

  constructor() {
    this.#initHtmlElement();
  }

  /**
   * @param {string} message
   */
  message(message, type) {
    this.#liveToast.classList.remove("bg-primary", "bg-success", "bg-danger");
    if (type) this.#liveToast.classList.add(`bg-${type}`);
    this.#toastBody.innerHTML = message;
    this.#showToast();
  }

  #showToast() {
    const toastLiveExample = document.getElementById("liveToast");
    const toastBootstrap =
      bootstrap.Toast.getOrCreateInstance(toastLiveExample);
    toastBootstrap.show();
  }

  #initHtmlElement() {
    if (!document.querySelector(".toast-container")) {
      document.body.appendChild(this.#htmlElement);
    }
  }
}

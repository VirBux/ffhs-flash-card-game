class ObserverAndSubject {
  #observers = [];

  constructor() {
    this.#observers = [];
  }

  addObserver(observer) {
    this.#observers.push(observer);
  }

  removeObserver(observer) {
    this.#observers = this.#observers.filter((obs) => obs !== observer);
  }

  notify(data) {
    this.#observers.forEach((observer) => observer.update(data));
  }

  /**
   * @param {any} data
   */
  update(data) {
    throw new Error("This method must be overwritten!");
  }
}

class FlashCardPlayerAnswer {
  /**
   * @param {string} uuid
   */
  userUuid = "";
  /**
   * @param {string} uuid
   */
  flashCardUuid = "";
  /**
   * @param {number} points
   */
  answerIndex = 0;

  /**
   * @param {string} userUuid
   * @param {string} flashCardUuid
   * @param {number} answerIndex
   */
  constructor(userUuid, flashCardUuid, answerIndex) {
    this.userUuid = userUuid;
    this.flashCardUuid = flashCardUuid;
    this.answerIndex = answerIndex;
  }
}

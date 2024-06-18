class Player {
  /**
   * @param {string} uuid
   */
  uuid = "";
  /**
   * @param {string} userName
   */
  userName = "";
  /**
   * @param {number} points
   */
  points = 0;
  /**
   * @param {boolean} isPlaying
   */
  playing = false;

  /**
   * @param {string} uuid
   * @param {string} userName
   * @param {number} [points]
   */
  constructor(uuid, userName, points) {
    this.uuid = uuid;
    this.userName = userName;
    this.points = points || 0;
  }
}

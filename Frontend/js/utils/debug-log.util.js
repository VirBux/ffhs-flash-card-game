const debug = true;

/**
 * Console Logger nur wenn man im Entwicklugnsmodus ist
 */
class Dev {
  static log(...args) {
    if (debug) console.log(...args);
  }
  static error(...args) {
    if (debug) console.error(...args);
  }
}

class TemplateStringParser {
  /**
   *
   * @param {*} templateString
   * @returns HTML Node Element
   */
  static parse(templateString) {
    let parser = new DOMParser();
    const htmlDoc = parser.parseFromString(templateString, "text/html");
    return htmlDoc.querySelector("html > body > *");
  }
}

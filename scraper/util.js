"use strict";

var x = require('x-ray')();

var HEADING_TAGS = ['h1', 'h2', 'h3', 'h4', 'h5', 'h6'];

var util = {
  scrape(url, scope, selector, callback) {
    callback = callback || ((obj) => console.log(JSON.stringify(obj, null, 4)));
    var indexStream = x(url, scope, selector).write();
    var index = '';
    indexStream.on('data', (c) => index += c);
    indexStream.on('end', () => callback(JSON.parse(index)));
  },

  forEachSectionInIndex(index, callback) {
    index.sections && index.sections.forEach((section) => {
      callback(section);
      util.forEachSectionInIndex(section, callback);
    });
  },

  HEADING_TAGS,

  isHeadingTag($elem) {
    if ($elem.length === 0) {
      return false;
    }
    return (HEADING_TAGS.indexOf($elem[0].name) !== -1);
  }
};

module.exports = util;

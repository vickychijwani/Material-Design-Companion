"use strict";

var cheerio = require('cheerio');

var util = require('./util');
var format = require('./format');
var io = require('./io');

var sectionContents = {
  scrape(section, callback) {
    util.scrape(section.href, '', {
      contents: '.chapter@html'
    }, (scraped) => {
      var contents = parseContents(scraped.contents, (contents) => {
        callback({
          title: section.title,
          href: section.href,
          content: contents
        });
      });
    });
  }
};

function parseContents(contentsHtml, contentsCallback) {
  var c = cheerio.load(contentsHtml);
  var parts = [];

  io.newRequestQueue(() => {
    console.log('REQUEST QUEUE COMPLETE!');
    contentsCallback(parts);
  });
  c('.chapter-content').children().each((i, elem) => {
    var $part = c(elem, contentsHtml),
      tag = $part[0].name,
      clazz = $part.attr('class');
    if (tag === 'figure') {
      parts.push(format.figure(c, $part));
    } else if (clazz === 'chapter-intro') {
      parts.push(format.chapterIntro(c, $part));
    } else if (clazz === 'article-list') {
      parts.push(format.articleList(c, $part));
    } else {
      console.error(`[ignored] <${tag} class="${clazz}">`);
    }
  });
  return parts;
}


module.exports = sectionContents;

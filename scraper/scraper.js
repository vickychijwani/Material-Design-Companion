#!/usr/bin/env node

"use strict";

var path = require('path');
var _ = require('lodash-node');
var x = require('x-ray')();

var util = require('./util');
var io = require('./io');
var sectionContents = require('./section-contents');

var STARTING_URL = 'http://www.google.com/design/spec/';

function scrapeIndex() {
  util.scrape(STARTING_URL, '#side-nav', {
    sections: x('dt', [{ title: '' }])
  }, scrapeIndexSubsections);
}

function scrapeIndexSubsections(indexOfTopLevelSections) {
  util.scrape(STARTING_URL, '#side-nav', {
    sections: x('dd', [{
      sections: x('a', [{ title: '', href: '@href' }])
    }])
  }, (indexOfSubsections) => {
    var fullIndex = _.merge(indexOfTopLevelSections, indexOfSubsections);
//    fullIndex.sections = fullIndex.sections.slice(0, 1);
    util.forEachSectionInIndex(fullIndex, (s) => {
      if (s.title) {
        s.title = s.title.trim();
      }
      if (s.href) {
        s.filepath = path.join(io.OUTPUT_DIR,
            s.href.split('/').slice(-2).join('/').replace(/html$/, 'json'));
      }
    });
    io.writeIndex(fullIndex);
    util.forEachSectionInIndex(fullIndex, (s) => {
      if (s.href) {
        sectionContents.scrape(s, (contents) => {
          io.writeSectionContents(contents, s.filepath);
        });
      }
    });
  });
}

scrapeIndex();

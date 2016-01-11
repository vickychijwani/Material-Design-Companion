"use strict";

var path = require('path');
var fs = require('fs');
var exec = require('child_process').exec

var request = require('request');
var RequestQueue = require("limited-request-queue");
var mkdirp = require('mkdirp');
var jsonfile = require('jsonfile');

var OUTPUT_DIR = 'spec';
var MEDIA_DIR = 'media';
var INDEX_FILENAME = 'index.json';

var requestQueue = null;

var io = {

  OUTPUT_DIR,
  MEDIA_DIR,

  newRequestQueue(queueCompleteCallback) {
    requestQueue = new RequestQueue({
      rateLimit: 100  // wait for these many msecs before each request
    }, {
      item: downloadFileFn,
      end: queueCompleteCallback
    });
  },

  writeIndex(index) {
    var filepath = path.join(OUTPUT_DIR, INDEX_FILENAME);
    console.log(`[io     ] writing index to ${filepath}`);
    mkdirp(path.dirname(filepath), (err) => console.error(err));
    jsonfile.writeFile(filepath, index, {spaces: 2}, (err) => console.error(err));
  },

  writeSectionContents(section, filepath) {
    console.log(`[io     ] writing section "${section.title}" to ${filepath}`);
    mkdirp(path.dirname(filepath), (err) => console.error(err));
    jsonfile.writeFile(filepath, section, {spaces: 2}, (err) => console.error(err));
  },

  downloadMedia(url) {
    var filepath = path.join(MEDIA_DIR, url.split('/').slice(3).join('/'));
    mkdirp(path.dirname(filepath), (err) => {
      if (err) { console.error(err); return; }
      enqueueFileDownload(url, filepath);
    });
    return filepath;
  }

};

function enqueueFileDownload(url, filepath) {
  requestQueue.enqueue({ url, filepath });
}

function downloadFileFn(input, done) {
  let url = input.url;
  let filepath = input.filepath;
  let callback = () => {
    console.log(`[dload   ] downloaded ${filepath}`);
    done();
  }

  request.head(url, function (err, res, body) {
    if (typeof res === 'undefined' || res === null || err) {
      console.error(`url: ${url}`);
      if (err) {
        console.error(err);
      }
    }
    fs.access(filepath, (err) => {
      if (err) {
        // file does not exist, download it
        request(url).pipe(fs.createWriteStream(filepath)).on('close', callback);
      } else {
        exec(`md5sum ${filepath} | cut -d' ' -f1`, (err, stdout, stderr) => {
          stderr = (typeof stderr === 'undefined' || stderr === null) ? '' : stderr;
          if (err || stderr.trim().length > 0 || res.headers['etag'] !== stdout.trim()) {
            // checksum mismatch, download file
            request(url).pipe(fs.createWriteStream(filepath)).on('close', callback);
          }
        });
      }
    });
  });
}

module.exports = io;
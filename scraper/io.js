"use strict";

var path = require('path');
var fs = require('fs');
var exec = require('child_process').exec;

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
    mkdirp(path.dirname(filepath), (err) => err && console.error(err));
    jsonfile.writeFile(filepath, index, {spaces: 2}, (err) => err && console.error(err));
  },

  writeSectionContents(section, filepath) {
    console.log(`[io     ] writing section "${section.title}" to ${filepath}`);
    mkdirp(path.dirname(filepath), (err) => err && console.error(err));
    jsonfile.writeFile(filepath, section, {spaces: 2}, (err) => err && console.error(err));
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

/**
 * @param {string} filepath
 * @param {string} newFileHash - updated hash of file
 * @param {function} callback
 */
function checkFileHash(filepath, newFileHash, callback) {
  exec(`md5sum ${filepath} | cut -d' ' -f1`, (err, stdout, stderr) => {
    stderr = (typeof stderr === 'undefined' || stderr === null) ? '' : stderr;
    if (err || stderr.trim().length > 0 || (newFileHash && newFileHash !== stdout.trim())) {
      callback(true);
    }
  });
}

function downloadFileFn(input, done) {
  let url = input.url;
  let filepath = input.filepath;
  let download = (newFileHash) => {
    request(url).pipe(fs.createWriteStream(filepath)).on('close', checkDownloadedFile.bind(null, newFileHash));
  };
  let checkDownloadedFile = (newFileHash) => {
    checkFileHash(filepath, newFileHash, (mismatch) => {
      if (mismatch) {
        console.error(`[dload   ] hash mismatch for ${filepath}, retrying download`);
        download();
      } else {
        console.log(`[dload   ] downloaded ${filepath}`);
        done();
      }
    });
  };

  request.head(url, function (err, res, body) {
    if (typeof res === 'undefined' || res === null || err) {
      console.error(`url: ${url}`);
      if (err) {
        console.error(err);
      }
    }
    const newFileHash = res.headers['etag'];
    fs.access(filepath, (err) => {
      if (err) {
        // file does not exist, download it
        download(newFileHash);
      } else {
        checkFileHash(filepath, newFileHash, (mismatch) => {
          if (mismatch) {
            download(newFileHash);
          }
        });
      }
    });
  });
}

module.exports = io;

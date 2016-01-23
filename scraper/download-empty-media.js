#!/usr/bin/env node

"use strict";

var exec = require('child_process').exec;

var io = require('./io');

const BASE_URL = 'http://material-design.storage.googleapis.com';

function downloadMedia(filepaths) {
  io.newRequestQueue(() => {
    console.log(`All media downloaded`);
  });
  filepaths.forEach((filepath) => {
    var url = getMediaUrlFromFilepath(filepath);
    io.downloadMedia(url);
  });
}

function getMediaUrlFromFilepath(filepath) {
  var pathParts = filepath.split('/');
  pathParts.shift();
  return `${BASE_URL}/${pathParts.join('/')}`;
}

function withEmptyMediaPaths(callback) {
  exec(`./check-empty-media.sh`, (err, stdout, stderr) => {
    stdout = stdout.trim();
    if (stdout.length === 0) {
      callback([]);
      return;
    }
    var filepaths = stdout.split('\n');
    callback(filepaths);
  });
}

function downloadEmptyMedia() {
  withEmptyMediaPaths((filepaths) => {
    if (filepaths.length === 0) {
      console.log(`Nothing to download`);
      return;
    }
    downloadMedia(filepaths);
  });
}

downloadEmptyMedia();

"use strict";

var util = require('./util');
var io = require('./io');

var format = {
  figure(c, $figure) {
    var figure = {
      type: 'figure'
    };
    if ($figure.find('img').length > 0) {
      figure.mediaType = 'image';
      figure.src = io.downloadMedia($figure.find('img').attr('src'));
    } else if ($figure.find('video').length > 0) {
      figure.mediaType = 'video';
      figure.src = io.downloadMedia(c($figure.find('video source')[0], $figure).attr('src'));
    } else {
      console.error(`Unhandled <figure>:\n${$figure.html()}`);
    }
    if ($figure.find('figcaption').html()) {
      figure.caption = $figure.find('figcaption').html().trim();
    }
    return figure;
  },

  figureGroup(c, $figureGroup) {
    var figures = [];
    $figureGroup.find('figure').each((i, e) => {
      figures.push(format.figure(c, c(e, $figureGroup)));
    });
    return {
      type: 'figure-group',
      figures: figures
    }
  },

  chapterIntro(c, $intro) {
    if ($intro.find('section').length === 0) {
      return {
        type: 'intro',
        html: $intro.html().trim()
      };
    }
    var chapterIntro = format.article(c, $intro);
    delete chapterIntro.title;
    chapterIntro.type = 'intro';
    return chapterIntro;
  },

  articleList(c, $articleList) {
    var articles = [];
    $articleList.find('.article').each((i, e) => {
      articles.push(format.article(c, c(e, $articleList)));
    });
    return {
      type: 'article-list',
      articles: articles
    };
  },

  article(c, $article) {
    var modules = [];
    $article.find('section').each((i, e) => {
      modules.push(format.module(c, c(e, $article)));
    });
    return {
      type: 'article',
      title: format.articleTitle(c, $article.find('.article-title')),
      modules: modules
    }
  },

  articleTitle(c, $articleTitle) {
    var title = null;
    $articleTitle.contents().filter(function () {
      if (this.type === 'text' && this.data.trim().length > 0) {
        title = this.data.trim();
        return false;
      }
    });
    return title;
  },

  module(c, $module) {
    var moduleParts = [], errors = [];
    $module.children().each((i, e) => {
      try {
        moduleParts.push(format.modulePart(c, c(e, $module)));
      } catch (e) {
        errors.push(e.message);
      }
    });
    var module = {
      type: 'module',
      content: moduleParts.filter((p) => p !== null)
    };
    if (errors.length > 0) {
      module.errors = errors;
    }
    util.HEADING_TAGS.forEach((tag) => {
      var $heading = $module.children(tag);
      if ($heading.length > 0) {
        module.title = $heading.text();
      }
    });
    return module;
  },

  modulePart(c, $modulePart) {
    var tag = $modulePart[0].name,
      clazz = $modulePart.attr('class');
    if (clazz === 'figure-group') {
      return format.figureGroup(c, $modulePart);
    } else if (clazz === 'module-body') {
      return format.moduleBody(c, $modulePart);
    } else if (util.HEADING_TAGS.indexOf(tag) !== -1) {
      // nothing to do since this is handled separately by format.module()
      return null;
    } else {
      throw new Error(`[ignored] <${tag} class="${clazz}">`);
      return null;
    }
  },

  moduleBody(c, $moduleBody) {
    return {
      type: 'module-body',
      html: $moduleBody.html().trim()
    };
  }
};

module.exports = format;

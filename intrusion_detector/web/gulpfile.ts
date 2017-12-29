'use strict';

import * as gulp from 'gulp';

const copyFiles = [
  './server/package.json',
  './server/pm2.config.json',
  './server/config.json'
];
gulp.task('copy-data', () => {
        return gulp.src(copyFiles , { base: 'dist' })
        .pipe(gulp.dest('dist/server'));
});

gulp.task('copy-resources', ['copy-data']);

const fs = require('fs-extra')

fs.copy('./server/pm2.config.json', './dist/server/pm2.config.json', { replace: false }, function (err) {
  if (err) {
    console.log("file already exists or can't write to directory"); 
    throw err;
  }
  console.log("pm2.config.json copied in dist/server");
});

fs.copy('./server/package.json', './dist/server/package.json', { replace: false }, function (err) {
  if (err) {
    console.log("file already exists or can't write to directory"); 
    throw err;
  }
  console.log("package.json copied in dist/server");
});



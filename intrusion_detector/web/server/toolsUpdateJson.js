const fs = require('fs-extra')
fs.readFile('./dist/server/pm2.config.json',{encoding: 'utf8'},function(err,data) {
  var config = JSON.parse(data);
  config.apps[0].script = "./bin/www.js";
  var string = JSON.stringify(config,null,'\t');
  console.log(config);

  fs.writeFile('./dist/server/pm2.config.json',string,function(err) {
  if(err) {
    return console.error(err);
  }
  console.log('done');
  })
})
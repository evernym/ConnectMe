const localtunnel = require('localtunnel');
const fs = require('fs');

(async () => {
  const tunnel = await localtunnel({ port: 8020 });

  fs.writeFile('lt.txt', tunnel.url, err => {
    if (err) {
      console.error(err)
      return
    }
  })
})()

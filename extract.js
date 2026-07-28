const fs = require('fs');
const PNG = require('pngjs').PNG;

fs.createReadStream('image_0.png')
  .pipe(new PNG({ filterType: 4 }))
  .on('parsed', function() {
    console.log("Width:", this.width, "Height:", this.height);
    // Find boundaries or simple pixel dump
  });

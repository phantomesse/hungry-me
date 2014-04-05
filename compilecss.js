var autoprefixer = require('autoprefixer');
var less = require('less');
var fs = require('fs') // for reading files


// Read styles.less
var cssFilePath = 'war/css/styles.less';
fs.readFile(cssFilePath, 'utf8', function(err, data) {
    if (err) {
        return console.log(err);
    }

    // Convert from LESS to CSS
    less.render(data, function (e, css) {
        // Auto prefix that shit
        css = autoprefixer.process(css).css;

        // Write output to styles.css
        fs.writeFile('war/css/styles.css', css, function(err) {
            if(err) {
                console.log(err);
            } else {
                console.log(cssFilePath + ' was updated');
            }
        });
    });
});
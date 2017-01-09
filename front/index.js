var path = require('path');
var express = require('express');
var compression = require('compression');

var port = process.env.PORT || '9001';
var rootPath = path.normalize(__dirname);

var app = express();
var deployMode = process.argv[2] === "deploy";

var deployFolder = "" + (deployMode ? '/deploy' : '');

app.use(function (req, res, next) {
    if (!req.secure && process.env.NODE_ENV === 'production' && req.headers['x-forwarded-proto'] !== 'https') {
        return res.redirect('https://' + req.headers.host + req.url);
    }

    next();
});

// app.get('/', function (req, res) {
//     return res.redirect('/portal');
// });

app.use(compression());

app.use('/', express.static(path.resolve(path.join(__dirname, deployFolder))));

app.set('view engine', 'html');
app.set('views', path.join(rootPath, deployFolder));

app.listen(port, undefined, function () {
    console.log('Listening on port %d %s', port, (deployMode ? 'using deploy mode' : ''));
});
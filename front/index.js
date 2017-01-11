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

// Public route
app.get('/api/deals/public', function (req, res) {
  var deals = [
	{
		id: 1234,
		name: 'Name of Product',
		description: 'Description of Product',
		originalPrice: 19.99, // Original price of product
		salePrice: 9.99 // Sale price of product
	}
  ];
  res.json(deals);
});
// Private route 
app.get('/api/deals/private', function (req, res) {
  var deals = [
	{
		id: 5555,
		name: 'Name of Product',
		description: 'Description of Product',
		originalPrice: 19.99, // Original price of product
		salePrice: 9.99 // Sale price of product
	}
  ];
  res.json(deals);
});

app.post('/api/authenticate', function (req, res) {
	console.log(req.body);
	res.json({logged: true});
});
app.use(compression());

app.use('/', express.static(path.resolve(path.join(__dirname, deployFolder))));

app.set('view engine', 'html');
app.set('views', path.join(rootPath, deployFolder));

app.listen(port, undefined, function () {
    console.log('Listening on port %d %s', port, (deployMode ? 'using deploy mode' : ''));
});
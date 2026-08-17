const http = require("http");

const server = http.createServer((req, res) => {

    res.writeHead(200, {
        "Content-Type": "text/plain"
    });

    if (req.url === "/") {
        res.end("Welcome to Home Page");

    } else if (req.url === "/about") {
        res.end("About Us");

    } else if (req.url === "/contact") {
        res.end("Contact Information");

    } else if (req.url === "/services") {
        res.end("Our Services");

    } else {
        res.writeHead(404);
        res.end("404 - Page Not Found");
    }
});

server.listen(3000, () => {
    console.log("Server is running on http://localhost:3000");
});
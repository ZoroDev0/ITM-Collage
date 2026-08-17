const http = require("http");

const server = http.createServer((req, res) => {
    res.writeHead(200, {
        "Content-Type": "text/html"
    });

    const html = `
        <!DOCTYPE html>
        <html>
        <head>
            <title>Student Portal</title>
        </head>
        <body>
            <h1>Student Portal</h1>

            <hr>

            <p><strong>Name:</strong>Zoro</p>
            <p><strong>Course:</strong> BTech in Computer Science</p>
            <p><strong>College:</strong>ITM Skills</p>

            <p>Welcome to our Node.js application.</p>
        </body>
        </html>
    `;

    res.end(html);
});

server.listen(3000, () => {
    console.log("Server is running on http://localhost:3000");
});
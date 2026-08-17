const http = require("http");

const server = http.createServer((req, res) => {

    if (req.url === "/student") {
        res.writeHead(200, {
            "Content-Type": "application/json"
        });

        const student = {
            id: 101,
            name: "Zoro",
            course: "BTech in Computer Science",
            semester: 4,
            city: "Mumbai"
        };

        res.end(JSON.stringify(student));
    } else {
        res.writeHead(404, {
            "Content-Type": "text/plain"
        });

        res.end("404 - Page Not Found");
    }
});

server.listen(3000, () => {
    console.log("Server is running on http://localhost:3000");
});
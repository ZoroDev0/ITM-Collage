const http = require("http");

const server = http.createServer((req, res) => {

    res.writeHead(200, {
        "Content-Type": "text/html"
    });

    let content = "";

    if (req.url === "/") {

        content = `
            <h1>Home</h1>
            <p>Welcome to my personal portfolio.</p>
        `;

    } else if (req.url === "/about") {

        content = `
            <h1>About Me</h1>
            <p>Hello! I am a student learning Full Stack Development and Node.js.</p>
        `;

    } else if (req.url === "/skills") {

        content = `
            <h1>Skills</h1>
            <ul>
                <li>HTML</li>
                <li>CSS</li>
                <li>JavaScript</li>
                <li>Node.js</li>
            </ul>
        `;

    } else if (req.url === "/projects") {

        content = `
            <h1>Projects</h1>
            <ul>
                <li>Student Management System</li>
                <li>Portfolio Website</li>
                <li>Node.js HTTP Server</li>
            </ul>
        `;

    } else if (req.url === "/contact") {

        content = `
            <h1>Contact Details</h1>
            <p>Email: zoro@example.com</p>
            <p>Phone: +91 9876543210</p>
        `;

    } else {

        res.writeHead(404, {
            "Content-Type": "text/html"
        });

        res.end(`
            <h1>404 - Page Not Found</h1>
            <a href="/">Go Home</a>
        `);

        return;
    }

    const navigation = `
        <nav>
            <a href="/">Home</a> |
            <a href="/about">About Me</a> |
            <a href="/skills">Skills</a> |
            <a href="/projects">Projects</a> |
            <a href="/contact">Contact</a>
        </nav>

        <hr>
    `;

    const html = `
        <!DOCTYPE html>
        <html>
        <head>
            <title>My Portfolio</title>
        </head>

        <body>
            ${navigation}

            ${content}
        </body>
        </html>
    `;

    res.end(html);
});

server.listen(3000, () => {
    console.log("Server is running on http://localhost:3000");
});
const express = require("express");

const app = express();
const router = express.Router();
const PORT = 3000;

function logger(req, res, next) {
    const dateTime = new Date().toLocaleString();
    console.log(`${req.method} ${req.originalUrl} ${dateTime}`);
    next();
}

function responseTimeLogger(req, res, next) {
    const startTime = Date.now();

    res.on("finish", () => {
        const responseTime = Date.now() - startTime;
        console.log(`${req.method} ${req.originalUrl} - ${responseTime} ms`);
    });

    next();
}

function routerLogger(req, res, next) {
    const dateTime = new Date().toLocaleString();
    console.log(`${req.method} ${req.originalUrl} ${dateTime}`);
    next();
}

app.use(logger);
app.use(responseTimeLogger);

app.get("/", (req, res) => {
    res.send("Welcome to Home Page");
});

app.get("/about", (req, res) => {
    res.send("About Us");
});

app.get("/contact", (req, res) => {
    res.send("Contact Information");
});

app.get("/products", (req, res) => {
    res.send("Product List");
});

app.get("/users", (req, res) => {
    res.send("User List");
});

router.use(routerLogger);

router.get("/students", (req, res) => {
    res.send("Students List");
});

router.get("/courses", (req, res) => {
    res.send("Courses List");
});

router.get("/faculty", (req, res) => {
    res.send("Faculty List");
});

app.use("/api", router);

app.listen(PORT, () => {
    console.log(`Server running at http://localhost:${PORT}`);
});
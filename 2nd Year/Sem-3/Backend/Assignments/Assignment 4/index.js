const fs = require("fs");
const path = require("path");

// File paths inside the current Assignment 4 folder
const studentFile = path.join(__dirname, "student.txt");
const studentDetailsFile = path.join(__dirname, "studentDetails.txt");

// Task 1: Create Student Information File
const studentData = `Name: Sasanka Sekhar Kundu
Course: Full Stack Development
Technology: Node.js`;

fs.writeFile(studentFile, studentData, (err) => {
    if (err) {
        console.error("Error creating file:", err);
        return;
    }

    console.log("File created successfully");

    // Task 2: Read Student Information
    fs.readFile(studentFile, "utf8", (err, data) => {
        if (err) {
            console.error("Error reading file:", err);
            return;
        }

        console.log("\nStudent Information:");
        console.log(data);

        // Task 3: Update Student Information
        const additionalData = `
Experience: 1 Year
City: Kolkata`;

        fs.appendFile(studentFile, additionalData, (err) => {
            if (err) {
                console.error("Error updating file:", err);
                return;
            }

            console.log("\nData updated successfully");

            // Read updated file
            fs.readFile(studentFile, "utf8", (err, updatedData) => {
                if (err) {
                    console.error("Error reading updated file:", err);
                    return;
                }

                console.log("\nUpdated Student Information:");
                console.log(updatedData);

                // Task 4: Rename File
                fs.rename(studentFile, studentDetailsFile, (err) => {
                    if (err) {
                        console.error("Error renaming file:", err);
                        return;
                    }

                    console.log("\nFile renamed successfully");

                    // Task 5: Delete File
                    fs.unlink(studentDetailsFile, (err) => {
                        if (err) {
                            console.error("Error deleting file:", err);
                            return;
                        }

                        console.log("File deleted successfully");
                    });
                });
            });
        });
    });
});
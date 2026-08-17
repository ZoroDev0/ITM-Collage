// Promise-based Recruitment Evaluation System

// Question 1: Coding Assessment
function codingScoreCheck(marks, cutoff) {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            const average =
                marks.reduce((sum, mark) => sum + mark, 0) / marks.length;

            if (average >= cutoff) {
                resolve(average);
            } else {
                reject("Sorry, you have not cleared the Coding Assessment.");
            }
        }, 2000);
    });
}


// Question 2: Technical Interview
function technicalInterviewCheck(marks, cutoff) {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            const average =
                marks.reduce((sum, mark) => sum + mark, 0) / marks.length;

            if (average >= cutoff) {
                resolve(average);
            } else {
                reject("Sorry, you have not cleared the Technical Interview.");
            }
        }, 2000);
    });
}


// Question 3: Final Selection Review
function finalSelectionCheck(codingAverage, technicalAverage, cutoff) {
    return new Promise((resolve, reject) => {
        setTimeout(() => {
            const finalAverage =
                (codingAverage + technicalAverage) / 2;

            if (finalAverage >= cutoff) {
                resolve(finalAverage);
            } else {
                reject(
                    "Sorry, you have not cleared the final selection cutoff."
                );
            }
        }, 2000);
    });
}


// Test Data
const codingMarks = [80, 75, 90];
const technicalMarks = [85, 70, 80];

const codingCutoff = 70;
const technicalCutoff = 70;
const finalCutoff = 75;


// Start Recruitment Evaluation
codingScoreCheck(codingMarks, codingCutoff)

    .then((codingAverage) => {
        console.log("Coding Assessment cleared!");
        console.log("Coding Average:", codingAverage);

        return technicalInterviewCheck(
            technicalMarks,
            technicalCutoff
        ).then((technicalAverage) => {
            return {
                codingAverage,
                technicalAverage
            };
        });
    })

    .then(({ codingAverage, technicalAverage }) => {
        console.log("Technical Interview cleared!");
        console.log("Technical Average:", technicalAverage);

        return finalSelectionCheck(
            codingAverage,
            technicalAverage,
            finalCutoff
        );
    })

    .then((finalAverage) => {
        console.log("Final Selection cleared!");
        console.log("Final Average:", finalAverage);
        console.log("Congratulations! You have been selected.");
    })

    .catch((error) => {
        console.log(error);
    });
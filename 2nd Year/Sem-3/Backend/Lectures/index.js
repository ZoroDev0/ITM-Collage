let students = [
    {name:"A", marks: 80},
    {name:"B", marks: 40},
    {name:"C", marks: 90},
    {name:"D", marks: 71},
    {name:"E", marks: 50},
    {name:"F", marks: 76},
    {name:"G", marks: 30},
];

const x = students.filter((student) => student.marks >= 50);
const y = students.filter((student) => student.marks % 2 !== 0);
console.log(x);
console.log(y);

let arr = [1, 2, 3, 4, 5];

const z = arr.filter((a) => a % 2 == 0);
console.log(z);

const c = z.reduce((acc, curr) => acc + curr, 0);
console.log(c);

const e = arr.filter((a) => a % 2 == 0).reduce((acc, curr) => acc + curr, 0);
console.log(e);
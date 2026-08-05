// Define an interface
interface Student {
    id: number;
    name: string;
    course: string;
}

// Create an object that follows the interface
const student: Student = {
    id: 101,
    name: "Harsh",
    course: "Computer Science"
};

// Print the details
console.log("Student Details");
console.log("----------------");
console.log("ID:", student.id);
console.log("Name:", student.name);
console.log("Course:", student.course);
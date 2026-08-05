// // Interface
// interface Student {
//     id: number;
//     name: string;
//     course: string;

//     display(): void;
// }

// // Class implementing the interface
// class StudentInfo implements Student {

//     id: number;
//     name: string;
//     course: string;

//     // Constructor
//     constructor(id: number, name: string, course: string) {
//         this.id = id;
//         this.name = name;
//         this.course = course;
//     }

//     // Method implementation
//     display(): void {
//         console.log("Student Details");
//         console.log("----------------");
//         console.log("ID:", this.id);
//         console.log("Name:", this.name);
//         console.log("Course:", this.course);
//     }
// }

// // Create object
// const student1 = new StudentInfo(
//     101,
//     "Harsh",
//     "Computer Science"
// );

// // Call method
// student1.display();

class Student {

    private id: number;
    private name: string;
    private course: string;

    // Default Constructor
    constructor() {
        this.id = 101;
        this.name = "Harsh";
        this.course = "Computer Science";

        console.log("Default Constructor Called");
    }

    // Method
    public display(): void {
        console.log("\nStudent Details");
        console.log("----------------");
        console.log("ID      :", this.id);
        console.log("Name    :", this.name);
        console.log("Course  :", this.course);
    }
}

// Creating Object
const student1 = new Student();

// Calling Method
student1.display();
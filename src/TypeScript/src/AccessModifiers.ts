// Interface
interface Student {
    display(): void;
}

// Class implementing the interface
class StudentInfo implements Student {

    // Private properties
    private id: number;
    private name: string;
    private course: string;

    // Constructor
    constructor(id: number, name: string, course: string) {
        this.id = id;
        this.name = name;
        this.course = course;
    }

    // Public method
    public display(): void {
        console.log("Student Details");
        console.log("----------------");
        console.log("ID:", this.id);
        console.log("Name:", this.name);
        console.log("Course:", this.course);
    }

    // Getter Methods
    public getId(): number {
        return this.id;
    }

    public getName(): string {
        return this.name;
    }

    public getCourse(): string {
        return this.course;
    }

    // Setter Methods
    public setName(name: string): void {
        this.name = name;
    }

    public setCourse(course: string): void {
        this.course = course;
    }
}

// Create Object
const student = new StudentInfo(101, "Harsh", "Computer Science");

// Display Details
student.display();

console.log();

// Using Getter
console.log("Student Name:", student.getName());

console.log();

// Using Setter
student.setName("Rahul");
student.setCourse("Information Technology");

console.log("After Updating Details");
student.display();
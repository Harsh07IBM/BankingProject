class Student {

    private id: number;
    private name: string;

    // Constructor Signatures
    constructor();
    constructor(id: number, name: string);

    // Single Constructor Implementation
    constructor(id?: number, name?: string) {

        if (id !== undefined && name !== undefined) {
            this.id = id;
            this.name = name;
            console.log("Parameterized Constructor Called");
        } else {
            this.id = 101;
            this.name = "Harsh";
            console.log("Default Constructor Called");
        }
    }

    public display(): void {
        console.log("----------------------");
        console.log("ID   :", this.id);
        console.log("Name :", this.name);
    }
}

// Default Constructor
const student1 = new Student();
student1.display();

console.log();

// Parameterized Constructor
const student2 = new Student(102, "Rahul");
student2.display();
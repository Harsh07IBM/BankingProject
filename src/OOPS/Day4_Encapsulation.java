class StudentData {

    private String studentName;

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}

public class Day4_Encapsulation {

    public static void main(String[] args) {

        StudentData student = new StudentData();

        student.setStudentName("Harsh Saini");

        System.out.println("Student Name: " +
                student.getStudentName());
    }
}

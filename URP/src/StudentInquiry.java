import javax.swing.*;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentInquiry extends JFrame {

    private int studentId;

    public StudentInquiry(int studentId) {
        super("학적 조회 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        this.studentId = studentId;

        displayStudentInfo();

        setVisible(true);
    }

    private void displayStudentInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        try {
            ResultSet resultSet = DAO.GetStudent(studentId);

            if (resultSet != null && resultSet.next()) {
                String name = resultSet.getString("sname");
                String department = resultSet.getString("departmentname");
                String enrollmentStatus = "0".equals(resultSet.getString("status")) ? "재학" : "휴학";
                String birthday = resultSet.getString("birthdate");
                String advisor = resultSet.getString("pname");
                int grade = resultSet.getInt("grade");
                
                JLabel nameLabel = new JLabel("이름: " + name);
                JLabel departmentLabel = new JLabel("학과: " + department);
                JLabel enrollmentStatusLabel = new JLabel("재학상태: " + enrollmentStatus);
                JLabel birthdayLabel = new JLabel("생일: " + birthday);
                JLabel advisorLabel = new JLabel("지도교수: " + advisor);
                JLabel gradeLabel = new JLabel("학년: " + grade);

                panel.add(nameLabel);
                panel.add(departmentLabel);
                panel.add(enrollmentStatusLabel);
                panel.add(birthdayLabel);
                panel.add(advisorLabel);
                panel.add(gradeLabel);
            }

            if (resultSet != null) {
                resultSet.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        add(panel, BorderLayout.CENTER);
    }
}

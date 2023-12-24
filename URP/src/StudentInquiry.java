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

        // 학적 조회 페이지에 필요한 컴포넌트 및 로직을 추가합니다.
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

                // 패널에 추가
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
            // TODO: 에러 처리 로직을 추가할 수 있습니다.
        }

        add(panel, BorderLayout.CENTER);
    }

}

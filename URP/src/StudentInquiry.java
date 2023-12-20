import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class StudentInquiry extends JFrame {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/URP";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public StudentInquiry() {
        super("학적 조회 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // 학적 조회 페이지에 필요한 컴포넌트 및 로직을 추가합니다.
        displayStudentInfo();

        setVisible(true);
    }

    private void displayStudentInfo() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 2));

        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             String query = "SELECT sname, departmentid, status, birthdate, advisorid, grade FROM students"; // 학적테이블은 실제 테이블 이름으로 변경해야 합니다.

            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                String name = resultSet.getString("sname");
                String department = resultSet.getString("departmentid");
                String enrollmentStatus = resultSet.getString("status");
                String birthday = resultSet.getString("birthdate");
                String advisor = resultSet.getString("advisorid");
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

            resultSet.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: 에러 처리 로직을 추가할 수 있습니다.
        }

        add(panel, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentInquiry();
        });
    }
}

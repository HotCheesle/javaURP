import javax.swing.*;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StudentGrade extends JFrame {

    private String studentId; // SID 저장 변수 추가
    private DefaultListModel<String> gradeListModel;

    public StudentGrade(String studentId) {
        super("성적 조회 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        this.studentId = studentId; // SID 설정
        this.gradeListModel = new DefaultListModel<>();

        // 성적 조회 페이지에 필요한 컴포넌트 및 로직을 추가합니다.
        displayStudentGrade();

        setVisible(true);
    }

    private void displayStudentGrade() {
        JList<String> gradeList = new JList<>(gradeListModel);
        JScrollPane scrollPane = new JScrollPane(gradeList);

        try {
            DAO.SetConnection("urp", "root", "root");

            // SID에 해당하는 학생이 듣고 있는 강의 정보 조회
            String query = "SELECT class.classname, listeningclass.score FROM listeningclass " +
                           "JOIN class ON listeningclass.CID = class.CID WHERE SID = ?";
            PreparedStatement statement = DAO.conn.prepareStatement(query);
            statement.setString(1, studentId); // SID 설정

            ResultSet resultSet = statement.executeQuery();

            List<String> grades = new ArrayList<>();
            while (resultSet.next()) {
                String courseName = resultSet.getString("classname"); // 강의 코드 대신 과목 명 사용
                int score = resultSet.getInt("score");
                String gradeInfo = "과목 명: " + courseName + " - 성적: " + score; // 수정된 부분
                grades.add(gradeInfo);
            }

            resultSet.close();
            statement.close();

            // JList에 성적 정보 추가
            for (String grade : grades) {
                gradeListModel.addElement(grade);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: 에러 처리 로직을 추가할 수 있습니다.
        }

        add(scrollPane, BorderLayout.CENTER);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 여기서는 예시로 "s123"이라는 SID로 StudentGrade를 호출하고 있습니다.
            // 로그인 시 사용자의 SID를 전달받아야 합니다.
            new StudentGrade("s123");
        });
    }
}

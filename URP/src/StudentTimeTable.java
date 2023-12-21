import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class StudentTimeTable extends JFrame {

    private String studentId; // SID 저장 변수 추가
    private JTable timeTable;

    public StudentTimeTable(String studentId) {
        super("수강 과목 조회 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);
        setLayout(new BorderLayout());

        this.studentId = studentId; // SID 설정

        // 수강 과목 조회 페이지에 필요한 컴포넌트 및 로직을 추가합니다.
        displayTimeTable();

        setVisible(true);
    }

    private void displayTimeTable() {
        // 테이블 모델 생성
        DefaultTableModel tableModel = new DefaultTableModel();
        timeTable = new JTable(tableModel);

        // 컬럼명 설정
        String[] columnNames = {"과목명", "강의 번호", "강의실", "수업 시간", "학점"};
        tableModel.setColumnIdentifiers(columnNames);

        try {
            DAO.SetConnection("urp", "root", "root");

            // SID에 해당하는 학생이 수강 중인 과목을 찾습니다.
            String listeningClassQuery = "SELECT CID FROM listeningclass WHERE SID = ?";
            PreparedStatement listeningClassStatement = DAO.conn.prepareStatement(listeningClassQuery);
            listeningClassStatement.setString(1, studentId);

            ResultSet listeningClassResultSet = listeningClassStatement.executeQuery();

            while (listeningClassResultSet.next()) {
                String courseId = listeningClassResultSet.getString("CID");

                // CID에 해당하는 강의 정보를 가져옵니다.
                String classQuery = "SELECT CID, classname, lectureid, classroom, classstarttime, classendtime, classdayoftheweek, grades " +
                        "FROM class WHERE CID = ?";
                PreparedStatement classStatement = DAO.conn.prepareStatement(classQuery);
                classStatement.setString(1, courseId);

                ResultSet classResultSet = classStatement.executeQuery();

                while (classResultSet.next()) {
                    String className = classResultSet.getString("classname");
                    String lectureId = classResultSet.getString("lectureid");
                    String classroom = classResultSet.getString("classroom");
                    String startTime = classResultSet.getString("classstarttime");
                    String endTime = classResultSet.getString("classendtime");
                    String dayOfWeek = classResultSet.getString("classdayoftheweek");
                    String grades = classResultSet.getString("grades");

                    // 결과 처리 부분: 가져온 강의 정보를 모델에 추가
                    Vector<String> rowData = new Vector<>();
                    rowData.add(className);
                    rowData.add(lectureId);
                    rowData.add(classroom);
                    rowData.add(dayOfWeek + " " + startTime + " - " + endTime);
                    rowData.add(grades);

                    tableModel.addRow(rowData);
                }

                classResultSet.close();
                classStatement.close();
            }

            listeningClassResultSet.close();
            listeningClassStatement.close();

            // 테이블을 스크롤 가능하도록 패널에 추가
            JScrollPane scrollPane = new JScrollPane(timeTable);
            add(scrollPane, BorderLayout.CENTER);

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: 에러 처리 로직을 추가할 수 있습니다.
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentTimeTable("123"); // 예시로 "123"이라는 SID를 사용
        });
    }
}

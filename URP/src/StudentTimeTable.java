import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

public class StudentTimeTable extends JFrame {

    private JTable timeTable;

    public StudentTimeTable() {
        super("시간표 조회 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // 페이지에 필요한 컴포넌트 초기화
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel studentIdLabel = new JLabel("학생 ID:");
        JTextField studentIdField = new JTextField();

        JButton searchButton = new JButton("시간표 조회");

        // 시간표 조회 버튼의 ActionListener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdField.getText();

                // TODO: 실제 데이터베이스에서 시간표 조회 로직을 구현하고, 결과를 테이블에 표시

                retrieveStudentTimeTable(studentId);
            }
        });

        // 테이블 초기화
        timeTable = new JTable();

        // 테이블을 담을 패널 초기화
        JScrollPane scrollPane = new JScrollPane(timeTable);

        // 패널에 컴포넌트 추가
        panel.add(studentIdLabel);
        panel.add(studentIdField);
        panel.add(new JLabel()); // 빈 라벨
        panel.add(new JLabel()); // 빈 라벨
        panel.add(new JLabel()); // 빈 라벨
        panel.add(searchButton);

        // 프레임에 패널과 테이블을 추가
        add(panel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }

    // MySQL을 사용하여 학생의 시간표 조회
    private void retrieveStudentTimeTable(String studentId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/urp", "root", "root");

            // listeningclass 테이블에서 학생이 수강한 강의의 CID 조회
            String listeningQuery = "SELECT CID FROM listeningclass WHERE SID = ?";
            PreparedStatement listeningPstmt = conn.prepareStatement(listeningQuery);
            listeningPstmt.setString(1, studentId);

            ResultSet listeningRs = listeningPstmt.executeQuery();

            // ResultSet의 데이터를 Vector<Vector<Object>>로 변환하여 JTable에 표시
            Vector<Vector<Object>> data = new Vector<>();
            while (listeningRs.next()) {
                String classId = listeningRs.getString("CID");

                // class 테이블에서 CID에 해당하는 강의 정보 조회
                String classQuery = "SELECT * FROM class WHERE CID = ?";
                PreparedStatement classPstmt = conn.prepareStatement(classQuery);
                classPstmt.setString(1, classId);

                ResultSet classRs = classPstmt.executeQuery();

                while (classRs.next()) {
                    Vector<Object> row = new Vector<>();
                    row.add(classRs.getString("classname"));
                    row.add(classRs.getString("classroom"));
                    row.add(classRs.getString("classstarttime"));
                    row.add(classRs.getString("classendtime"));
                    row.add(classRs.getString("classdayoftheweek"));
                    row.add(classRs.getString("grades"));
                    data.add(row);
                }

                classRs.close();
                classPstmt.close();
            }

            // 테이블 모델 생성 및 데이터 설정
            Vector<String> columnNames = new Vector<>();
            columnNames.add("강의명");
            columnNames.add("강의실");
            columnNames.add("시작 시간");
            columnNames.add("종료 시간");
            columnNames.add("강의 요일");
            columnNames.add("성적");

            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            timeTable.setModel(model);

            listeningRs.close();
            listeningPstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentTimeTable();
        });
    }
}

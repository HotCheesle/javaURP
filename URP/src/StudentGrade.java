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

public class StudentGrade extends JFrame {

	public StudentGrade(int currentUserId) {
		// TODO Auto-generated constructor stub
	}
/*
    private JTable gradeTable;

    public StudentGrade() {
        super("성적 조회 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLayout(new BorderLayout());

        // 페이지에 필요한 컴포넌트 초기화
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        JLabel studentIdLabel = new JLabel("학생 ID:");
        JTextField studentIdField = new JTextField();

        JButton searchButton = new JButton("성적 조회");

        // 성적 조회 버튼의 ActionListener
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String studentId = studentIdField.getText();

                // TODO: 실제 데이터베이스에서 성적 조회 로직을 구현하고, 결과를 테이블에 표시

                retrieveStudentGrades(studentId);
            }
        });

        // 테이블 초기화
        gradeTable = new JTable();

        // 테이블을 담을 패널 초기화
        JScrollPane scrollPane = new JScrollPane(gradeTable);

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

    // MySQL을 사용하여 학생의 성적 조회
    private void retrieveStudentGrades(String studentId) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/urp", "root", "root");

            String query = "SELECT * FROM listeningclass WHERE SID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, studentId);

            ResultSet rs = pstmt.executeQuery();

            // ResultSet의 데이터를 Vector<Vector<Object>>로 변환하여 JTable에 표시
            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> row = new Vector<>();
                row.add(rs.getString("SID"));
                row.add(rs.getString("CID"));
                row.add(rs.getString("score"));
                data.add(row);
            }

            // 테이블 모델 생성 및 데이터 설정
            Vector<String> columnNames = new Vector<>();
            columnNames.add("학생 ID");
            columnNames.add("강의 ID");
            columnNames.add("성적");

            DefaultTableModel model = new DefaultTableModel(data, columnNames);
            gradeTable.setModel(model);

            rs.close();
            pstmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentGrade();
        });
    }*/
}

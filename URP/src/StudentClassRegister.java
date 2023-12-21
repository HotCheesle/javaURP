import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class StudentClassRegister extends JFrame {

    private String studentId; // SID 저장 변수 추가
    private JList<String> classList;
    private JTextField cidField;

    public StudentClassRegister(String studentId) {
        super("수강 신청 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        this.studentId = studentId; // SID 설정

        // 수강 신청 페이지에 필요한 컴포넌트 및 로직을 추가합니다.
        displayClassList();

        setVisible(true);
    }

    private void displayClassList() {
        // 강의 목록을 담을 리스트 모델 생성
        DefaultListModel<String> listModel = new DefaultListModel<>();
        classList = new JList<>(listModel);

        try {
            DAO.SetConnection("urp", "root", "root");

            // 모든 강의 목록을 조회
            String classQuery = "SELECT CID, classname FROM class";
            PreparedStatement classStatement = DAO.conn.prepareStatement(classQuery);
            ResultSet classResultSet = classStatement.executeQuery();

            // 조회한 강의 목록을 리스트 모델에 추가
            while (classResultSet.next()) {
                String courseId = classResultSet.getString("CID");
                String className = classResultSet.getString("classname");
                listModel.addElement(courseId + " - " + className);
            }

            classResultSet.close();
            classStatement.close();

            // 리스트를 스크롤 가능하도록 패널에 추가
            JScrollPane scrollPane = new JScrollPane(classList);
            add(scrollPane, BorderLayout.CENTER);

            // 수강 신청 버튼과 강의 번호 입력 필드 추가
            cidField = new JTextField();
            JButton registerButton = new JButton("수강 신청");
            registerButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    registerClass();
                }
            });

            JPanel panel = new JPanel(new GridLayout(2, 2));
            panel.add(new JLabel("강의 번호:"));
            panel.add(cidField);
            panel.add(new JLabel()); // 빈 라벨
            panel.add(registerButton);

            add(panel, BorderLayout.SOUTH);

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: 에러 처리 로직을 추가할 수 있습니다.
        }
    }

    private void registerClass() {
        // 입력된 강의 번호 가져오기
        String selectedValue = classList.getSelectedValue();
        if (selectedValue == null) {
            JOptionPane.showMessageDialog(this, "수강할 강의를 선택해주세요.");
            return;
        }

        // 선택된 강의 번호에서 CID 추출
        String[] parts = selectedValue.split(" - ");
        String courseId = parts[0];

        try {
            // listeningclass 테이블에 수강 신청 정보 추가
            String registerQuery = "INSERT INTO listeningclass (SID, CID, score) VALUES (?, ?, 0)";
            PreparedStatement registerStatement = DAO.conn.prepareStatement(registerQuery);
            registerStatement.setString(1, studentId);
            registerStatement.setString(2, courseId);

            int rowsAffected = registerStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "수강 신청이 완료되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "수강 신청에 실패했습니다.");
            }

            registerStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: 에러 처리 로직을 추가할 수 있습니다.
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentClassRegister("123"); // 예시로 "123"이라는 SID를 사용
        });
    }
}

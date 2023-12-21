import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class StudentClassRegister extends JFrame {

    private JTextField sidField;
    private JTextField lectureIdField;

    public StudentClassRegister() {
        super("수강 신청 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // 페이지에 필요한 컴포넌트 초기화
        JLabel sidLabel = new JLabel("학생 ID:");
        sidField = new JTextField();

        JLabel lectureIdLabel = new JLabel("강의 ID:");
        lectureIdField = new JTextField();

        JButton registerButton = new JButton("수강 신청");

        // 수강 신청 버튼의 ActionListener
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerClass();
            }
        });

        // 패널 생성 및 컴포넌트 추가
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(sidLabel);
        panel.add(sidField);
        panel.add(lectureIdLabel);
        panel.add(lectureIdField);
        panel.add(new JLabel());
        panel.add(registerButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // MySQL을 사용하여 수강 신청 정보 추가
    private void registerClass() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/urp", "root", "root");

            // 사용자로부터 입력받은 학생 ID와 강의 ID
            String sid = sidField.getText();
            String lectureId = lectureIdField.getText();

            // 적절한 SQL 쿼리를 작성해야 합니다.
            String query = "INSERT INTO listeningclass (SID, CID, score) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, sid);
            pstmt.setString(2, lectureId);
            pstmt.setInt(3, 0); // 예시로 score는 0으로 초기화

            // 쿼리 실행
            pstmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "수강 신청이 완료되었습니다.");

            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentClassRegister();
        });
    }
}

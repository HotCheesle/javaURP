import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StudentChange extends JFrame {

    private JTextField nameField;
    private JTextField passwordField;
    private JTextField birthdateField;

    public StudentChange(int currentUserId) {
        super("정보 변경 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // 페이지에 필요한 컴포넌트 초기화
        nameField = new JTextField();
        passwordField = new JTextField();
        birthdateField = new JTextField();

        JButton updateButton = new JButton("정보 수정");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudentInfo(currentUserId);
            }
        });

        // 패널 생성 및 컴포넌트 추가
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("이름:"));
        panel.add(nameField);
        panel.add(new JLabel("비밀번호:"));
        panel.add(passwordField);
        panel.add(new JLabel("생년월일:"));
        panel.add(birthdateField);
        panel.add(new JLabel()); // 빈 라벨
        panel.add(updateButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // MySQL을 사용하여 학생 정보 업데이트
    private void updateStudentInfo(int currentUserId) {
        // 유효성 검사를 통해 데이터 형식 확인
        if (!isValidData()) {
            return;
        }

        try {
            DAO.SetConnection("urp", "root", "root");

            String updateQuery = "UPDATE students SET sname=?, pw=?, birthdate=? WHERE SID=?";
            PreparedStatement pstmt = DAO.conn.prepareStatement(updateQuery);

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, passwordField.getText());
            pstmt.setString(3, birthdateField.getText());
            pstmt.setInt(4, currentUserId);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "정보가 성공적으로 수정되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "정보 수정에 실패했습니다.");
            }

            pstmt.close();
            DAO.conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 유효성 검사를 통해 데이터 형식 확인
    private boolean isValidData() {
        String name = nameField.getText();
        String password = passwordField.getText();
        String birthdate = birthdateField.getText();

        if (name.isEmpty() || password.isEmpty() || birthdate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "빈 칸을 모두 채워주세요.");
            return false;
        }

        // 예시: 생년월일 형식 검사
        if (!birthdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "올바른 생년월일 형식이 아닙니다. (예: 2000-01-01)");
            return false;
        }

        // 여기에 추가적인 데이터 형식 검사를 추가할 수 있습니다.

        return true;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 여기에서는 예시로 "s123"이라는 SID로 StudentChange를 호출하고 있습니다.
            // 로그인 시 사용자의 SID를 전달받아야 합니다.
            new StudentChange(123);
        });
    }
}

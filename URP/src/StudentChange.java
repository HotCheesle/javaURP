import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class StudentChange extends JFrame {

    private JTextField nameField;
    private JTextField passwordField;
    private JTextField birthdateField;

    public StudentChange() {
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
                updateStudentInfo();
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
    private void updateStudentInfo() {
        // 유효성 검사를 통해 데이터 형식 확인
        if (!isValidData()) {
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/URP", "root", "root");

            String updateQuery = "UPDATE students SET sname=?, pw=?, birthdate=?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);

            // 현재 로그인한 학생의 학생 아이디는 알아야 합니다.
            // 여기에서는 임의로 's'로 시작하는 학생 아이디를 사용하였습니다. 실제 사용자의 아이디에 맞게 수정하세요.
            String studentId = "s123"; // 예시 학생 아이디

            pstmt.setString(1, nameField.getText());
            pstmt.setString(2, passwordField.getText());
            pstmt.setString(3, birthdateField.getText());

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "정보가 성공적으로 수정되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "정보 수정에 실패했습니다.");
            }

            pstmt.close();
            conn.close();
        } catch (Exception e) {
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
            new StudentChange();
        });
    }
}

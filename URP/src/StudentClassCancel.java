import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class StudentClassCancel extends JFrame {

    private JTextField sidField;
    private JTextField cidField;

    public StudentClassCancel() {
        super("수강 취소 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // 페이지에 필요한 컴포넌트 초기화
        JLabel sidLabel = new JLabel("학생 ID:");
        sidField = new JTextField();

        JLabel cidLabel = new JLabel("강의 ID:");
        cidField = new JTextField();

        JButton cancelButton = new JButton("수강 취소");

        // 수강 취소 버튼의 ActionListener
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelClass();
            }
        });

        // 패널 생성 및 컴포넌트 추가
        JPanel panel = new JPanel(new GridLayout(3, 2));
        panel.add(sidLabel);
        panel.add(sidField);
        panel.add(cidLabel);
        panel.add(cidField);
        panel.add(new JLabel());
        panel.add(cancelButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // MySQL을 사용하여 수강 취소 정보 삭제
    private void cancelClass() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/urp", "root", "root");

            // 사용자로부터 입력받은 학생 ID와 강의 ID
            String sid = sidField.getText();
            String cid = cidField.getText();

            // 적절한 SQL 쿼리를 작성해야 합니다.
            String query = "DELETE FROM listeningclass WHERE SID = ? AND CID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, sid);
            pstmt.setString(2, cid);

            // 쿼리 실행
            int result = pstmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "수강 취소가 완료되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "수강 취소 실패. 입력 정보를 확인하세요.");
            }

            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentClassCancel();
        });
    }
}

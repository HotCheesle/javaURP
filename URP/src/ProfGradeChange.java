import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfGradeChange extends JFrame {

    private JTextField sidField;
    private JTextField cidField;
    private JTextField scoreField;

    public ProfGradeChange() {
        super("성적 수정 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // 컴포넌트 초기화
        sidField = new JTextField();
        cidField = new JTextField();
        scoreField = new JTextField();

        JButton inputButton = new JButton("성적 수정");

        // 성적 수정 버튼의 ActionListener
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modifyGrade();
            }
        });

        // 패널 생성 및 컴포넌트 추가
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(new JLabel("학생 ID:"));
        panel.add(sidField);
        panel.add(new JLabel("강의 ID:"));
        panel.add(cidField);
        panel.add(new JLabel("성적:"));
        panel.add(scoreField);
        panel.add(new JLabel()); // 빈 라벨
        panel.add(inputButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // 성적 수정 메서드
    private void modifyGrade() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/urp", "root", "root");

            String sid = sidField.getText();
            String cid = cidField.getText();
            String newScore = scoreField.getText();

            // 기존 성적이 존재하는지 확인
            if (isExistingGrade(sid, cid)) {
                // 성적이 존재하면 수정
                String query = "UPDATE listeningclass SET score = ? WHERE SID = ? AND CID = ?";
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setString(1, newScore);
                pstmt.setString(2, sid);
                pstmt.setString(3, cid);

                pstmt.executeUpdate();
                pstmt.close();

                JOptionPane.showMessageDialog(this, "성적이 수정되었습니다.");
            } else {
                // 성적이 존재하지 않으면 메시지 출력
                JOptionPane.showMessageDialog(this, "해당 학생과 강의에 대한 성적이 존재하지 않습니다.");
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // 기존 성적이 존재하는지 확인하는 메서드
    private boolean isExistingGrade(String sid, String cid) {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/urp", "root", "root");

            String query = "SELECT * FROM listeningclass WHERE SID = ? AND CID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, sid);
            pstmt.setString(2, cid);

            ResultSet rs = pstmt.executeQuery();

            // 결과가 있으면 성적이 이미 존재함
            boolean isExisting = rs.next();

            rs.close();
            pstmt.close();
            conn.close();

            return isExisting;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProfGradeChange();
        });
    }
}

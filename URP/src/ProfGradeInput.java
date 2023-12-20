import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class ProfGradeInput extends JFrame { //수강 신청할 시, listeningclass에 자동으로 추가가 되고나서 그 항목의 grade만 입력하는 게 좋을 듯

    private JTextField sidField;
    private JTextField cidField;
    private JTextField scoreField;

    public ProfGradeInput() {
        super("성적 입력 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        // 페이지에 필요한 컴포넌트 초기화
        JLabel sidLabel = new JLabel("학생 ID:");
        sidField = new JTextField();

        JLabel cidLabel = new JLabel("강의 ID:");
        cidField = new JTextField();

        JLabel scoreLabel = new JLabel("성적:");
        scoreField = new JTextField();

        JButton inputButton = new JButton("성적 입력");

        // 성적 입력 버튼의 ActionListener
        inputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputGrade();
            }
        });

        // 패널 생성 및 컴포넌트 추가
        JPanel panel = new JPanel(new GridLayout(4, 2));
        panel.add(sidLabel);
        panel.add(sidField);
        panel.add(cidLabel);
        panel.add(cidField);
        panel.add(scoreLabel);
        panel.add(scoreField);
        panel.add(new JLabel());
        panel.add(inputButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    // MySQL을 사용하여 성적 정보 입력
    private void inputGrade() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/urp", "root", "root");

            // 사용자로부터 입력받은 학생 ID, 강의 ID, 성적
            String sid = sidField.getText();
            String cid = cidField.getText();
            String score = scoreField.getText();

            // 적절한 SQL 쿼리를 작성해야 합니다.
            String query = "INSERT INTO listeningclass (SID, CID, score) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, sid);
            pstmt.setString(2, cid);
            pstmt.setString(3, score);

            // 쿼리 실행
            int result = pstmt.executeUpdate();

            if (result > 0) {
                JOptionPane.showMessageDialog(this, "성적 입력이 완료되었습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "성적 입력 실패. 입력 정보를 확인하세요.");
            }

            pstmt.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ProfGradeInput();
        });
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class StudentClassCancel extends JFrame {

    private String studentId; // SID 저장 변수 추가
    private JList<String> classList;

    public StudentClassCancel(String studentId) {
        super("수강 취소 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        this.studentId = studentId; // SID 설정

        // 수강 취소 페이지에 필요한 컴포넌트 및 로직을 추가합니다.
        displayClassList();

        setVisible(true);
    }

    public StudentClassCancel(int currentUserId) {
		// TODO Auto-generated constructor stub
	}

	private void displayClassList() {
        // 강의 목록을 담을 리스트 모델 생성
        DefaultListModel<String> listModel = new DefaultListModel<>();
        classList = new JList<>(listModel);

        try {
            DAO.SetConnection("urp", "root", "root");

            // 수강 중인 강의 목록을 조회
            String classQuery = "SELECT class.CID, class.classname FROM class JOIN listeningclass ON class.CID = listeningclass.CID WHERE listeningclass.SID = ?";
            PreparedStatement classStatement = DAO.conn.prepareStatement(classQuery);
            classStatement.setString(1, studentId);
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

            // 수강 취소 버튼 추가
            JButton cancelButton = new JButton("수강 취소");
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cancelClass();
                }
            });

            JPanel panel = new JPanel(new GridLayout(1, 1));
            panel.add(cancelButton);

            add(panel, BorderLayout.SOUTH);

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: 에러 처리 로직을 추가할 수 있습니다.
        }
    }

    private void cancelClass() {
        // 입력된 강의 번호 가져오기
        String selectedValue = classList.getSelectedValue();
        if (selectedValue == null) {
            JOptionPane.showMessageDialog(this, "취소할 강의를 선택해주세요.");
            return;
        }

        // 선택된 강의 번호에서 CID 추출
        String[] parts = selectedValue.split(" - ");
        String courseId = parts[0];

        try {
            // listeningclass 테이블에서 수강 취소 정보 삭제
            String cancelQuery = "DELETE FROM listeningclass WHERE SID = ? AND CID = ?";
            PreparedStatement cancelStatement = DAO.conn.prepareStatement(cancelQuery);
            cancelStatement.setString(1, studentId);
            cancelStatement.setString(2, courseId);

            int rowsAffected = cancelStatement.executeUpdate();
            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(this, "수강 취소가 완료되었습니다.");
                // 새로고침
                refreshClassList();
            } else {
                JOptionPane.showMessageDialog(this, "수강 취소에 실패했습니다.");
            }

            cancelStatement.close();

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: 에러 처리 로직을 추가할 수 있습니다.
        }
    }

    private void refreshClassList() {
        // 기존 리스트 모델 초기화
        DefaultListModel<String> newListModel = new DefaultListModel<>();

        try {
            // 새로운 강의 목록 조회
            String classQuery = "SELECT class.CID, class.classname FROM class JOIN listeningclass ON class.CID = listeningclass.CID WHERE listeningclass.SID = ?";
            PreparedStatement classStatement = DAO.conn.prepareStatement(classQuery);
            classStatement.setString(1, studentId);
            ResultSet classResultSet = classStatement.executeQuery();

            // 조회한 강의 목록을 리스트 모델에 추가
            while (classResultSet.next()) {
                String courseId = classResultSet.getString("CID");
                String className = classResultSet.getString("classname");
                newListModel.addElement(courseId + " - " + className);
            }

            classResultSet.close();
            classStatement.close();

            // 새로운 리스트 모델로 업데이트
            classList.setModel(newListModel);

        } catch (SQLException e) {
            e.printStackTrace();
            // TODO: 에러 처리 로직을 추가할 수 있습니다.
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentClassCancel("123"); // 예시로 "123"이라는 SID를 사용
        });
    }
}

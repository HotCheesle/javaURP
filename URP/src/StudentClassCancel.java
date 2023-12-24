import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentClassCancel {

    private static int currentUserId;

    public StudentClassCancel(int studentId) {
        currentUserId = studentId;
        initialize();
    }

    private static void initialize() {
        JFrame cancelFrame = new JFrame("수강 취소");
        cancelFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        cancelFrame.setSize(400, 300);
        cancelFrame.setLayout(new BorderLayout());

        // 현재 수강 중인 과목 리스트를 가져옴
        List<String> currentClasses = getCurrentClasses();

        // 리스트를 보여주기 위한 JList
        JList<String> classList = new JList<>(currentClasses.toArray(new String[0]));
        JScrollPane scrollPane = new JScrollPane(classList);

        JButton cancelButton = new JButton("수강 취소");

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 선택된 과목 코드를 가져옴
                String selectedClass = classList.getSelectedValue();
                if (selectedClass != null) {
                    String[] parts = selectedClass.split(" - ");
                    if (parts.length == 2) {
                        int classId = Integer.parseInt(parts[0]);
                        String result = DAO.CancelClass(currentUserId, classId);
                        JOptionPane.showMessageDialog(cancelFrame, result);
                        // 취소 후 리스트 갱신
                        currentClasses.clear();
                        currentClasses.addAll(getCurrentClasses());
                        classList.setListData(currentClasses.toArray(new String[0]));
                    }
                } else {
                    JOptionPane.showMessageDialog(cancelFrame, "과목을 선택하세요.");
                }
            }
        });

        cancelFrame.add(scrollPane, BorderLayout.CENTER);
        cancelFrame.add(cancelButton, BorderLayout.SOUTH);

        cancelFrame.setVisible(true);
    }

    private static List<String> getCurrentClasses() {
        List<String> currentClasses = new ArrayList<>();

        try {
            ResultSet rs = DAO.GetStudentCurrentClasses(currentUserId);
            while (rs.next()) {
                int classId = rs.getInt("CID");
                String className = rs.getString("classname");
                String classInfo = classId + " - " + className;
                currentClasses.add(classInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return currentClasses;
    }
}

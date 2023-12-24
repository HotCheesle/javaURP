import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StudentClassRegister {

    private static int currentUserId;

    public StudentClassRegister(int studentId) {
        currentUserId = studentId;
        createClassRegisterPage();
    }

    private static void createClassRegisterPage() {
        JFrame registerFrame = new JFrame("수강 신청");
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        registerFrame.setSize(500, 300);
        registerFrame.setLayout(new BorderLayout());

        JPanel classListPanel = createClassListPanel();
        JPanel registerPanel = createRegisterPanel();

        registerFrame.add(classListPanel, BorderLayout.CENTER);
        registerFrame.add(registerPanel, BorderLayout.SOUTH);

        registerFrame.setVisible(true);
    }

    private static JPanel createClassListPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        List<String> classList = getClassList();

        JList<String> list = new JList<>(classList.toArray(new String[0]));

        JScrollPane scrollPane = new JScrollPane(list);

        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private static List<String> getClassList() {
        List<String> classList = new ArrayList<>();

        try {
            ResultSet rs = DAO.GetClassList();
            while (rs.next()) {
                int cid = rs.getInt("CID");
                String className = rs.getString("classname");
                String classInfo = "CID: " + cid + " - " + className;
                classList.add(classInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return classList;
    }


    private static JPanel createRegisterPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JLabel cidLabel = new JLabel("수강할 과목의 CID를 입력하세요:");
        JTextField cidField = new JTextField(10);

        JButton registerButton = new JButton("수강 신청");
        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int cid = Integer.parseInt(cidField.getText());
                    DAO.RegisterClass(currentUserId, cid);
                    JOptionPane.showMessageDialog(null, "수강 신청이 완료되었습니다.");
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null, "올바른 숫자를 입력하세요.");
                }
            }
        });

        panel.add(cidLabel);
        panel.add(cidField);
        panel.add(registerButton);

        return panel;
    }
}

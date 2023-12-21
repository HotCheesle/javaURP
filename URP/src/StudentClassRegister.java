import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class StudentClassRegister extends JFrame {

	public StudentClassRegister(int currentUserId) {
		// TODO Auto-generated constructor stub
	}
/*
    private String studentId;
    private JList<String> classList;
    private JTextField cidField;

    public StudentClassRegister(String studentId) {
        super("수강 신청 페이지");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLayout(new BorderLayout());

        this.studentId = studentId;
        displayClassList();

        setVisible(true);
    }

    private void displayClassList() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        classList = new JList<>(listModel);

        try {
            ResultSet resultSet = DAO.getClassListAndRegisterCourse(studentId, "");

            while (resultSet.next()) {
                String courseId = resultSet.getString("CID");
                String className = resultSet.getString("classname");
                listModel.addElement(courseId + " - " + className);
            }

            resultSet.close();

            JScrollPane scrollPane = new JScrollPane(classList);
            add(scrollPane, BorderLayout.CENTER);

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
            panel.add(new JLabel());
            panel.add(registerButton);

            add(panel, BorderLayout.SOUTH);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerClass() {
        String selectedValue = classList.getSelectedValue();
        if (selectedValue == null) {
            JOptionPane.showMessageDialog(this, "수강할 강의를 선택해주세요.");
            return;
        }

        String[] parts = selectedValue.split(" - ");
        String courseId = parts[0];

        ResultSet resultSet = DAO.getClassListAndRegisterCourse(studentId, courseId);

        // 결과 처리 (생략 가능)

        JOptionPane.showMessageDialog(this, "수강 신청이 완료되었습니다.");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new StudentClassRegister("123");
        });
    }*/
}

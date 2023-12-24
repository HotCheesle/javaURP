import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ProfGradeChange {
    private JFrame frame;
    private JComboBox<Integer> classComboBox;
    private JTable studentTable;
    private DefaultTableModel tableModel;
    private JTextField scoreField;
    private JButton updateButton;

    private int profId;

    public ProfGradeChange(int profId) {
        this.profId = profId;
        initialize();
    }

    private void initialize() {
        frame = new JFrame("성적 입력/수정");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(500, 400);
        frame.setLayout(new BorderLayout());

        // 강의 선택 콤보 박스
        classComboBox = new JComboBox<>();
        updateClassComboBox();

        // 학생 성적 표
        tableModel = new DefaultTableModel();
        studentTable = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(studentTable);

        // 성적 입력 필드 및 버튼
        JPanel scorePanel = new JPanel();
        JLabel scoreLabel = new JLabel("성적:");
        scoreField = new JTextField(5);
        updateButton = new JButton("성적 업데이트");

        // 성적 업데이트 버튼 리스너
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateStudentGrade();
            }
        });

        scorePanel.add(scoreLabel);
        scorePanel.add(scoreField);
        scorePanel.add(updateButton);

        // 프레임에 컴포넌트 추가
        frame.add(classComboBox, BorderLayout.NORTH);
        frame.add(tableScrollPane, BorderLayout.CENTER);
        frame.add(scorePanel, BorderLayout.SOUTH);

        // 프레임 표시
        frame.setVisible(true);
    }

    // 강의 선택 콤보 박스 업데이트
    private void updateClassComboBox() {
        classComboBox.removeAllItems();
        ResultSet classList = DAO.GetProfessorClass(profId);
        try {
            while (classList.next()) {
                int classId = classList.getInt("CID");
                classComboBox.addItem(classId);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // 콤보 박스 아이템 선택 리스너
        classComboBox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedClassId = (int) classComboBox.getSelectedItem();
                updateStudentTable(selectedClassId);
            }
        });
    }

    // 학생 성적 표 업데이트
    private void updateStudentTable(int classId) {
        tableModel.setColumnCount(0);
        tableModel.setRowCount(0);

        ResultSet studentList = DAO.GetClassStudents(classId);
        try {
            int columnCount = studentList.getMetaData().getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                tableModel.addColumn(studentList.getMetaData().getColumnName(i));
            }

            while (studentList.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = studentList.getObject(i);
                }
                tableModel.addRow(rowData);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 성적 업데이트 메서드
    private void updateStudentGrade() {
        int classId = (int) classComboBox.getSelectedItem();
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow != -1) {
            int studentId = (int) tableModel.getValueAt(selectedRow, 0);
            try {
                float newScore = Integer.parseInt(scoreField.getText());
                DAO.UpdateStudentGrade(profId, classId, studentId, newScore);
                JOptionPane.showMessageDialog(frame, "성적이 업데이트되었습니다.");
                updateStudentTable(classId);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(frame, "유효한 성적을 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(frame, "학생을 선택하세요.", "경고", JOptionPane.WARNING_MESSAGE);
        }
    }
}

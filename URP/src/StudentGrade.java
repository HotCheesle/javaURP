import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.Vector;

public class StudentGrade {

    private static int currentUserId;

    public StudentGrade(int studentId) {
        currentUserId = studentId;
        initialize();
    }

    private static void initialize() {
        JFrame gradeFrame = new JFrame("성적 조회");
        gradeFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gradeFrame.setSize(400, 300);
        gradeFrame.setLayout(new BorderLayout());

        ResultSet gradeResultSet = DAO.GetStudentGrades(currentUserId);

        JTable gradeTable = new JTable(buildTableModel(gradeResultSet));
        JScrollPane scrollPane = new JScrollPane(gradeTable);

        gradeFrame.add(scrollPane, BorderLayout.CENTER);

        gradeFrame.setVisible(true);
    }

    private static DefaultTableModel buildTableModel(ResultSet rs) {
        try {
            ResultSetMetaData metaData = rs.getMetaData();

            Vector<String> columnNames = new Vector<>();
            int columnCount = metaData.getColumnCount();
            for (int column = 1; column <= columnCount; column++) {
                columnNames.add(metaData.getColumnName(column));
            }

            Vector<Vector<Object>> data = new Vector<>();
            while (rs.next()) {
                Vector<Object> vector = new Vector<>();
                for (int columnIndex = 1; columnIndex <= columnCount; columnIndex++) {
                    vector.add(rs.getObject(columnIndex));
                }
                data.add(vector);
            }

            return new DefaultTableModel(data, columnNames);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;
import java.util.Vector;

public class StudentTimeTable extends JFrame {

    private JTable timetableTable;

    public StudentTimeTable(int studentId) {
        setTitle("학생 시간표");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);

        ResultSet timetableData = DAO.GetStudentTimeTable(studentId);

        Vector<Vector<String>> dataVector = new Vector<>();
        Vector<String> columnNames = new Vector<>();
        columnNames.add("과목명");
        columnNames.add("강의실");
        columnNames.add("시간");
        columnNames.add("요일");

        try {
            while (timetableData.next()) {
                Vector<String> row = new Vector<>();
                row.add(timetableData.getString("classname"));
                row.add(timetableData.getString("classroom"));
                row.add(timetableData.getString("classstarttime") + " - " + timetableData.getString("classendtime"));
                row.add(timetableData.getString("classdayoftheweek"));
                dataVector.add(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        DefaultTableModel tableModel = new DefaultTableModel(dataVector, columnNames);
        timetableTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(timetableTable);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        setVisible(true);
    }
}

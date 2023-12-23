import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.ResultSet;

public class ProfClassEdit extends JFrame {
	
    private JTextField cidField;

    public ProfClassEdit(int currentUserId) {
        super("수업 삭제 페이지");
        JFrame frm = this;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLayout(new BorderLayout());

        String colNames[] = { "강의코드", "강의명", "강의실", "수업시작시간", "수업종료시간", "수업요일", "학점"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0);
        JTable table = new JTable(model);
        
        refreshList(model, currentUserId);
        
        
        setVisible(true);
    }
    
    private void refreshList(DefaultTableModel model, int pid) {
    	try
		{
			model.getDataVector().removeAllElements();
			ResultSet rsc = DAO.GetProfessorClass(pid);
			String[] row = new String[7];
			while (rsc.next()) {
				row[0] = rsc.getString("CID");
				row[1] = rsc.getString("classname");
				row[2] = rsc.getString("classroom");
				row[3] = rsc.getString("classstarttime");
				row[4] = rsc.getString("classendtime");
				row[5] = rsc.getString("classdayoftheweek");
				row[6] = rsc.getString("grades");
				model.addRow(row);
			}
		} catch(Exception ex) {}
    }
    /*
     * 내일 작업할거 gpt질문내용 참고해서 테이블에서 행을 클릭하면 해당 정보가 텍스트박스에 표시가 되고 그것을 수정하고 수정버튼을
     * 누르면 수정이되고(물론 기준은 만족해야함 ClassMake참고) 삭제버튼을 누르면 가능하면 확인메세지를 띄운뒤에 삭제가 되도록 하자
     * 
     */
    
}

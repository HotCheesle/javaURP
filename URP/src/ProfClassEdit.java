import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class ProfClassEdit extends JFrame {
	
	private JLabel CIDLabel;
	private JTextField nameField;
    private JTextField roomField;
    JComboBox<String> startComboBox;
    JComboBox<String> endComboBox;
    JComboBox<String> dayComboBox;
    private JTextField gradesField;
    String[] starttime = {"09:00:00", "11:00:00", "13:00:00", "15:00:00", "17:00:00", "19:00:00"};
    String[] endtime = {"10:45:00", "12:45:00", "14:45:00", "16:45:00", "18:45:00", "20:45:00"};
    String[] dayoftheweek = {"월요일", "화요일", "수요일", "목요일", "금요일"};

    public ProfClassEdit(int currentUserId) {
        super("수업 수정 페이지");
        JFrame frm = this;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 400);
        setLayout(new BorderLayout());
        
        CIDLabel = new JLabel();
        nameField = new JTextField();
        roomField = new JTextField();
        startComboBox = new JComboBox<>(starttime);
        endComboBox = new JComboBox<>(endtime);
        dayComboBox = new JComboBox<>(dayoftheweek);
        gradesField = new JTextField();

        String colNames[] = { "강의코드", "강의명", "강의실", "수업시작시간", "수업종료시간", "수업요일", "학점"};
        DefaultTableModel model = new DefaultTableModel(colNames, 0);
        JTable table = new JTable(model);
        
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					int selectrow = table.getSelectedRow();
					if (selectrow != -1) {
						CIDLabel.setText((String)table.getValueAt(selectrow, 0));
						nameField.setText((String)table.getValueAt(selectrow, 1));
						roomField.setText((String)table.getValueAt(selectrow, 2));
						startComboBox.setSelectedItem((String)table.getValueAt(selectrow, 3));
						endComboBox.setSelectedItem((String)table.getValueAt(selectrow, 4));
						dayComboBox.setSelectedItem((String)table.getValueAt(selectrow, 5));
						gradesField.setText((String)table.getValueAt(selectrow, 6));
					}
				}
			}
		});
        
        refreshList(model, currentUserId);
        JButton editButton = new JButton("수업 수정");
        JButton deleteButton = new JButton("수업 삭제");
        
        editButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isValidData()) {
					if(isDupleTime(currentUserId)) {
						String start = (String) startComboBox.getSelectedItem();
						String end = (String) endComboBox.getSelectedItem();
						String day = (String) dayComboBox.getSelectedItem();
						int cid;
						try {cid = Integer.parseInt(CIDLabel.getText());} catch(Exception ex) {cid = 0;}
						DAO.UpdateClass(cid, nameField.getText(), roomField.getText(), 
								start, end, day, gradesField.getText());
						JOptionPane.showMessageDialog(frm, "수업이 수정되었습니다.");
						refreshList(model, currentUserId);
					} else {JOptionPane.showMessageDialog(frm, "수업시간이 중복되었습니다.");}
				} else {JOptionPane.showMessageDialog(frm, "빈 칸을 모두 채워야합니다.");}
			}
		});
        
        deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				int result = JOptionPane.showConfirmDialog(frm, "정말 삭제하시겠습니까?", "삭제확인", 
						JOptionPane.YES_NO_OPTION);
				switch(result) {
				case JOptionPane.YES_OPTION:
					int cid;
					try {cid = Integer.parseInt(CIDLabel.getText());} catch(Exception ex) {cid = 0;}
					DAO.DeleteClass(cid);
					JOptionPane.showMessageDialog(frm, "수업이 삭제되었습니다.");
					refreshList(model, currentUserId);
					break;
				case JOptionPane.NO_OPTION: 
				case JOptionPane.CLOSED_OPTION:
				}
			}
		});
        
        add(new JScrollPane(table), BorderLayout.CENTER);
        JPanel panel = new JPanel(new GridLayout(4, 4));
        panel.add(new JLabel("강의코드:"));
        panel.add(CIDLabel);
        panel.add(new JLabel("강의명:"));
        panel.add(nameField);
        panel.add(new JLabel("강의실:"));
        panel.add(roomField);
        panel.add(new JLabel("수업시작시간:"));
        panel.add(startComboBox);
        panel.add(new JLabel("수업종료시간:"));
        panel.add(endComboBox);
        panel.add(new JLabel("수업요일:"));
        panel.add(dayComboBox);
        panel.add(new JLabel("학점:"));
        panel.add(gradesField);
        panel.add(editButton);
        panel.add(deleteButton);
        add(panel, BorderLayout.SOUTH);
        
        setVisible(true);
    }
    
    private void refreshList(DefaultTableModel model, int pid) {
    	try
		{
			model.setRowCount(0);
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
    private boolean isValidData() {
        String name = nameField.getText();
        String room = roomField.getText();
        String grades = gradesField.getText();

        if (name.isEmpty() || room.isEmpty() || grades.isEmpty()) {
            return false;
        }
        try {
        	Integer.parseInt(grades); // 숫자가 아닐경우 flase반환
        } catch (Exception ex) {
        	JOptionPane.showMessageDialog(this, "학점은 숫자로 입력해야합니다.");
        	return false;
        }
        return true;
    }
    private boolean isDupleTime(int pid) {
    	String start = (String) startComboBox.getSelectedItem();
    	String st = start.substring(0, 2);
    	String end = (String) endComboBox.getSelectedItem();
    	String ed = end.substring(0, 2);
    	String day = (String) dayComboBox.getSelectedItem();
    	ResultSet rst = DAO.GetClassTime(pid);
    	String cstart, cend, cday, cst, ced;
    	int ist = 0, ied = 0, cist = 0, cied = 0;
    	try {
    		ist = Integer.parseInt(st);
    		ied = Integer.parseInt(ed);
    	}catch(Exception ex) {}
    	System.out.println(ist);
    	System.out.println(ied);
    	if (ist > ied) {
    		JOptionPane.showMessageDialog(this, "수업시작시간이 수업종료시간 이전이어야 합니다.");
    		return false;
    	}
    	
    	try {
    		while (rst.next()) {
    			cstart = rst.getString("classstarttime");
    			cst = cstart.substring(0, 2);
    			cend = rst.getString("classendtime");
    			ced = cend.substring(0, 2);
    			cday = rst.getString("classdayoftheweek");
    			try {
    	    		cist = Integer.parseInt(cst);
    	    		cied = Integer.parseInt(ced);
    	    	}catch(Exception ex) {}
    			if (day.equals(cday)) {
    				if (cist < ied && cied > ist) {
    					return false;
    				}
    			}
    		}
    		return true;
    	}catch (Exception ex) {return false;}
    }
    
}

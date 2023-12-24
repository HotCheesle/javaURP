import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class ProfClassMake extends JFrame {
	
	private JTextField nameField;
    private JTextField classroomField;
    private JTextField gradesField;
    JComboBox<String> starttimeComboBox;
    JComboBox<String> endtimeComboBox;
    JComboBox<String> dayoftheweekComboBox;
    String[] starttime = {"09:00:00", "11:00:00", "13:00:00", "15:00:00", "17:00:00", "19:00:00"};
    String[] endtime = {"10:45:00", "12:45:00", "14:45:00", "16:45:00", "18:45:00", "20:45:00"};
    String[] dayoftheweek = {"월요일", "화요일", "수요일", "목요일", "금요일"};

    public ProfClassMake(int currentUserId) {
        super("수업 생성 페이지");
        JFrame frm = this;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 500);
        setLayout(new BorderLayout());
        nameField = new JTextField();
        classroomField = new JTextField();
        gradesField = new JTextField();
        starttimeComboBox = new JComboBox<>(starttime);
        endtimeComboBox = new JComboBox<>(endtime);
        dayoftheweekComboBox = new JComboBox<>(dayoftheweek);
        
        JButton createButton = new JButton("수업 생성");
        
        createButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(isValidData()) {
					if(isDupleTime(currentUserId)) {
						String start = (String) starttimeComboBox.getSelectedItem();
						String end = (String) endtimeComboBox.getSelectedItem();
						String day = (String) dayoftheweekComboBox.getSelectedItem();
						DAO.CreateClass(nameField.getText(), currentUserId, classroomField.getText(), 
								start, end, day, gradesField.getText());
						JOptionPane.showMessageDialog(frm, "수업이 생성되었습니다.");
						frm.dispose();
					} else {JOptionPane.showMessageDialog(frm, "수업시간이 중복되었습니다.");}
				} else {JOptionPane.showMessageDialog(frm, "빈 칸을 모두 채워야합니다.");}
			}
		});
        
        JPanel panel = new JPanel(new GridLayout(7, 2));
        panel.add(new JLabel("강의명:"));
        panel.add(nameField);
        panel.add(new JLabel("강의실:"));
        panel.add(classroomField);
        panel.add(new JLabel("수업시작시간:"));
        panel.add(starttimeComboBox);
        panel.add(new JLabel("수업종료시간:"));
        panel.add(endtimeComboBox);
        panel.add(new JLabel("수업요일"));
        panel.add(dayoftheweekComboBox);
        panel.add(new JLabel("학점:"));
        panel.add(gradesField);
        panel.add(new JLabel());
        panel.add(createButton);
        
        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }
    
    private boolean isValidData() {
        String name = nameField.getText();
        String room = classroomField.getText();
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
    	String start = (String) starttimeComboBox.getSelectedItem();
    	String st = start.substring(0, 2);
    	String end = (String) endtimeComboBox.getSelectedItem();
    	String ed = end.substring(0, 2);
    	String day = (String) dayoftheweekComboBox.getSelectedItem();
    	ResultSet rst = DAO.GetClassTime(pid);
    	String cstart, cend, cday, cst, ced;
    	int ist = 0, ied = 0, cist = 0, cied = 0;
    	try {
    		ist = Integer.parseInt(st);
    		ied = Integer.parseInt(ed);
    	}catch(Exception ex) {}
    	
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

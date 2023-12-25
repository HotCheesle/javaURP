import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class StudentChange extends JFrame {

    private JTextField nameField;
    private JTextField passwordField;
    private JTextField birthdateField;
    private JTextField idField;

    public StudentChange(int currentUserId) {
        super("정보 변경 페이지");
        JFrame frm = this;
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 400);
        setLayout(new BorderLayout());
        nameField = new JTextField();
        idField = new JTextField();
        passwordField = new JTextField();
        birthdateField = new JTextField();
        
        JButton updateButton = new JButton("정보 수정");
        
        try {
        	ResultSet rss = DAO.GetStudent(currentUserId);
        	if (rss.next()) {
        		nameField.setText(rss.getString("sname"));
        		idField.setText(rss.getString("id"));
        		passwordField.setText(rss.getString("pw"));
        		birthdateField.setText(rss.getString("birthdate"));
        	}
        } catch(Exception ex) {}
        
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	if (!isValidData()) {
            		
                }
            	else {
            		DAO.ChangeStudent(currentUserId, nameField.getText(), idField.getText(),
            				passwordField.getText(), birthdateField.getText());
            		JOptionPane.showMessageDialog(frm, "수정이 완료되었습니다.");
            		frm.dispose();
            	}
            }
        });

        JPanel panel = new JPanel(new GridLayout(5, 2));
        panel.add(new JLabel("이름:"));
        panel.add(nameField);
        panel.add(new JLabel("아이디:"));
        panel.add(idField);
        panel.add(new JLabel("비밀번호:"));
        panel.add(passwordField);
        panel.add(new JLabel("생년월일:"));
        panel.add(birthdateField);
        panel.add(new JLabel());
        panel.add(updateButton);

        add(panel, BorderLayout.CENTER);

        setVisible(true);
    }

    private boolean isValidData() {
        String name = nameField.getText();
        String password = passwordField.getText();
        String id = idField.getText();
        String birthdate = birthdateField.getText();

        if (name.isEmpty() || password.isEmpty() || id.isEmpty() || birthdate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "빈 칸을 모두 채워야합니다.");
            return false;
        }
        if (!birthdate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            JOptionPane.showMessageDialog(this, "생년월일은 YYYY-MM-DD 형식이어야 합니다.");
            return false;
        }

        return true;
    }
    
}

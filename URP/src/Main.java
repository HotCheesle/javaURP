import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

public class Main {

    private static int currentUserId;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginPage();
        });
        
        try {
    		DAO.SetConnection("urp", "root", "root");//여기에 비밀번호와 데이터베이스 이름 입력
    	}
    	catch(Exception e){}
    }

    private static void LoginPage() {
        JFrame loginFrame = new JFrame("URP 로그인");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 200);
        loginFrame.setLayout(new GridLayout(4, 2));

        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();

        JLabel pwLabel = new JLabel("Password:");
        JPasswordField pwField = new JPasswordField();

        JButton loginButton = new JButton("로그인");
        JButton signUpButton = new JButton("회원가입");

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                char[] pwChars = pwField.getPassword();
                String password = new String(pwChars);
                String[] login = {"'" + id + "'", "'" + password + "'"};

                ResultSet rss = DAO.StudentLogin(login);
                ResultSet rsp = DAO.ProfessorLogin(login);
                try {
                    if (rss.next()) {
                        currentUserId = rss.getInt("SID");
                        StudentMain();
                        loginFrame.dispose();
                    } else if (rsp.next()) {
                        currentUserId = rsp.getInt("PID");
                        ProfessorMain();
                        loginFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(loginFrame, "잘못된 ID 또는 Password입니다.");
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	SignUpPage();
            }
        });

        loginFrame.add(idLabel);
        loginFrame.add(idField);
        loginFrame.add(pwLabel);
        loginFrame.add(pwField);
        loginFrame.add(loginButton);
        loginFrame.add(signUpButton);
        loginFrame.add(new JLabel()); 
        loginFrame.add(new JLabel()); 

        loginFrame.setVisible(true);
    }
    
    private static void SignUpPage() {
        JFrame signUpFrame = new JFrame("회원가입");
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setSize(400, 300);
        signUpFrame.setLayout(new GridLayout(6, 2));

        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();

        JLabel pwLabel = new JLabel("Password:");
        JPasswordField pwField = new JPasswordField();

        JLabel nameLabel = new JLabel("이름:");
        JTextField nameField = new JTextField();

        JLabel departmentLabel = new JLabel("학과:");
        String[] departments = {"기계공학과", "컴퓨터공학과", "물리학과", "수학과"};
        JComboBox<String> departmentComboBox = new JComboBox<>(departments);

        JLabel birthdayLabel = new JLabel("생일: ('2000-08-05'형식으로 기입)");
        JTextField birthdayField = new JTextField();

        JButton signUpButton = new JButton("회원가입");


        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                char[] pwChars = pwField.getPassword();
                String password = new String(pwChars);
                String name = nameField.getText();
                String selectedDepartment = (String) departmentComboBox.getSelectedItem();
                String birthday = birthdayField.getText();
                String[] dpt = {"'" + selectedDepartment + "'"};
                int dpid = 0, adid = 0;
                if (!isValidBirthdayFormat(birthday)) {
                    JOptionPane.showMessageDialog(signUpFrame, "생일은 'YYYY-MM-DD' 형식으로 입력해주세요.");
                    return;
                }
                try {
                	String[] eid = {"'" + id + "'"};
                	ResultSet rsdupleid = DAO.IDduplication(eid);
                	if(rsdupleid.next()) {
                		JOptionPane.showMessageDialog(signUpFrame, "아이디가 중복되었습니다. 다른아이디를 입력헤주세요.");
                	} else {
                		try {
                			ResultSet rsdpid = DAO.GetDepartmentID(dpt);
                			if (rsdpid.next()) {
                				dpid = rsdpid.getInt("DPID");
                			}
                			else {
                				JOptionPane.showMessageDialog(signUpFrame, "잘못된 학과명입니다.");
                			}
                		}catch(Exception ex) {}
                		try {
                			ResultSet rsadPID = DAO.GetAdvisorID(dpid);
                			if(rsadPID.next()) {
                				adid = rsadPID.getInt("PID");
                			}
                			else {
                				JOptionPane.showMessageDialog(signUpFrame, "해당 학과의 교수를 찾을수 없습니다.");
                			}
                		}catch (Exception ex) {}
                		if(dpid == 0 || adid == 0) {
                			JOptionPane.showMessageDialog(signUpFrame, "오류가 발생하였습니다.");
                		}
                		else {
                			DAO.SignUp(name, id, password, dpid, birthday, adid);
                			JOptionPane.showMessageDialog(signUpFrame, "회원가입이 완료되었습니다.");
                			signUpFrame.dispose();
                		}
                	}
                } catch (Exception ex) {System.out.println("익셉션");}
            }
        });

        signUpFrame.add(idLabel);
        signUpFrame.add(idField);
        signUpFrame.add(pwLabel);
        signUpFrame.add(pwField);
        signUpFrame.add(nameLabel);
        signUpFrame.add(nameField);
        signUpFrame.add(departmentLabel);
        signUpFrame.add(departmentComboBox);
        signUpFrame.add(birthdayLabel);
        signUpFrame.add(birthdayField);
        signUpFrame.add(new JLabel());
        signUpFrame.add(signUpButton);

        signUpFrame.setVisible(true);
    }
    
    private static boolean isValidBirthdayFormat(String birthday) {
        // Check if birthday is in the format "YYYY-MM-DD"
        return birthday.matches("\\d{4}-\\d{2}-\\d{2}");
    }

    private static void ProfessorMain() {
        JFrame professorFrame = new JFrame("교수 페이지");
        professorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        professorFrame.setSize(400, 300);
        professorFrame.setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("수업관리", createProfClassPanel());
        tabbedPane.addTab("성적관리", createProfGradePanel());

        professorFrame.add(tabbedPane, BorderLayout.CENTER);

        professorFrame.setVisible(true);
    }

    private static void StudentMain() {
        JFrame studentFrame = new JFrame("학생 페이지");
        studentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentFrame.setSize(400, 300);
        studentFrame.setLayout(new BorderLayout());
        
        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("학적관리", createStudentAcademicPanel()); //학적관리
        tabbedPane.addTab("수업관리", createStudentClassPanel()); //수업관리
        tabbedPane.addTab("성적관리", createStudentGradePanel()); //성적관리


        studentFrame.add(tabbedPane, BorderLayout.CENTER);

        studentFrame.setVisible(true);
    }

    //여기서부터는 버튼마다의 각각 페이지 호출 객체 생성
    private static void createStudentInquiryPage() {
        StudentInquiry StudentInquiryPage = new StudentInquiry(currentUserId);
    }
    
    private static void createStudentChangePage() {
        StudentChange StudentChangePage = new StudentChange(currentUserId);
    }
    
    private static void createStudentTimeTablePage() {
        StudentTimeTable StudentTimeTablePage = new StudentTimeTable(currentUserId);
    }
    
    private static void createStudentClassRegisterPage() {
        StudentClassRegister StudentClassRegisterPage = new StudentClassRegister(currentUserId);
    }
    
    private static void createStudentClassCancelPage() {
        StudentClassCancel StudentClassCancelPage = new StudentClassCancel(currentUserId);
    }
    
    private static void createStudentGradePage() {
    	StudentGrade StudentGradePage = new StudentGrade(currentUserId);
    }
    
    private static void createProfGradeChangePage() {
    	ProfGradeChange ProfGradeChangePage = new ProfGradeChange(currentUserId);
    }
    
    private static void createProfClassMakePage() {
    	ProfClassMake ProfClassMakePage = new ProfClassMake(currentUserId);
    }
    
    private static void createProfClassEditPage() {
    	ProfClassEdit ProfClassEditPage = new ProfClassEdit(currentUserId);
    }
    
    
    private static JPanel createStudentAcademicPanel() { // 학생의 학적관리 패널
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton StudentInquiryButton = new JButton("학적 조회");
        JButton StudentChangeButton = new JButton("정보 변경");

        StudentInquiryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createStudentInquiryPage();
            }
        });
        
        StudentChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createStudentChangePage();
            }
        });
        
        panel.add(StudentInquiryButton);
        panel.add(StudentChangeButton);

        return panel;
    }
    private static JPanel createStudentClassPanel() { // 학생의 수업관리 패널
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton StudentTimeTableButton = new JButton("시간표 조회");
        JButton StudentClassRegisterButton = new JButton("수강 신청");
        JButton StudentClassCancelButton = new JButton("수강 취소");

        StudentTimeTableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	createStudentTimeTablePage();
            }
        });
        
        StudentClassRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	createStudentClassRegisterPage();
            }
        });
        
        StudentClassCancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	createStudentClassCancelPage();
            }
        });
        
        panel.add(StudentTimeTableButton);
        panel.add(StudentClassRegisterButton);
        panel.add(StudentClassCancelButton);

        return panel;
    }
    private static JPanel createStudentGradePanel() { // 학생의 성적관리 패널
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton StudentGradeButton = new JButton("성적 조회");

        StudentGradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	createStudentGradePage();
            }
        });
        
        panel.add(StudentGradeButton);

        return panel;
    }
    
    private static JPanel createProfClassPanel() { // 교수의 수업관리 패널
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton ProfClassMakeButton = new JButton("수업 생성");
        JButton ProfClassEditButton = new JButton("수업 수정");

        ProfClassMakeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                createProfClassMakePage();
            }
        });
        
        ProfClassEditButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	createProfClassEditPage();
            }
        });
        
        panel.add(ProfClassMakeButton);
        panel.add(ProfClassEditButton);

        return panel;
    }

    private static JPanel createProfGradePanel() { // 교수의 성적입력/수정 패널
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());
        
        JButton ProfGradeChangeButton = new JButton("성적 입력/수정");
        
        ProfGradeChangeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	createProfGradeChangePage();
            }
        });
        
        panel.add(ProfGradeChangeButton);

        return panel;
    }
    
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.DriverManager;

public class Main {

    private static String currentUserRole;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            로그인페이지();
        });
        
        try {
    		Class.forName("com.mysql.cj.jdbc.Driver");
    		
    		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/bankdb", "root", "ssho000805!");
    		System.out.println("db 연결됨");
    	}
    	catch(Exception e){}
    }

    private static void 로그인페이지() {
        JFrame loginFrame = new JFrame("URP 로그인");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(300, 200);  // 조금 더 높이 조절
        loginFrame.setLayout(new GridLayout(4, 2));  // 행 추가

        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();

        JLabel pwLabel = new JLabel("Password:");
        JPasswordField pwField = new JPasswordField();

        JButton loginButton = new JButton("로그인");
        JButton signUpButton = new JButton("회원가입");  // 회원가입 버튼 추가

        // 로그인 버튼의 ActionListener
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String id = idField.getText();
                char[] pwChars = pwField.getPassword();
                String password = new String(pwChars);

                // 교수 또는 학생 여부 확인
                if (isProfessor(id, password)) {
                    currentUserRole = "교수";
                    교수페이지();
                    loginFrame.dispose(); // 로그인 창 닫기
                } else if (isStudent(id, password)) {
                    currentUserRole = "학생";
                    학생페이지();
                    loginFrame.dispose(); // 로그인 창 닫기
                } else {
                    JOptionPane.showMessageDialog(loginFrame, "잘못된 ID 또는 Password입니다.");
                }
            }
        });

        // 회원가입 버튼의 ActionListener
        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	회원가입페이지();
            }
        });

        loginFrame.add(idLabel);
        loginFrame.add(idField);
        loginFrame.add(pwLabel);
        loginFrame.add(pwField);
        loginFrame.add(loginButton);
        loginFrame.add(signUpButton);  // 회원가입 버튼 추가
        loginFrame.add(new JLabel()); // 빈 라벨
        loginFrame.add(new JLabel()); // 빈 라벨

        loginFrame.setVisible(true);
    }
    
    private static void 회원가입페이지() {
        JFrame signUpFrame = new JFrame("회원가입");
        signUpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        signUpFrame.setSize(300, 200);
        signUpFrame.setLayout(new GridLayout(5, 2));

        JLabel nameLabel = new JLabel("이름:");
        JTextField nameField = new JTextField();

        JLabel idLabel = new JLabel("ID:");
        JTextField idField = new JTextField();

        JLabel pwLabel = new JLabel("Password:");
        JPasswordField pwField = new JPasswordField();

        JButton signUpButton = new JButton("회원가입");

        signUpButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String id = idField.getText();
                char[] pwChars = pwField.getPassword();
                String password = new String(pwChars);

 /*               if (registerUser(name, id, password)) {
                    JOptionPane.showMessageDialog(signUpFrame, "회원가입이 완료되었습니다.");
                    signUpFrame.dispose();
                } else {
                    JOptionPane.showMessageDialog(signUpFrame, "이미 존재하는 ID입니다. 다른 ID를 사용해주세요.");
                }
 */
            }
        });

        signUpFrame.add(nameLabel);
        signUpFrame.add(nameField);
        signUpFrame.add(idLabel);
        signUpFrame.add(idField);
        signUpFrame.add(pwLabel);
        signUpFrame.add(pwField);
        signUpFrame.add(new JLabel());
        signUpFrame.add(signUpButton);
        signUpFrame.add(new JLabel());
        signUpFrame.add(new JLabel());

        signUpFrame.setVisible(true);
    }

   
    
    private static boolean isProfessor(String id, String password) {
        // 교수 계정 확인 로직을 여기에 추가
        return id.equals("p") && password.equals("p");
    }

    private static boolean isStudent(String id, String password) {
        // 학생 계정 확인 로직을 여기에 추가
        return id.equals("s") && password.equals("s");
    }

    private static void 교수페이지() {
        JFrame professorFrame = new JFrame("교수 페이지");
        professorFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        professorFrame.setSize(600, 400);
        professorFrame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("수업관리", create수업관리1Panel());
        tabbedPane.addTab("성적관리", create성적관리1Panel());

        professorFrame.add(tabbedPane, BorderLayout.CENTER);

        professorFrame.setVisible(true);
    }

    private static void 학생페이지() {
        JFrame studentFrame = new JFrame("학생 페이지");
        studentFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        studentFrame.setSize(600, 400);
        studentFrame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        // 학생 페이지에 필요한 탭 추가
        tabbedPane.addTab("학적관리", create학적관리2Panel());
        tabbedPane.addTab("수업관리", create수업관리2Panel());
        tabbedPane.addTab("성적관리", create성적관리2Panel());


        studentFrame.add(tabbedPane, BorderLayout.CENTER);

        studentFrame.setVisible(true);
    }

    private static JPanel create학적관리2Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton 학적조회Button = new JButton("학적 조회");
        JButton 정보변경Button = new JButton("정보 변경");

        panel.add(학적조회Button);
        panel.add(정보변경Button);

        return panel;
    }

    
    private static JPanel create수업관리1Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton 수업생성Button = new JButton("수업 생성");
        JButton 수업삭제Button = new JButton("수업 삭제");

        panel.add(수업생성Button);
        panel.add(수업삭제Button);

        return panel;
    }

    private static JPanel create성적관리1Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton 성적입력Button = new JButton("성적 입력");
        JButton 성적수정Button = new JButton("성적 수정");

        panel.add(성적입력Button);
        panel.add(성적수정Button);

        return panel;
    }

    private static JPanel create성적관리2Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton 성적조회Button = new JButton("성적 조회");

        panel.add(성적조회Button);

        return panel;
    }
    private static JPanel create수업관리2Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton 시간표조회Button = new JButton("시간표 조회");
        JButton 수강신청Button = new JButton("수강 신청");
        JButton 수강취소Button = new JButton("수강 취소");

        panel.add(시간표조회Button);
        panel.add(수강신청Button);
        panel.add(수강취소Button);

        return panel;
    }
    
}

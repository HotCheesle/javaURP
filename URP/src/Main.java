import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("학사sssssss관리시스템");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLayout(new BorderLayout());

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("학적관리", create학적관리Panel());
        tabbedPane.addTab("수업관리", create수업관리Panel());
        tabbedPane.addTab("성적관리", create성적관리Panel());

        frame.add(tabbedPane, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private static JPanel create학적관리Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton 학적조회Button = new JButton("학적 조회");
        JButton 정보변경Button = new JButton("정보 변경");

        panel.add(학적조회Button);
        panel.add(정보변경Button);

        return panel;
    }

    private static JPanel create수업관리Panel() {
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

    private static JPanel create성적관리Panel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout());

        JButton 성적조회Button = new JButton("성적 조회");

        panel.add(성적조회Button);

        return panel;
    }
}

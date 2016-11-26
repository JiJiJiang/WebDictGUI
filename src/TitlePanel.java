import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 77 on 2016/11/26.
 */
public class TitlePanel extends JPanel{
    //constructor
    public TitlePanel()
    {
        setLayout(new BorderLayout());
        final Color myColor=new Color(31,162,242);//天蓝色

        /*WebDictLabel*/
        ImageIcon webDictLabelIcon=new ImageIcon("image/webDictLabel.jpg");
        JLabel webDictLabel=new JLabel(webDictLabelIcon);
        webDictLabel.setPreferredSize(new Dimension(25,25));
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        webDictLabel.setToolTipText("欢迎使用在线词典！");
        /*Login*/
        ImageIcon loginIcon=new ImageIcon("image/login.jpg");
        JButton login=new JButton(loginIcon);
        ImageIcon loginRolloverIcon=new ImageIcon("image/loginRollover.jpg");
        login.setRolloverIcon(loginRolloverIcon);
        login.setContentAreaFilled(false);
        login.setBorderPainted(false);
        login.setPreferredSize(new Dimension(25,25));
        login.setFocusPainted(false);
        /*create a left panel to put webDictLabel and loginButton*/
        JPanel leftPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        leftPanel.setBackground(myColor);
        leftPanel.add(webDictLabel);
        leftPanel.add(login);
        add(leftPanel,BorderLayout.WEST);

        /*share*/
        ImageIcon shareIcon=new ImageIcon("image/shareButton.jpg");
        JButton share=new JButton(shareIcon);
        share.setContentAreaFilled(false);
        share.setBorderPainted(false);
        share.setPreferredSize(new Dimension(25,25));
        share.setFocusPainted(false);
        share.setToolTipText("分享单词卡！");
        /*create a right panel to put shareButton*/
        JPanel rightPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        rightPanel.setBackground(myColor);
        rightPanel.add(share);
        add(rightPanel,BorderLayout.CENTER);

        /*add all listeners here.*/
        /*login*/
        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Handle Login!");
            }
        });
        /*share*/
    }
}

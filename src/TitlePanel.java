import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 77 on 2016/11/26.
 */
public class TitlePanel extends JPanel{
    private JFrame userFrame=new JFrame();//登录的frame
    private UserPanel userPanel=new UserPanel();//登录的panel

    JButton message=null;//消息盒子

    private ContentPanel contentPanel;//use it to display the result
    //constructor
    public TitlePanel(ContentPanel contentPanel)
    {
        this.contentPanel=contentPanel;
        setLayout(new BorderLayout());
        final Color myColor=new Color(31,162,242);//天蓝色

        /*WebDictLabel*/
        ImageIcon webDictLabelIcon=new ImageIcon("image/webDictLabel.jpg");
        JLabel webDictLabel=new JLabel(webDictLabelIcon);
        webDictLabel.setPreferredSize(new Dimension(25,25));
        ToolTipManager.sharedInstance().setDismissDelay(10000);
        webDictLabel.setToolTipText("欢迎使用在线词典！");
        /*User*/
        ImageIcon userIcon=new ImageIcon("image/login.jpg");
        JButton user=new JButton(userIcon);
        ImageIcon userRolloverIcon=new ImageIcon("image/loginRollover.jpg");
        user.setRolloverIcon(userRolloverIcon);
        user.setContentAreaFilled(false);
        user.setBorderPainted(false);
        user.setPreferredSize(new Dimension(25,25));
        user.setFocusPainted(false);
        user.setToolTipText("用户窗口");
        /*create a left panel to put webDictLabel and loginButton*/
        JPanel leftPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        leftPanel.setBackground(myColor);
        leftPanel.add(webDictLabel);
        leftPanel.add(user);
        add(leftPanel,BorderLayout.WEST);

        /*message*/
        ///*
        //ImageIcon messageIcon=new ImageIcon("image/shareButton.jpg");
        message=new JButton("消息 0");
        message.setVisible(false);
        message.setContentAreaFilled(false);
        message.setBorderPainted(false);
        message.setPreferredSize(new Dimension(100,25));
        message.setFocusPainted(false);
        message.setToolTipText("分享单词卡！");
        //*/
        /*create a right panel to put shareButton*/
        JPanel rightPanel=new JPanel(new FlowLayout(FlowLayout.RIGHT,0,0));
        rightPanel.setBackground(myColor);
        rightPanel.add(message);
        add(rightPanel,BorderLayout.CENTER);

        //初始化用户的frame
        userFrame.setTitle("用户窗口");
        userFrame.setSize(250,300);
        userFrame.setLocationRelativeTo(null);
        userFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//只关闭该窗口
        userFrame.setVisible(false);
        userFrame.setResizable(false);
        userFrame.add(userPanel);
        userPanel.setContentPanel(contentPanel);

        /*add all listeners here.*/
        /*user*/
        user.addActionListener(new UserListener());
        /*message*/
        message.addActionListener(new MessageCheck());
    }
    private class UserListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Handle Login!");
            userFrame.setVisible(true);
        }
    }
    private class MessageCheck implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            if(contentPanel.allWords.size()>0) {
                String word=contentPanel.allWords.get(0);
                contentPanel.allWords.remove(0);
                boolean[] selectedItem=contentPanel.allSelectedItems.get(0);
                contentPanel.allSelectedItems.remove(0);

                int messageNum=contentPanel.allWords.size();
                if(messageNum==0)
                    message.setForeground(Color.black);
                message.setText("消息 "+messageNum);

                contentPanel.setSelectedItem(selectedItem);
                contentPanel.displayWordExplanations(word);
            }
        }
    }
}

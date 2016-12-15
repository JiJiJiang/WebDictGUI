/**
 * Created by 77 on 2016/12/15.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UserPanel extends JPanel{
    //state Label
    private ImageIcon onlineIcon=new ImageIcon("image/online.jpg");
    private ImageIcon offlineIcon=new ImageIcon("image/offline.jpg");

    //register frame
    private JFrame registerFrame=new JFrame();
    //private

    //login frame


    public UserPanel()
    {
        setLayout(new BorderLayout());

        /*titlePanel*/
        JPanel titlePanel=new JPanel();
        titlePanel.setLayout(new BorderLayout());
        //左
        JLabel status=new JLabel();
        status.setIcon(offlineIcon);
        status.setToolTipText("离线状态");
        titlePanel.add(status,BorderLayout.WEST);
        //右
        JPanel titleRightPanel=new JPanel();
        titleRightPanel.setLayout(new GridLayout(1,2));
        JButton register=new JButton("注册");
        titleRightPanel.add(register);
        JButton login=new JButton("登录");
        titleRightPanel.add(login);
        JButton logout=new JButton("注销");
        titleRightPanel.add(logout);
        titlePanel.add(titleRightPanel,BorderLayout.CENTER);


        add(titlePanel,BorderLayout.NORTH);

        /*userListPanel*/
        JPanel userListPanel=new JPanel();
        userListPanel.setLayout(new BorderLayout());
        //左
        JPanel leftPanel=new JPanel();
        leftPanel.setLayout(new GridLayout(2,1));
        JButton freshen=new JButton("刷新");
        leftPanel.add(freshen);
        JButton share=new JButton("分享");
        leftPanel.add(share);
        userListPanel.add(leftPanel,BorderLayout.WEST);
        //右
        JTabbedPane rightPanel=new JTabbedPane();
        JPanel online=new JPanel();
        JList<String> onlineUserList=new JList<String>();
        onlineUserList.setVisibleRowCount(12);
        onlineUserList.setFixedCellWidth(150);
        JScrollPane onlineJScrollPane=new JScrollPane(onlineUserList);
        onlineJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        online.add(onlineJScrollPane);
        //onlineUserList.setListData(new String[]{"as","b","c","d","as","b","c","d","as","b","c","d","as","b","c","d"});
        JPanel offline=new JPanel();
        JList<String> offlineUserList=new JList<String>();
        offlineUserList.setVisibleRowCount(12);
        offlineUserList.setFixedCellWidth(150);
        JScrollPane offlineJScrollPane=new JScrollPane(offlineUserList);
        offlineJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        offline.add(offlineJScrollPane);
        rightPanel.add(online,"在线用户");
        rightPanel.add(offline,"离线用户");
        userListPanel.add(rightPanel,BorderLayout.CENTER);

        add(userListPanel,BorderLayout.CENTER);
        //userListPanel.setVisible(false);

        //all listeners
        register.addActionListener(new RegisterListener());
        login.addActionListener(new LoginListener());
    }

    /*register listener*/
    private class RegisterListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {

        }
    }
    /*login listener*/
    private class LoginListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Handle Login!");
        }
    }
}
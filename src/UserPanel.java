/**
 * Created by 77 on 2016/12/15.
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class UserPanel extends JPanel{
    //status Label
    boolean status=false;//未登录
    int[] statusChange=new int[1];//用户登录状态变化
    JLabel statusLabel=null;//用户登录状态指示的label
    private ImageIcon onlineIcon=new ImageIcon("image/online.jpg");
    private ImageIcon offlineIcon=new ImageIcon("image/offline.jpg");

    //登录以后才可见
    JPanel userListPanel=null;
    JList<String> onlineUserList=null;//在线用户列表
    JList<String> offlineUserList=null;//离线用户列表

    //register frame
    private JFrame registerFrame=new JFrame();
    private RegisterPanel registerPanel=new RegisterPanel();

    //login frame
    private JFrame loginFrame=new JFrame();
    private LoginPanel loginPanel=new LoginPanel();


    public UserPanel()
    {
        setLayout(new BorderLayout());
        /*titlePanel*/
        JPanel titlePanel=new JPanel();
        titlePanel.setLayout(new BorderLayout());
        //左
        statusLabel=new JLabel();
        statusLabel.setIcon(offlineIcon);
        statusLabel.setToolTipText("离线状态");
        titlePanel.add(statusLabel,BorderLayout.WEST);
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
        userListPanel=new JPanel();
        userListPanel.setLayout(new BorderLayout());
        //左
        JPanel leftPanel=new JPanel();
        leftPanel.setLayout(new GridLayout(2,1));
        JButton freshen=new JButton("刷新");
        freshen.setToolTipText("刷新用户列表");
        leftPanel.add(freshen);
        JButton share=new JButton("分享");
        share.setToolTipText("分享单词卡");
        leftPanel.add(share);
        userListPanel.add(leftPanel,BorderLayout.WEST);
        //右
        JTabbedPane rightPanel=new JTabbedPane();
        JPanel online=new JPanel();
        onlineUserList=new JList<String>();
        onlineUserList.setVisibleRowCount(12);
        onlineUserList.setFixedCellWidth(150);
        JScrollPane onlineJScrollPane=new JScrollPane(onlineUserList);
        onlineJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        online.add(onlineJScrollPane);
        //onlineUserList.setListData(new String[]{"as","b","c","d","as","b","c","d","as","b","c","d","as","b","c","d"});
        JPanel offline=new JPanel();
        offlineUserList=new JList<String>();
        offlineUserList.setVisibleRowCount(12);
        offlineUserList.setFixedCellWidth(150);
        JScrollPane offlineJScrollPane=new JScrollPane(offlineUserList);
        offlineJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        offline.add(offlineJScrollPane);
        rightPanel.add(online,"在线用户");
        rightPanel.add(offline,"离线用户");
        userListPanel.add(rightPanel,BorderLayout.CENTER);

        add(userListPanel,BorderLayout.CENTER);
        userListPanel.setVisible(false);


        //初始化注册窗口
        registerFrame.setTitle("注册窗口");
        registerFrame.setSize(300,140);
        registerFrame.setLocationRelativeTo(null);
        registerFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//只关闭该窗口
        registerFrame.setVisible(false);
        registerFrame.setResizable(false);
        registerFrame.add(registerPanel);
        registerPanel.setFrame(registerFrame);

        //初始化登录窗口
        loginFrame.setTitle("登录窗口");
        loginFrame.setSize(300,120);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//只关闭该窗口
        loginFrame.setVisible(false);
        loginFrame.setResizable(false);
        loginFrame.add(loginPanel);
        statusChange[0]=0;
        loginPanel.setStatusChange(statusChange);
        loginPanel.setFrame(loginFrame);


        //判断用户登录状态是否发生变化
        Thread userStatusListener=new Thread(new UserStatusListener());
        userStatusListener.start();

        //all listeners
        register.addActionListener(new RegisterListener());
        login.addActionListener(new LoginListener());
        logout.addActionListener(new LogoutListener());
        freshen.addActionListener(new RenewUserList());
        share.addActionListener(new ShareListener());
    }

    /*register listener*/
    private class RegisterListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            registerFrame.setVisible(true);
        }
    }
    /*login listener*/
    private class LoginListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            //System.out.println("Handle Login!");
            if(status)//已经登录
                JOptionPane.showMessageDialog(null, "已登录！", "提示",JOptionPane.PLAIN_MESSAGE);
            else
                loginFrame.setVisible(true);
        }
    }
    /*logout listener*/
    private class LogoutListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e){
            if(!status)//未登录
                JOptionPane.showMessageDialog(null, "未登录！", "提示",JOptionPane.PLAIN_MESSAGE);
            else {
                synchronized (statusChange) {
                    statusChange[0] = 1;
                }
                JOptionPane.showMessageDialog(null, "注销成功！", "提示",JOptionPane.PLAIN_MESSAGE);
            }
        }
    }
    /*freshen listener*/
    private class RenewUserList implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            //向服务器发送请求获得在线和离线用户列表
            onlineUserList.setListData(new String[]{"as","b","c","d","as","b","c","d","as","b","c","d","as","b","c","d"});
            offlineUserList.setListData(new String[]{"as","b","c","d","as","b","c","d","as","b","c","d","as","b","c","d"});
        }
    }
    /*share listener*/
    private class ShareListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {

        }
    }

    /*监听用户登录状态是否发生改变的任务*/
    private class UserStatusListener implements Runnable
    {
        public void run()
        {
            while(true)
            {
                //System.out.println("..");
                synchronized (statusChange) {
                    if (statusChange[0] == 1) {
                        //System.out.println("AAA");
                        statusChange[0] = 0;
                        status = !status;
                        if (status)//用户刚刚登陆成功
                        {
                            statusLabel.setIcon(onlineIcon);
                            statusLabel.setToolTipText("在线状态");
                            userListPanel.setVisible(true);
                        } else//用户刚刚注销
                        {
                            statusLabel.setIcon(offlineIcon);
                            statusLabel.setToolTipText("离线状态");
                            userListPanel.setVisible(false);
                        }
                    }
                }
            }
        }
    }
}
/**
 * Created by 77 on 2016/12/15.
 */

import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class UserPanel extends JPanel{
    //status Label
    boolean status=false;//未登录
    int[] statusChange=new int[1];//用户登录状态变化
    String[] userName=new String[1];//用户名
    String[] password=new String[1];//密码
    JLabel statusLabel=null;//用户登录状态指示的label
    private ImageIcon onlineIcon=new ImageIcon("image/online.jpg");
    private ImageIcon offlineIcon=new ImageIcon("image/offline.jpg");

    //登录以后才可见
    JPanel userListPanel=null;
    JList<String> onlineUserList=null;//在线用户列表
    JList<String> offlineUserList=null;//离线用户列表
    boolean toShare=false;//是否选择了分享

    //register frame
    private JFrame registerFrame=new JFrame();
    private RegisterPanel registerPanel=new RegisterPanel();

    //login frame
    private JFrame loginFrame=new JFrame();
    private LoginPanel loginPanel=new LoginPanel();

    private ContentPanel contentPanel;//use it to display the result

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
        loginPanel.setUserName(userName);
        loginPanel.setFrame(loginFrame);
        loginPanel.setPassword(password);
        loginPanel.setUserPanel(this);

        //判断用户登录状态是否发生变化
        Thread userStatusListener=new Thread(new UserStatusListener());
        userStatusListener.start();

        //新开一个线程，向服务器发送请求，判断登录之后是否消息
        Thread messageListener=new Thread(new MessageListener());
        messageListener.start();

        //all listeners
        register.addActionListener(new RegisterListener());
        login.addActionListener(new LoginListener());
        logout.addActionListener(new LogoutListener());
        freshen.addActionListener(new RenewUserList());
        share.addActionListener(new ShareListener());

        onlineUserList.addListSelectionListener(new OnlineUserListListener());
        offlineUserList.addListSelectionListener(new OfflineUserListListener());
    }
    public void setContentPanel(ContentPanel contentPanel)
    {
        this.contentPanel=contentPanel;
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
                //向服务器发送注销的请求
                sendLogout();
            }
        }
    }
    /*freshen listener*/
    private class RenewUserList implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            renewUserList();
        }
    }
    /*share listener*/
    private class ShareListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e)
        {
            toShare=true;
            renewUserList();
            JOptionPane.showMessageDialog(null, "请选择目标用户", "提示",JOptionPane.PLAIN_MESSAGE);
        }
    }
    /*JList listener*/
    private class OnlineUserListListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e)
        {
            if(toShare&&onlineUserList.getSelectedValue()!=null) {//用户点击了分享
                if (onlineUserList.getValueIsAdjusting())
                {
                    String senderUserName=userName[0];
                    //System.out.println(senderUserName);
                    String targetUserName=onlineUserList.getSelectedValue();
                    //System.out.println(targetUserName);
                    //获得发送的单词及网站
                    String curWordOrPhrase=contentPanel.getCurWordOrPhrase();//单词
                    boolean[] selectedItem=contentPanel.getSelectedItem();//分享的网站
                    if(curWordOrPhrase!=null)//要发送的单词非空
                    {
                        //向服务器发送单词卡
                        //senderUserName,targetUserName,word,{true,true,true}
                        String fromUser=senderUserName;
                        String toUser=targetUserName;
                        String word=curWordOrPhrase;
                        String url="http://115.159.0.12:8080/card/send";
                        String param="fromUser="+fromUser+"&toUser="+toUser+"&word="+word+"&youdao="+selectedItem[1]+"&jinshan="+selectedItem[2]+"&haici="+selectedItem[0];
                        //System.out.println(param);
                        String jsonResult=contentPanel.sendPost(url,param);
                        //System.out.println(jsonResult);
                        JSONObject all=new JSONObject(jsonResult);
                        String status=all.getString("status");
                        if(status.equals("success"))//发送成功
                            JOptionPane.showMessageDialog(null, "成功向用户\""+targetUserName+"\"分享单词\""+curWordOrPhrase+"\"!!", "提示",JOptionPane.PLAIN_MESSAGE);
                    }
                    else
                    {
                        //错误提示
                        JOptionPane.showMessageDialog(null, "请指定单词！", "错误",JOptionPane.ERROR_MESSAGE);
                    }

                    toShare=false;//一次只能分享给一个用户
                }
            }
        }
    }
    private class OfflineUserListListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e)
        {
            if(toShare&&offlineUserList.getSelectedValue()!=null) {//用户点击了分享
                if (offlineUserList.getValueIsAdjusting())
                {
                    String senderUserName=userName[0];
                    //System.out.println(senderUserName);
                    String targetUserName=offlineUserList.getSelectedValue();
                    //System.out.println(targetUserName);
                    //获得发送的单词及网站
                    String curWordOrPhrase=contentPanel.getCurWordOrPhrase();//单词
                    boolean[] selectedItem=contentPanel.getSelectedItem();//分享的网站
                    if(curWordOrPhrase!=null)//要发送的单词非空
                    {
                        //向服务器发送单词卡
                        //senderUserName,targetUserName,word,{true,true,true}
                        String fromUser=senderUserName;
                        String toUser=targetUserName;
                        String word=curWordOrPhrase;
                        String url="http://115.159.0.12:8080/card/send";
                        String param="fromUser="+fromUser+"&toUser="+toUser+"&word="+word+"&youdao="+selectedItem[1]+"&jinshan="+selectedItem[2]+"&haici="+selectedItem[0];
                        //System.out.println(param);
                        String jsonResult=contentPanel.sendPost(url,param);
                        //System.out.println(jsonResult);
                        JSONObject all=new JSONObject(jsonResult);
                        String status=all.getString("status");
                        if(status.equals("success"))//发送成功
                            JOptionPane.showMessageDialog(null, "成功向用户\""+targetUserName+"\"分享单词\""+curWordOrPhrase+"\"!!", "提示",JOptionPane.PLAIN_MESSAGE);
                    }
                    else
                    {
                        //错误提示
                        JOptionPane.showMessageDialog(null, "请指定单词！", "错误",JOptionPane.ERROR_MESSAGE);
                    }
                    toShare=false;//一次只能分享给一个用户
                }
            }
        }
    }

    //发送注销请求
    public void sendLogout()
    {
            String url="http://115.159.0.12:8080/user/logout";
            String param="username="+userName[0]+"&pwd="+password[0];
            String jsonResult=contentPanel.sendPost(url,param);
            System.out.println(jsonResult);
            JSONObject all=new JSONObject(jsonResult);
            String status=all.getString("status");
            if(status.equals("success"))
            {
                synchronized (statusChange) {
                    statusChange[0] = 1;
                }
                JOptionPane.showMessageDialog(null, "注销成功！", "提示",JOptionPane.PLAIN_MESSAGE);
            }
            else
                JOptionPane.showMessageDialog(null, "注销失败！", "错误",JOptionPane.ERROR_MESSAGE);
    }
    //获得用户列表
    public void renewUserList()
    {
        //清空当前列表
        //onlineUserList.setListData(new String[]{});
        //offlineUserList.setListData(new String[]{});
        //向服务器发送请求获得在线和离线用户列表
        String urlName="http://115.159.0.12:8080/user/list";
        String jsonResult=contentPanel.get(urlName);
        JSONObject all=new JSONObject(jsonResult);
        //在线用户
        JSONArray onlineUsers=all.getJSONArray("onlineUsers");
        String[] onlineUsersStringArray=new String[onlineUsers.length()-1];
        int index=0;
        for(int i=0;i<onlineUsers.length();i++)
        {
            if(!onlineUsers.getString(i).equals(userName[0])) {
                onlineUsersStringArray[index] = onlineUsers.getString(i);
                index++;
            }
        }
        onlineUserList.setListData(onlineUsersStringArray);
        //离线用户
        JSONArray offlineUsers=all.getJSONArray("offlineUsers");
        String[] offlineUsersStringArray=new String[offlineUsers.length()];
        for(int i=0;i<offlineUsers.length();i++)
        {
            offlineUsersStringArray[i]=offlineUsers.getString(i);
        }
        offlineUserList.setListData(offlineUsersStringArray);
        //onlineUserList.setListData(new String[]{"as","b","c","d","as","b","c","d","as","b","c","d","as","b","c","d"});
        //offlineUserList.setListData(new String[]{"as","b","c","d","as","b","c","d","as","b","c","d","as","b","c","d"});
    }


    /*监听用户登录状态是否发生改变的任务*/
    private class UserStatusListener implements Runnable
    {
        public void run()
        {
            while(true)
            {
                //System.out.println("..");
                try {
                    Thread.sleep(2000);//每隔2秒请求一次
                }catch(InterruptedException e)
                {
                    System.out.println("Interrupted!");
                }
                synchronized (statusChange) {
                    if (statusChange[0] == 1) {
                        //System.out.println("AAA");
                        statusChange[0] = 0;
                        status = !status;
                        if (status)//用户刚刚登录成功
                        {
                            statusLabel.setIcon(onlineIcon);
                            statusLabel.setToolTipText("在线状态");
                            userListPanel.setVisible(true);
                            contentPanel.headPanel.titlePanel.message.setVisible(true);
                            contentPanel.headPanel.titlePanel.user.setToolTipText("当前在线："+userName[0]);
                        } else//用户刚刚注销
                        {
                            statusLabel.setIcon(offlineIcon);
                            statusLabel.setToolTipText("离线状态");
                            userListPanel.setVisible(false);
                            contentPanel.headPanel.titlePanel.message.setVisible(false);
                            contentPanel.headPanel.titlePanel.user.setToolTipText("用户窗口");
                        }
                    }
                }
            }
        }
    }
    /*监听用户登录之后是否接受到了消息*/
    private class MessageListener implements Runnable
    {
        public void run()
        {
            while(true) {
                try {
                    Thread.sleep(5000);//每隔10秒请求一次
                }catch(InterruptedException e)
                {
                    System.out.println("Interrupted!");
                }
                if (status)//用户已经登录
                {
                    //向服务器发送消息请求
                    String urlName="http://115.159.0.12:8080/card/receive?username="+userName[0];
                    String jsonResult=contentPanel.get(urlName);
                    JSONObject all=new JSONObject(jsonResult);
                    JSONArray cards=all.getJSONArray("cards");
                    boolean receiveMessage=(cards.length()>0);
                    if(receiveMessage)//有消息
                    {
                        //解析json获得senderUserName,wordOrPhrase,selectedItem
                        for (int i = 0; i < cards.length(); i++) {
                            JSONObject aCard=cards.getJSONObject(i);
                            String senderUserName =aCard.getString("from");
                            String wordOrPhrase = aCard.getString("word");
                            boolean[] selectedItem = {aCard.getBoolean("haici"), aCard.getBoolean("youdao"), aCard.getBoolean("jinshan")};
                            //更新contentPanel
                            contentPanel.addWordOrPhrase(wordOrPhrase);
                            contentPanel.addSelectedItem(selectedItem);
                            //提示收到消息
                            JOptionPane.showMessageDialog(null, "收到用户\"" + senderUserName + "\"分享的单词\"" + wordOrPhrase + "\"!!\n请在消息中查看!!", "提示", JOptionPane.PLAIN_MESSAGE);
                        }
                    }
                }
                try {
                    Thread.sleep(5000);//每隔10秒请求一次
                }catch(InterruptedException e)
                {
                    System.out.println("Interrupted!");
                }
            }
        }
    }
}
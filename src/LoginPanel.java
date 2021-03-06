/**
 * Created by 77 on 2016/12/15.
 */

import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class LoginPanel extends JPanel{
    JFrame outFrame=null;
    int[] statusChange=null;
    String[] userName=null;
    String[] password=null;

    JTextField userNameTextField=null;
    JPasswordField passwordTextField=null;

    UserPanel userPanel=null;

    public LoginPanel()
    {
        //upPanel
        JPanel upPanel=new JPanel();
        upPanel.setLayout(new GridLayout(2,2,5,5));
        upPanel.add(new JLabel("用户名:"));
        userNameTextField=new JTextField(20);
        upPanel.add(userNameTextField);
        upPanel.add(new JLabel("密码："));
        passwordTextField=new JPasswordField(20);
        upPanel.add(passwordTextField);
        add(upPanel,BorderLayout.CENTER);

        //downPanel
        JPanel downPanel=new JPanel(new GridLayout(1,2,10,10));
        JButton confirm=new JButton("确定");
        downPanel.add(confirm);
        JButton cancel=new JButton("取消");
        downPanel.add(cancel);
        add(downPanel,BorderLayout.SOUTH);

        //all listeners
        confirm.addActionListener(new ConfrimListener());
        cancel.addActionListener(new CancelListener());
    }

    public void setFrame(JFrame frame)
    {
        outFrame=frame;
    }
    public void setStatusChange(int[] statusChange)
    {
        this.statusChange=statusChange;
    }
    public void setUserName(String[] userName){
        this.userName=userName;
    }
    public void setPassword(String[] password){
        this.password=password;
    }
    public void setUserPanel(UserPanel userPanel){
        this.userPanel=userPanel;
    }

    /*two listeners*/
    private class ConfrimListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            String userNameString=new String(userNameTextField.getText().trim());
            String passwordString=new String(passwordTextField.getPassword());
            if(userNameString.length()==0||passwordString.length()==0)
                JOptionPane.showMessageDialog(null, "输入栏不能为空！！", "错误",JOptionPane.ERROR_MESSAGE);
            else
            {
                //建立url，发送登录请求给服务器
                String url= "http://115.159.0.12:8080/user/login";
                String param="username="+userNameString+"&pwd="+passwordString;
                String jsonResult=ContentPanel.sendPost(url,param);
                System.out.println(jsonResult);
                //解析jsonResult
                JSONObject all=new JSONObject(jsonResult);
                String status=all.getString("status");
                if(status.equals("success"))
                {
                    //清空输入框
                    userNameTextField.setText("");
                    passwordTextField.setText("");
                    outFrame.setVisible(false);
                    synchronized (statusChange) {
                        statusChange[0] = 1;
                        userName[0]=userNameString;
                        password[0]=passwordString;
                    }
                    //更新在线和离线用户列表
                    userPanel.renewUserList();
                    JOptionPane.showMessageDialog(null, "登录成功！", "提示",JOptionPane.PLAIN_MESSAGE);
                }
                else
                {
                    String failReason=all.getString("msg");
                    if(failReason.equals("user not found"))//用户名不存在
                        JOptionPane.showMessageDialog(null, "用户名不存在，请重新登录！", "错误",JOptionPane.ERROR_MESSAGE);
                    else if(failReason.equals("password incorrect"))//密码错误
                        JOptionPane.showMessageDialog(null, "密码错误，请重新登录！", "错误",JOptionPane.ERROR_MESSAGE);
                    //else
                        //JOptionPane.showMessageDialog(null, "该用户已经登录！！", "错误",JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    private class CancelListener implements  ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            outFrame.setVisible(false);
        }
    }
}

/**
 * Created by 77 on 2016/12/15.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel{
    JFrame outFrame=null;
    JTextField userName=null;
    JPasswordField password=null;
    JPasswordField passwordConfirm=null;

    public RegisterPanel()
    {
        //upPanel
        JPanel upPanel=new JPanel();
        upPanel.setLayout(new GridLayout(3,2,5,5));
        upPanel.add(new JLabel("用户名:"));
        userName=new JTextField(20);
        upPanel.add(userName);
        upPanel.add(new JLabel("密码："));
        password=new JPasswordField(20);
        upPanel.add(password);
        upPanel.add(new JLabel("确认密码："));
        passwordConfirm=new JPasswordField(20);
        upPanel.add(passwordConfirm);
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
    private class ConfrimListener implements ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            String userNameString=new String(userName.getText().trim());
            String passwordString=new String(password.getPassword());
            //System.out.println(passwordString);
            String passwordConfirmString=new String(passwordConfirm.getPassword());
            //System.out.println(passwordConfirmString);
            if(userNameString.length()==0||passwordString.length()==0||passwordConfirmString.length()==0)
                JOptionPane.showMessageDialog(null, "输入栏不能为空！！", "错误",JOptionPane.ERROR_MESSAGE);
            else if(!passwordString.equals(passwordConfirmString))
                JOptionPane.showMessageDialog(null, "密码不一致", "错误",JOptionPane.ERROR_MESSAGE);
            else
            {
                //建立url，发送注册请求给服务器

                //得到返回结果
                if(true)
                {
                    //清空输入框
                    userName.setText("");
                    password.setText("");
                    passwordConfirm.setText("");
                    outFrame.setVisible(false);
                    JOptionPane.showMessageDialog(null, "注册成功，请登录！", "提示",JOptionPane.PLAIN_MESSAGE);
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "注册失败，请重新注册！", "提示",JOptionPane.PLAIN_MESSAGE);
                }
            }
        }
    }
    public boolean sendRegister()
    {
        return true;
    }
    private class CancelListener implements  ActionListener
    {
        public void actionPerformed(ActionEvent e) {
            outFrame.setVisible(false);
        }
    }
}

import javax.swing.*;
import java.awt.*;

/**
 * Created by 77 on 2016/11/26.
 */
public class JCheckBoxsPanel extends JPanel{
    private final Font font=new Font("Serif",Font.BOLD,15);//字体

    //private FlowLayout flowLayout=new FlowLayout(FlowLayout.LEFT,70,1);
    //constructor
    public JCheckBoxsPanel()
    {
        //setLayout(flowLayout);
        setLayout(new GridLayout(1,3,0,0));
        //setBackground(Color.WHITE);
        /*set up three JCheckBox*/
        JCheckBox baidu=new JCheckBox("百度");
        baidu.setFont(font);
        //baidu.setBackground(Color.WHITE);
        baidu.setFocusPainted(false);
        add(baidu);
        JCheckBox youdao=new JCheckBox("有道");
        youdao.setFont(font);
        //youdao.setBackground(Color.WHITE);
        youdao.setFocusPainted(false);
        add(youdao);
        JCheckBox jinshan=new JCheckBox("金山");
        jinshan.setFont(font);
        //jinshan.setBackground(Color.WHITE);
        jinshan.setFocusPainted(false);
        add(jinshan);

        /*add all listeners here!*/
    }
/*
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //super.paintComponent(g);
        System.out.println((getWidth()-100-150)/4);
        System.out.println(getHeight());
        setSize(new Dimension(getWidth(),getHeight()));
        flowLayout.setHgap((getWidth()-100-150)/4);

    }
    */
}

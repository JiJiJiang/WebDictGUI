import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by 77 on 2016/11/26.
 */
public class JCheckBoxsPanel extends JPanel{
    private final Font font=new Font("Serif",Font.BOLD,15);//字体
    private ContentPanel contentPanel;//use it to display the result

    //private FlowLayout flowLayout=new FlowLayout(FlowLayout.LEFT,70,1);
    //constructor
    public JCheckBoxsPanel(ContentPanel contentPanel)
    {
        this.contentPanel=contentPanel;
        //setLayout(flowLayout);
        setLayout(new GridLayout(1,3,0,0));
        //setBackground(Color.WHITE);

        /*set up three JCheckBox*/
        JCheckBox baidu=new JCheckBox("百度");
        baidu.setFont(font);
        //baidu.setBackground(Color.WHITE);
        baidu.setFocusPainted(false);
        JPanel baiduPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        baiduPanel.add(baidu);
        add(baiduPanel);

        JCheckBox youdao=new JCheckBox("有道",true);
        youdao.setFont(font);
        //youdao.setBackground(Color.WHITE);
        youdao.setFocusPainted(false);
        JPanel youdaoPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        youdaoPanel.add(youdao);
        add(youdaoPanel);

        JCheckBox jinshan=new JCheckBox("金山");
        jinshan.setFont(font);
        //jinshan.setBackground(Color.WHITE);
        jinshan.setFocusPainted(false);
        JPanel jinshanPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,5,5));
        jinshanPanel.add(jinshan);
        add(jinshanPanel);

        /*add all listeners here!*/
        baidu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.setSelectedItem(0,baidu.isSelected());
            }
        });
        youdao.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.setSelectedItem(1,youdao.isSelected());
            }
        });
        jinshan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                contentPanel.setSelectedItem(2,jinshan.isSelected());
            }
        });
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

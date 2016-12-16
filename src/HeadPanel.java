import javax.swing.*;
import java.awt.*;

/**
 * Created by 77 on 2016/11/26.
 */
public class HeadPanel extends JPanel{
    //constructor
    TitlePanel titlePanel=null;
    InputFieldPanel inputFieldPanel=null;
    JCheckBoxsPanel jCheckBoxsPanel=null;
    public HeadPanel(ContentPanel contentPanel)
    {
        //setLayout(new GridLayout(3,1,0,0));
        setLayout(new BorderLayout(0,0));
        //title panel(WebDictLabel,Login,share)
        titlePanel=new TitlePanel(contentPanel);
        add(titlePanel,BorderLayout.NORTH);
        //inputField panel(textField(hidden combo box),searchButton)
        inputFieldPanel=new InputFieldPanel(contentPanel);
        add(inputFieldPanel,BorderLayout.CENTER);
        //JCheckBoxs panel(jchkBaiDu,jchkYouDao,jchkJinSan)
        jCheckBoxsPanel=new JCheckBoxsPanel(contentPanel);
        add(jCheckBoxsPanel,BorderLayout.SOUTH);
    }
}

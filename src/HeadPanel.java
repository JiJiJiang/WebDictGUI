import javax.swing.*;
import java.awt.*;

/**
 * Created by 77 on 2016/11/26.
 */
public class HeadPanel extends JPanel{
    //constructor
    public HeadPanel()
    {
        //setLayout(new GridLayout(3,1,0,0));
        setLayout(new BorderLayout(0,0));
        //title panel(WebDictLabel,Login,share)
        add(new TitlePanel(),BorderLayout.NORTH);
        //inputField panel(textField(hidden combo box),searchButton)
        add(new InputFieldPanel(),BorderLayout.CENTER);
        //JCheckBoxs panel(jchkBaiDu,jchkYouDao,jchkJinSan)
        add(new JCheckBoxsPanel(),BorderLayout.SOUTH);
    }
}

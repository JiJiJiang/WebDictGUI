/**
 * Created by 77 on 2016/11/26.
 * Copyright:All rights reserved.
 * Function: The runner class "WebDictGUIRunner".
 * History: Author     Date          Version    Description
 *          JJJiang    2016/11/26    1.0        Set up it.
 */

import java.awt.*;
import javax.swing.*;

public class WebDictGUIRunner extends JFrame{
	private final static int FRAME_WIDTH=580;
	private final static int FRAME_HEIGHT=440;

    //constructor
    public WebDictGUIRunner()
    {
        setLayout(new BorderLayout());
        /*content panel*/
        ContentPanel contentPanel=new ContentPanel();
        JScrollPane jScrollPane=new JScrollPane(contentPanel);
        contentPanel.setJScrollPane(jScrollPane);
        //jScrollPane.setBorder(new LineBorder(Color.GRAY,0));
        jScrollPane.getVerticalScrollBar().setUnitIncrement(15);
        //jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(jScrollPane,BorderLayout.CENTER);
        //add(contentPanel,BorderLayout.CENTER);

        /*head panel*/
        HeadPanel headPanel=new HeadPanel(contentPanel);
        contentPanel.setHeadPanel(headPanel);
        add(headPanel,BorderLayout.NORTH);
    }

    public static void main(String[] args)throws Exception
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        WebDictGUIRunner frame=new WebDictGUIRunner();
        frame.setTitle("WebDict");
        frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        frame.setMinimumSize(new Dimension(FRAME_WIDTH-50,FRAME_HEIGHT-50));
        //frame.setLocation(200, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        //initial the dictionary
        ReadDictionary.initialDictionary();
    }
}

/**
 * Created by 77 on 2016/11/26.
 * Copyright:All rights reserved.
 * Function: The runner class "WebDictGUIRunner".
 * History: Author     Date          Version    Description
 *          JJJiang    2016/11/26    1.0        Set up it.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import javax.swing.text.*;

import java.util.*;


public class WebDictGUIRunner extends JFrame{
	private final static int FRAME_WIDTH=580;
	private final static int FRAME_HEIGHT=420;

    //constructor
    public WebDictGUIRunner()
    {
        setLayout(new BorderLayout());
        //content panel
        ContentPanel contentPanel=new ContentPanel();
        JScrollPane jScrollPane=new JScrollPane(contentPanel);
        //jScrollPane.setBorder(new LineBorder(Color.GRAY,0));
        jScrollPane.getVerticalScrollBar().setUnitIncrement(15);
        //jScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        add(jScrollPane,BorderLayout.CENTER);
        //add(contentPanel,BorderLayout.CENTER);

        //head panel
        HeadPanel headPanel=new HeadPanel(contentPanel);
        add(headPanel,BorderLayout.NORTH);
    }

    public static void main(String[] args)throws Exception
    {
        UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        WebDictGUIRunner frame=new WebDictGUIRunner();
        frame.setTitle("WebDict");
        frame.setSize(FRAME_WIDTH,FRAME_HEIGHT);
        //frame.setLocation(200, 100);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

/**
 * Created by 77 on 2016/11/26.
 */
public class ContentPanel extends JPanel{
    private final Color myColor=new Color(39,154,235);//天蓝色
    private final Color textPaneColor=new Color(240, 240, 240);//textPane背景色

    private FlowLayout flowLayout=new FlowLayout(FlowLayout.LEFT,5,5);
    private JTextPane textPane=new JTextPane();
    private JPanel outlinePanel=new JPanel();
    //constructor
    public ContentPanel()
    {
        setLayout(flowLayout);
        //TextPanel

        textPane.setBackground(textPaneColor);
        textPane.setEditable(false);//set it uneditable
        //textPane.setBorder(new LineBorder(myColor,1));
		/*add textPane*/
		outlinePanel.setLayout(flowLayout);
        outlinePanel.add(textPane);
        outlinePanel.setBorder(new LineBorder(myColor,1));
        add(outlinePanel);
        //JScrollPane scrollPane=new JScrollPane(textPane);
        //add(scrollPane,BorderLayout.CENTER);
    }
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //System.out.println(getWidth());
        outlinePanel.setSize(new Dimension(getWidth()-2*flowLayout.getHgap(),getHeight()-2*flowLayout.getVgap()));
        textPane.setSize(new Dimension(outlinePanel.getWidth()-2*flowLayout.getHgap(),outlinePanel.getHeight()-2*flowLayout.getVgap()));
    }
}

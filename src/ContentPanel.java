import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;
import java.security.acl.Group;

/**
 * Created by 77 on 2016/11/26.
 */
public class ContentPanel extends JPanel{
    /*colors*/
    private final Color myColor=new Color(39,154,235);//天蓝色
    private final Color textPaneColor=new Color(240, 240, 240);//textPane背景色

    /*fonts*/
    private final Font smallFont=new Font("Serif",Font.ITALIC,18);//字体
    private final Font bigFont=new Font("SanSerif", Font.ITALIC, 25);//大字体

    /*components*/
    private FlowLayout flowLayout=new FlowLayout(FlowLayout.LEFT,5,5);
    private JTextPane textPane=new JTextPane();//the real display area.
    private JPanel outlinePanel=new JPanel();

    /*websites*/
    String[] websiteTitle={"百度","有道","金山"};
    boolean[] selectedItem=new boolean[3];//store items chosen by the user.
    public void setSelectedItem(int index,boolean isSelected)
    {
        selectedItem[index]=isSelected;
    }

    //constructor
    public ContentPanel()
    {
        setLayout(flowLayout);

        textPane.setBackground(textPaneColor);
        textPane.setEditable(false);//set it uneditable
        //textPane.setBorder(new LineBorder(myColor,1));
		/*add textPane*/
        //outlinePanel.setLayout(flowLayout);
        //outlinePanel.add(textPane);
        //outlinePanel.setBorder(new LineBorder(myColor,1));
        add(outlinePanel);

        /*copy from baidu,it realizes autoLineWrap of textPane*/
        GroupLayout panel_contentLayout = new GroupLayout(outlinePanel);
        outlinePanel.setLayout(panel_contentLayout);
        panel_contentLayout.setHorizontalGroup(
                panel_contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(textPane, 0, 1, Short.MAX_VALUE)
        );
        panel_contentLayout.setVerticalGroup(
                panel_contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(textPane)
        );

    }
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //System.out.println(getHeight());
        outlinePanel.setSize(new Dimension(getWidth()-2*flowLayout.getHgap(),getHeight()-2*flowLayout.getVgap()));
        textPane.setSize(new Dimension(outlinePanel.getWidth()-2*flowLayout.getHgap(),outlinePanel.getHeight()-2*flowLayout.getVgap()));
    }

    public void displayWordExplanations(String wordOrPhrase)
    {
        textPane.setText("");//清空textPane

        //to complete
        String[] explanations=getExplanations(wordOrPhrase);
        int[] displayOrder=getDisplayOrder(wordOrPhrase);

        //baidu,youdao and jinshan.
        for(int i=0;i<3;i++) {
            if(selectedItem[displayOrder[i]])
                printAWebsiteResultOnTextpane(websiteTitle[displayOrder[i]],wordOrPhrase,explanations[displayOrder[i]]);
        }
        textPane.setCaretPosition(0);//display from the first line
    }
    /* display a website result in textPane using font.*/
    private void printAWebsiteResultOnTextpane(String websiteTitle,String wordOrPhrase,String explanation)
    {
        String lineSeparator=System.getProperty("line.separator");

        SimpleAttributeSet attrset=new SimpleAttributeSet();
        Document document=textPane.getDocument();
        /*display websiteTitle*/
        StyleConstants.setFontSize(attrset, bigFont.getSize());
        StyleConstants.setFontFamily(attrset, bigFont.getFontName());
        StyleConstants.setForeground(attrset,Color.BLUE);
        try{
            document.insertString(document.getLength(), websiteTitle+lineSeparator, attrset);
        }catch (BadLocationException ble) {
            // TODO: handle exception
            System.out.println("BadLocationException:"+ble);
        }
        /*display wordOrPhrase and explanation*/
        StyleConstants.setFontSize(attrset, smallFont.getSize());
        StyleConstants.setFontFamily(attrset, smallFont.getFontName());
        StyleConstants.setForeground(attrset,Color.BLACK);
        try{
            document.insertString(document.getLength(), wordOrPhrase+lineSeparator+" "+explanation+lineSeparator, attrset);
        }catch (BadLocationException ble) {
            // TODO: handle exception
            System.out.println("BadLocationException:"+ble);
        }
    }

    /*the interface with the server*/
    //get the explanations of input from the server
    private String[] getExplanations(String wordOrPhrase)
    {
        //to complete!
        String[] explanations=new String[3];
        explanations[0]="sadddddddddddddddddddddfff fffffasdasasafffff sfaassssssssssssssssssssssssssssssssssss";
        //explanations[0]="我我我我我我我我我我我我我 我我我我我我我我我我 我我我我我我我我我我我我我我我";
        explanations[1]="youdao explanation\nA\nB";
        explanations[2]="jinshan explanation\nA\nB";
        return explanations;
    }
    //get the popularity of the WordOrPhrase of three websites.
    private int[] getPopularities(String wordOrPhrase)
    {
        //to complete
        int[] popularities=new int[3];
        popularities[0]=(int)(Math.random()*10);
        popularities[1]=(int)(Math.random()*10);
        popularities[2]=(int)(Math.random()*10);
        return popularities;
    }
    //get the display order of "baidu,youdao and jinshan" according to their popularity
    private int[] getDisplayOrder(String wordOrPhrase)
    {
        int[] popularities=getPopularities(wordOrPhrase);
        /*
        for(int popularity:popularities)
            System.out.print(popularity+" ");
        System.out.println();
        */
        int[] displayOrder=new int[3];
        boolean[] isUesd={false,false,false};

        for(int i=0;i<3;i++)
        {
            int maxIndex=-1;
            int maxValue=-1;
            for(int j=0;j<3;j++)
            {
                if (!isUesd[j] && popularities[j] > maxValue)
                {
                    maxIndex=j;
                    maxValue=popularities[maxIndex];
                }
            }
            displayOrder[i]=maxIndex;
            isUesd[maxIndex]=true;
        }

        /*
        for(int order:displayOrder)
            System.out.print(order+" ");
        System.out.println();
        */
        return displayOrder;
    }
}

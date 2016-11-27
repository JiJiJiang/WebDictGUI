import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;
import java.security.acl.Group;

/**
 * Created by 77 on 2016/11/26.
 */
public class ContentPanel extends JPanel{
    /*colors*/
    private final Color myColor=new Color(39,154,235);//天蓝色
    private final Color textPaneColor=new Color(242, 242, 242);//textPane背景色

    /*fonts*/
    private final Font smallFont=new Font("Serif",Font.ITALIC,18);//字体
    private final Font bigFont=new Font("SanSerif", Font.ITALIC, 22);//大字体

    /*components*/
    private FlowLayout flowLayout=new FlowLayout(FlowLayout.LEFT,5,5);
    private JTextPane textPane=new JTextPane();//the real display area.
    public JTextPane getTextPane()
    {
        return textPane;
    }
    private JScrollPane jScrollPane;//the outside JScrollPane
    int resetValue=0;//reset jScrollPane

    /*(un)fold imageIcons*/
    boolean[] isUnfold={true,true,true};//initial unfold
    private void renewIsUnfold()
    {
        for(int i=0;i<3;i++) isUnfold[i]=true;
    }
    ImageIcon foldIamgeIcon=new ImageIcon("image/fold.jpg");
    ImageIcon unfoldIamgeIcon=new ImageIcon("image/unfold.jpg");

    /*(dis)like imageIcons*/
    boolean[] isLike={false,false,false};//initial dislike
    private void renewIsLike()
    {
        for(int i=0;i<3;i++) isLike[i]=false;
    }
    ImageIcon likeImageIcon=new ImageIcon("image/like.jpg");
    ImageIcon dislikeImageIcon=new ImageIcon("image/dislike.jpg");

    /*from the server*/
    String curWordOrPhrase=null;
    String[] explanations;
    int[] displayOrder;

    /*websites*/
    String line="";
    String spaceContent="                                                             ";
    String[] websiteTitle={"百度","有道","金山"};
    boolean[] selectedItem={false,true,false};//store items chosen by the user.
    public void setSelectedItem(int index,boolean isSelected)
    {
        selectedItem[index]=isSelected;
        if(curWordOrPhrase!=null)
            displayWordExplanations(curWordOrPhrase);
    }

    //constructor
    public ContentPanel()
    {
        for(int i=0;i<40;i++) line+="_____________";
        textPane.setBackground(textPaneColor);
        textPane.setEditable(false);//set it uneditable
        //textPane.setBorder(new LineBorder(myColor,1));
		/*add textPane*/

        /*copy from baidu,it realizes autoLineWrap of textPane*/
        GroupLayout panel_contentLayout = new GroupLayout(this);
        setLayout(panel_contentLayout);
        panel_contentLayout.setAutoCreateContainerGaps(true);
        panel_contentLayout.setHorizontalGroup(
                panel_contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(textPane, 0, 250, Short.MAX_VALUE)
        );
        panel_contentLayout.setVerticalGroup(
                panel_contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(textPane)
        );

    }
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //jScrollPane.getVerticalScrollBar().setValue(resetValue);
        System.out.println(jScrollPane.getVerticalScrollBar().getValue());
    }
    public void setJScrollPane(JScrollPane jScrollPane) {
        this.jScrollPane = jScrollPane;
        JScrollBar vBar = this.jScrollPane.getVerticalScrollBar();
        vBar.addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                //System.out.println(e.getValue());
                //System.out.println(vBar.getMaximum());
                //if(e.getValue()==vBar.getValue())
                    ;
            }
        });
    }

    public void displayWordExplanations(String wordOrPhrase)
    {
        if(this.curWordOrPhrase!=wordOrPhrase)//first search this word
        {
            this.curWordOrPhrase=wordOrPhrase;
            explanations=getExplanations(curWordOrPhrase);
            displayOrder=getDisplayOrder(curWordOrPhrase);
            renewIsLike();
            renewIsUnfold();
        }
        textPane.setText("");//clear textPane
        textPane.removeAll();
        //textPane.updateUI();
        //textPane.repaint();

        //baidu,youdao and jinshan.
        for(int i=0;i<3;i++) {
            if (selectedItem[displayOrder[i]]) {
                try {
                    printAWebsiteResultOnTextpane(displayOrder[i],websiteTitle[displayOrder[i]],explanations[displayOrder[i]]);
                }catch (BadLocationException ble) {
                    // TODO: handle exception
                    System.out.println("BadLocationException:" + ble);
                }
            }
        }
    }
    /* display a website result in textPane using font.*/
    private void printAWebsiteResultOnTextpane(int index,String websiteTitle,String explanation)throws BadLocationException
    {
        String lineSeparator = System.getProperty("line.separator");

        SimpleAttributeSet attrset = new SimpleAttributeSet();
        Document document = textPane.getDocument();

        /*display websiteTitle*/
        StyleConstants.setFontSize(attrset, bigFont.getSize());
        StyleConstants.setFontFamily(attrset, bigFont.getFontName());
        StyleConstants.setForeground(attrset, myColor);
        document.insertString(document.getLength(), websiteTitle + spaceContent, attrset);

        /*like button*/
        JButton likeButton;
        if (isLike[index]) {
            likeButton = new JButton(likeImageIcon);
            likeButton.setPreferredSize(new Dimension(10, 22));
            likeButton.setToolTipText("取消");
        } else {
            likeButton = new JButton(dislikeImageIcon);
            likeButton.setPreferredSize(new Dimension(10, 22));
            likeButton.setToolTipText("点赞");
        }
        likeButton.setContentAreaFilled(false);
        likeButton.setFocusPainted(false);
        likeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isLike[index]=!isLike[index];
                displayWordExplanations(curWordOrPhrase);
            }
        });
        textPane.insertComponent(likeButton);

        /*fold button*/
        JButton foldButton;
        if (isUnfold[index]) {
            foldButton = new JButton(foldIamgeIcon);
            foldButton.setPreferredSize(new Dimension(10, 22));
            foldButton.setToolTipText("收起");
        } else {
            foldButton = new JButton(unfoldIamgeIcon);
            foldButton.setPreferredSize(new Dimension(10, 22));
            foldButton.setToolTipText("展开");
        }
        foldButton.setContentAreaFilled(false);
        foldButton.setFocusPainted(false);
        foldButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                isUnfold[index]=!isUnfold[index];
                displayWordExplanations(curWordOrPhrase);
            }
        });
        textPane.insertComponent(foldButton);

        /*line*/
        StyleConstants.setFontSize(attrset, 2);
        document.insertString(document.getLength(), lineSeparator + line + lineSeparator, attrset);

        if (isUnfold[index]) {
        /*display wordOrPhrase */
            StyleConstants.setFontSize(attrset, smallFont.getSize());
            StyleConstants.setFontFamily(attrset, smallFont.getFontName());
            StyleConstants.setForeground(attrset, Color.RED);
            document.insertString(document.getLength(), curWordOrPhrase + lineSeparator, attrset);

        /*display explanation*/
            StyleConstants.setForeground(attrset, Color.BLACK);
            document.insertString(document.getLength(), explanation + lineSeparator, attrset);
        }
        StyleConstants.setFontSize(attrset, smallFont.getSize());
        document.insertString(document.getLength(),lineSeparator, attrset);
    }

    /*the interface with the server*/
    //get the explanations of input from the server
    private String[] getExplanations(String wordOrPhrase)
    {
        //to complete!
        String[] explanations=new String[3];
        //explanations[0]="sadddddddddddddddddddddfff fffffasdasasafffff sfaassssssssssssssssssssssssssssssssssss";
        //explanations[0]="我我我我我我我我我我我我我 我我我我我我我我我我 我我我我我我我我我我我我我我我";
        explanations[0]="   baidu explanation\n   start\n\n   end";
        explanations[1]="   youdao explanation\n   start\n\n   end";
        explanations[2]="   jinshan explanation\n   start\n\n   end";
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

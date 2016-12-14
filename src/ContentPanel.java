import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.*;
import java.util.*;
import org.json.*;

/**
 * Created by 77 on 2016/11/26.
 */
public class ContentPanel extends JPanel{
    /*colors*/
    private final Color myColor=new Color(39,154,235);//天蓝色
    private final Color textPaneColor=new Color(242, 242, 242);//textPane背景色

    /*fonts*/
    private final Font smallFont=new Font("Serif",Font.BOLD,16);//小字体
    private final Font middleFont=new Font("Serif",Font.ITALIC,18);//中字体
    private final Font bigFont=new Font("SanSerif", Font.ITALIC, 22);//大字体

    /*components*/
    private FlowLayout flowLayout=new FlowLayout(FlowLayout.LEFT,5,5);
    private JTextPane textPane=new JTextPane();//the real display area.
    public JTextPane getTextPane()
    {
        return textPane;
    }
    private JScrollPane jScrollPane;//the outside JScrollPane
    boolean isToResetZero=false;//need to resetCaretPosition
    int preWidth=0;//the previous width of contentPane
    int resetCaretPosition=0;//reset jScrollPane
    int[] resetLikeCaretPositions={0,0,0};//record three possible like reset positions
    int[] resetFoldCaretPositions={0,0,0};//record three possible fold reset positions
    private void renewResetCaretPositions()
    {
        resetCaretPosition=0;
        for(int i=0;i<3;i++) {
            resetLikeCaretPositions[i]=resetFoldCaretPositions[i]=0;
        }
    }

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
    String line;
    String spaceContent;
    String[] websiteTitle={"百度","有道","金山"};
    boolean[] selectedItem={true,true,true};//store items chosen by the user.
    public void setSelectedItem(int index,boolean isSelected)
    {
        selectedItem[index]=isSelected;
        if(curWordOrPhrase!=null) {
            renewResetCaretPositions();
            displayWordExplanations(curWordOrPhrase);
        }
    }

    //constructor
    public ContentPanel()
    {
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
        //redraw the blue line
        if(curWordOrPhrase!=null&&preWidth!=jScrollPane.getWidth())
        {
            //reset the length of line and spaceContent
            line="";
            for(int i=0;i<textPane.getWidth()-25;i++) line+="_";
            spaceContent="";
            for(int i=0;i<textPane.getWidth()-175;i++) spaceContent+=" ";
            isToResetZero=true;
            displayWordExplanations(curWordOrPhrase);
            isToResetZero=false;
            preWidth=jScrollPane.getWidth();
        }
    }

    public void setJScrollPane(JScrollPane jScrollPane) {
        this.jScrollPane = jScrollPane;
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
            renewResetCaretPositions();
        }
        textPane.setText("");//clear textPane
        //textPane.removeAll();

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
        textPane.setCaretPosition(isToResetZero?0:resetCaretPosition);
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
        document.insertString(document.getLength(), websiteTitle, attrset);

        /*spaceContent*/
        StyleConstants.setFontSize(attrset, 2);
        document.insertString(document.getLength(), spaceContent, attrset);

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
                if(isLike[index])
                    System.out.println("取消点赞");
                else
                    System.out.println("点赞");
                isLike[index]=!isLike[index];
                resetCaretPosition=resetLikeCaretPositions[index];
                displayWordExplanations(curWordOrPhrase);
            }
        });
        textPane.insertComponent(likeButton);
        resetLikeCaretPositions[index]=textPane.getCaretPosition();

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
                if(isUnfold[index])
                    System.out.println("收起");
                else
                    System.out.println("展开");
                isUnfold[index]=!isUnfold[index];
                resetCaretPosition=resetFoldCaretPositions[index];
                displayWordExplanations(curWordOrPhrase);
            }
        });
        textPane.insertComponent(foldButton);
        resetFoldCaretPositions[index]=textPane.getCaretPosition();

        /*line*/
        StyleConstants.setFontSize(attrset, 2);
        document.insertString(document.getLength(), lineSeparator + line + lineSeparator, attrset);

        if (isUnfold[index]) {
        /*display wordOrPhrase */
            StyleConstants.setFontSize(attrset, middleFont.getSize());
            StyleConstants.setFontFamily(attrset, middleFont.getFontName());
            StyleConstants.setForeground(attrset, Color.RED);
            document.insertString(document.getLength(), curWordOrPhrase + lineSeparator, attrset);

        /*display explanation*/
            StyleConstants.setFontSize(attrset, smallFont.getSize());
            StyleConstants.setFontFamily(attrset, smallFont.getFontName());
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
        URL url=null;
        Scanner input=null;
        String jsonResult="";
        try{
            url=new URL("http://115.159.0.12:8080/q?word="+wordOrPhrase+"&baidu=true&youdao=true&jinshan=true");
            input=new Scanner(url.openStream());
            while(input.hasNextLine()) {
                jsonResult+=input.nextLine();
            }
            System.out.println(jsonResult);
        }
        catch(MalformedURLException ex)
        {
            System.out.println("无法打开URL");
        }
        catch(IOException ex)
        {
            System.out.println("无法打开URL");
        }
        finally{
            input.close();
        }

        String[] explanations = new String[3];
        JSONObject all=new JSONObject(jsonResult);
        String jsonWord=all.getString("word");
        if(jsonWord.equals(wordOrPhrase)) {
            JSONArray jsonExplanations=all.getJSONArray("explanations");
            for (int i = 0; i < 3; i++) {
                JSONObject jsonObject=jsonExplanations.getJSONObject(i);
                String status=jsonObject.getString("status");
                if(status.equals("success"))
                {
                    String enPhonetic=jsonObject.getString("enPhonetic");
                    String usPhonetic=jsonObject.getString("usPhonetic");
                    String translation=jsonObject.getString("translation");
                    explanations[i]="英 ["+enPhonetic+"]\n美 ["+usPhonetic+"]\n译: "+translation;
                }
            }
        }

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

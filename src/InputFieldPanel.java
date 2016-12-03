import javax.swing.border.Border;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by 77 on 2016/11/26.
 */
public class InputFieldPanel extends JPanel{
    private final int MAX_ROW_COUNT=6;//下拉框最大显示的行数
    //private final int MAX_COUNT=10;

    private final Color myColor=new Color(39,154,235);//天蓝色
    private final Font font=new Font("Serif",Font.ITALIC,15);//字体

    private JTextField textInput = new JTextField(45);

    private ContentPanel contentPanel;//use it to display the result
    //constructor
    public InputFieldPanel(ContentPanel contentPanel)
    {
        this.contentPanel=contentPanel;
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(super.getPreferredSize().width,34));

        //JTextField textInput = new JTextField(45);
        textInput.setBackground(Color.WHITE);
        textInput.setForeground(Color.BLACK);
        textInput.setFont(font);
        //set border invisible
        Border textInputBorder=new LineBorder(Color.WHITE,0);
        textInput.setBorder(textInputBorder);
        //set up a combo box
        final DefaultComboBoxModel<String> model=new DefaultComboBoxModel<String>();
        final JComboBox<String> cbInput = new JComboBox<String>(model) {
            public Dimension getPreferredSize() {
                return new Dimension(super.getPreferredSize().width, 0);
            }
        };
        cbInput.setMaximumRowCount(MAX_ROW_COUNT);
        cbInput.setFont(font);
        //hide cbInput under textInput
        textInput.setLayout(new BorderLayout());
        textInput.add(cbInput, BorderLayout.SOUTH);

        /*set up a innerPanel to put textInput*/
        JPanel innerPanel=new JPanel(new FlowLayout(FlowLayout.LEFT,6,6));
        innerPanel.add(textInput);
        innerPanel.setBackground(Color.WHITE);
        Border textPanelBorder=new LineBorder(myColor,1);
        innerPanel.setBorder(textPanelBorder);
        /*set up a searchButton*/
        final ImageIcon searchIcon=new ImageIcon("image/searchButton.jpg");
        JButton searchButton=new JButton(searchIcon);
        final ImageIcon searchRolloverIcon=new ImageIcon("image/searchButtonRollover.jpg");
        searchButton.setRolloverIcon(searchRolloverIcon);
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(true);
        searchButton.setBorder(new LineBorder(Color.WHITE,1));
        //searchButton.setPreferredSize(new Dimension(100,super.getPreferredSize().height));
        searchButton.setFocusPainted(false);
        ToolTipManager.sharedInstance().setDismissDelay(10000);//display 10s
        searchButton.setToolTipText("点击搜索！");

        add(innerPanel,BorderLayout.CENTER);
        add(searchButton,BorderLayout.EAST);
        //setBackground(myColor);

        /*add all listeners here!*/
        //textField Input
        textInput.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e)
            {
                //System.out.println("insert");
                updateList();
            }
            public void removeUpdate(DocumentEvent e)
            {
                //System.out.println("remove");
                updateList();
            }
            public void changedUpdate(DocumentEvent e)
            {
                //System.out.println("change");
                updateList();
            }
            //更新下拉框的内容
            private void updateList()
            {
                setAdjusting(cbInput, true);//set cbInput changeable
                model.removeAllElements();
                String input = textInput.getText().trim();//delete the space at both ends
                if (!input.isEmpty())
                {
                    //get all associational words from the server
                    String[] associationalWords=getAssocitionalWords(input);
                    for(String word:associationalWords)
                        model.addElement(word);
                }

                boolean isACompleteWord=false;//modify it!!!
                if(!isACompleteWord) {
                    cbInput.setSelectedItem(null);
                    contentPanel.getTextPane().setText("");//clear extPane
                    //contentPanel.getTextPane().removeAll();
                }
                cbInput.setPopupVisible(false);//set cbInput invisible
                if(model.getSize() > 0)
                {
                    cbInput.setPopupVisible(true);
                }
                setAdjusting(cbInput, false);//关闭修改下拉框
            }
        });
        //keyboard Input
        textInput.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e)
            {
                setAdjusting(cbInput, true);
                if (e.getKeyCode() == KeyEvent.VK_ENTER
                        || e.getKeyCode() == KeyEvent.VK_UP
                        || e.getKeyCode() == KeyEvent.VK_DOWN)
                {
                    e.setSource(cbInput);
                    cbInput.dispatchEvent(e);
                    if (e.getKeyCode() == KeyEvent.VK_ENTER)
                    {
                        //put the content of the item into the textField!
                        //display in textInput and textPane
                        if(cbInput.getSelectedIndex()>=0)//matched item in cbInput
                        {
                            String[] tokens=cbInput.getSelectedItem().toString().split("\t");
                            textInput.setText(tokens[0]);//textInput
                            //textPane
                            contentPanel.displayWordExplanations(tokens[0]);

                        }
                        else//unmatch any item in cbInput,print error message.
                        {
                            String errorWordOrPhrase=textInput.getText();
                            if(errorWordOrPhrase.trim().length()!=0)
                            {
                                /*
                                if(correctionItem.size()==0)
                                    handleSearchResult(textPane,true,errorWordOrPhrase,"没有找到\""+errorWordOrPhrase+"\"相关的英汉翻译结果");//textPane
                                else
                                {
                                    handleSearchResult(textPane,true,errorWordOrPhrase,"");//textPane
                                    printCorrectionItem(textPane);
                                }
                                */
                            }
                        }
                        cbInput.setPopupVisible(false);//将下拉框置为不可见
                    }
                    else//UP和DOWN
                    {
                        if(model.getSize()>0&&cbInput.isPopupVisible())//下拉框中有推荐项且是可见的
                        {
                            if(cbInput.getSelectedItem()!=null)//有已选项
                            {
                                String[] tokens=cbInput.getSelectedItem().toString().split("\t");
                                //textInput.setText(tokens[0]);//textInput
                                //handleSearchResult(textPane,false,tokens[0],tokens[1].trim());//textPane
                            }
                        }
                    }
                }
                setAdjusting(cbInput, false);//关闭修改下拉框
            }
        });

        //cbInput
        cbInput.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (!isAdjusting(cbInput))
                {
                    //System.out.println("BBB");
                    if (cbInput.getSelectedItem() != null)
                    {
                        //textInput,textPane
                        String[] tokens=cbInput.getSelectedItem().toString().split("\t");
                        textInput.setText(tokens[0]);//textInput
                        contentPanel.displayWordExplanations(tokens[0]);//textPane
                    }
                }
            }
        });

        /*searchButton*/
        searchButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!isAdjusting(cbInput))
                {
                    if(cbInput.getSelectedIndex()>=0)//matched item in cbInput
                    {
                        //display in textInput,textPane
                        String[] tokens=cbInput.getSelectedItem().toString().split("\t");
                        textInput.setText(tokens[0]);//textInput
                        contentPanel.displayWordExplanations(tokens[0]);//textPane
                    }
                    else
                    {
                        String errorWordOrPhrase=textInput.getText();
                        if(errorWordOrPhrase.trim().length()!=0)
                        {
                            /*
                            if(correctionItem.size()==0)
                                handleSearchResult(textPane,true,errorWordOrPhrase,"没有找到\""+errorWordOrPhrase+"\"相关的英汉翻译结果");//textPane
                            else
                            {
                                handleSearchResult(textPane,true,errorWordOrPhrase,"");//textPane
                                printCorrectionItem(textPane);
                            }
                            */
                        }
                    }
                    cbInput.setPopupVisible(false);
                }
            }
        });
    }


    /*judge whether cbInput is being used.*/
    private static boolean isAdjusting(JComboBox<String> cbInput)
    {
        if (cbInput.getClientProperty("is_adjusting") instanceof Boolean)
            return (Boolean) cbInput.getClientProperty("is_adjusting");
        return false;
    }
    /*set the cbInput changeable.*/
    private static void setAdjusting(JComboBox<String> cbInput, boolean adjusting)
    {
        cbInput.putClientProperty("is_adjusting", adjusting);
    }

    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //System.out.println(getWidth());
        textInput.setSize(new Dimension(getWidth()-100-6*2,textInput.getHeight()));
    }

    /*the interface with the server*/
    //get the associational words from the server
    String[] getAssocitionalWords(String input)
    {
        //to complete
        String[] words=new String[6];
        words[0]="baidu-associational-word1";
        words[1]="baidu-associational-word2";
        words[2]="youdao-associational-word1";
        words[3]="youdao-associational-word2";
        words[4]="jinshan-associational-word1";
        words[5]="jinshan-associational-word2";
        return words;
    }
}

import javax.swing.border.Border;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Created by 77 on 2016/11/26.
 */
public class InputFieldPanel extends JPanel{
    private final int MAX_ROW_COUNT=5;//下拉框最大显示的行数
    //private final int MAX_COUNT=10;

    private final Color myColor=new Color(39,154,235);//天蓝色
    private final Font font=new Font("Serif",Font.ITALIC,15);//字体

    private JTextField textInput = new JTextField(45);
    //constructor
    public InputFieldPanel(ContentPanel contentPanel)
    {
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
                    //从服务器获取单词查询结果
                    String[] searchResult=get(input);
                    for(String str:searchResult)
                        model.addElement(str);
                }

                cbInput.setPopupVisible(false);//关闭下拉框
                if(model.getSize() > 0)//重新显示下拉框
                {
                    cbInput.setPopupVisible(true);
                }
                setAdjusting(cbInput, false);//关闭修改下拉框
            }
        });
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
                            //handleSearchResult(textPane,true,tokens[0],tokens[1].trim());//textPane
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
    //get the explanations of input from the server
    String[] get(String input)
    {
        //to complete!
        String[] result=new String[3];
        result[0]="baidu";
        result[1]="youdao";
        result[2]="jinshan";
        return result;
    }
}

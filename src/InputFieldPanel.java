import javax.swing.border.Border;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

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
    public InputFieldPanel()
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
  /*    //set up a combo box
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
*/
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
        //searchButton.setFocusable(true);
        searchButton.setFocusPainted(false);
        ToolTipManager.sharedInstance().setDismissDelay(10000);//display 10s
        searchButton.setToolTipText("点击搜索！");

        add(innerPanel,BorderLayout.CENTER);
        add(searchButton,BorderLayout.EAST);
        //setBackground(myColor);

        /*add all listeners here!*/
    }
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        //System.out.println(getWidth());
        textInput.setSize(new Dimension(getWidth()-100-6*2,textInput.getHeight()));
    }
}

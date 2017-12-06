

import com.raileurope.app.KeysI;
import com.raileurope.app.LaunchOption;
import com.raileurope.app.TestMain;

import org.apache.commons.exec.launcher.CommandLauncherImpl;

import javax.imageio.ImageIO;

import java.awt.AlphaComposite;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileSystemView;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class WinFrame  {
    public WinFrame(){
        SwingUtilities.invokeLater( new Runnable(){ public void run() {  new Win().createUI();  } } );
    }

    //public static void main(String[] args) {
    //    new WinFrame();
    //}
}

final class Win extends JFrame {
    final static String HEADING = "Automated RailTest Selenium Runner";
    final static String LABEL_XML_FILE = "Test XML File";
    final static String LABEL_DOMAIN = "Domain";
    final static String FAVICON_ICO = "/favicon.ico";
    final static String FONT_BOLD = "/OpenSansCondensed-Bold.ttf";
    final static String FONT_REGULAR = "/OpenSansCondensed-Light.ttf";
    final static String GC_1 = "/gc.png";
    final static String GC_2 = "/gc1.png";
    final static String FF_1 = "/ff.png";
    final static String FF_2 = "/ff1.png";
    final static String IE_1 = "/ie.png";
    final static String IE_2 = "/ie1.png";

    JButton _buttonGC;
    JButton _buttonFF;
    JButton _buttonIE;
    JButton _browseXmlFile;
    JTextField _xmlConfigFileTxt;

    JPanel _configPane;
    Container _contentPane;
    JComboBox _domain;

    LaunchOption.CLI _cli;

    public void createUI(){
        _cli = new LaunchOption.CLI(new String[]{});

        setSize(600,350);
        setResizable(false);
        setTitle(HEADING);
        setFrameIcon();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        loadComponents();
        this.setFont(getGFont(15f, false));
        setVisible(true);
    }

    private void loadComponents(){
        initComponent();

        JPanel jp = new JPanel();
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        jp.setLayout(gb);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 1;
        c.weightx = 0.5;

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10,10,10,10);  //top padding

        JLabel label = new JLabel(HEADING);
        Font font1 = getGFont(20, true);
        label.setFont(font1);

        gb.setConstraints(label, c);
        jp.add(label);

        c.ipady = 5;
        c.weightx = 0.5;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(5,5,5,5);  //top padding

        JSeparator separator = new JSeparator(SwingConstants.HORIZONTAL);

        gb.setConstraints(separator, c);
        jp.add(separator);

        getContentPane().setLayout(new BorderLayout());

        getContentPane().add(jp, BorderLayout.NORTH);
        getContentPane().add(getActionComponent(), BorderLayout.CENTER);
    }

    private Font getGFont(float size, boolean bold){
        try {
            InputStream is = Win.class.getResourceAsStream(bold ? FONT_BOLD : FONT_REGULAR);

            Font font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(Font.PLAIN, size);

            return font;
        } catch (Exception exp) {
            exp.printStackTrace();
        }

        return  new Font("Courier", bold ? Font.BOLD : Font.PLAIN, (int)size);
    }

    private void initComponent() {
        _contentPane = this.getContentPane();
        _configPane = new JPanel();
        //_configPane.setLayout(new GridLayout(2,2));

        _xmlConfigFileTxt = new JTextField(20);
        Font bigFont = getGFont(18f, false);//_xmlConfigFileTxt.getFont().deriveFont(Font.PLAIN, 18f);
        _xmlConfigFileTxt.setFont(bigFont);

        JLabel headerLabel = new JLabel(LABEL_XML_FILE);
        JLabel domainLabel = new JLabel(LABEL_DOMAIN);
        headerLabel.setFont(getGFont(18f, false));
        domainLabel.setFont(getGFont(18f, false));

        _domain = new JComboBox();
        _domain.setFont(bigFont);
        for(String option : LaunchOption.DOMAIN.getValidOptions()){
            _domain.addItem(option);
        }

        _xmlConfigFileTxt.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                showFileDialog();
            }
        });

        _xmlConfigFileTxt.setFont(bigFont);
        _configPane.add(headerLabel);
        _configPane.add(_xmlConfigFileTxt);
        _configPane.add(domainLabel);
        _configPane.add(_domain);

        _buttonGC = new JButton();
        this.setIcon(_buttonGC, GC_1, GC_2);
        _buttonGC.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runTest(KeysI.BROWSER_GC);
            }
        });

        _buttonFF = new JButton();
        this.setIcon(_buttonFF, FF_1, FF_2);
        _buttonFF.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runTest(KeysI.BROWSER_FF);
            }
        });

        _buttonIE = new JButton();
        this.setIcon(_buttonIE, IE_1, IE_2);
        _buttonIE.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                runTest(KeysI.BROWSER_IE);
            }
        });

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public final void showFileDialog() {
        JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getDefaultDirectory());
        File workingDirectory = new File(System.getProperty("user.dir"));
        jfc.setCurrentDirectory(workingDirectory);

        int returnValue = jfc.showOpenDialog(null);
        // int returnValue = jfc.showSaveDialog(null);

        if (returnValue == JFileChooser.APPROVE_OPTION) {
            File selectedFile = jfc.getSelectedFile();
            _xmlConfigFileTxt.setText( selectedFile.getAbsolutePath() );
        }
    }

    private final void runTest(String browserName){
        _cli.setBrowserName(browserName);
        String domain = (String) _domain.getSelectedItem();
        if(!StringUtils.isEmpty(domain)) {
            _cli.setDomain(domain);
        }

        String testXmlFile = _xmlConfigFileTxt.getText();
        if(!StringUtils.isEmpty(testXmlFile)) {
            _cli.setTextCasePath(testXmlFile);
        }

        final Component jc = this;
        SwingUtilities.invokeLater(
                new Runnable(){
                    public void run() {
                        try {
                            TestMain test = new TestMain(_cli);
                            test.execute();
                        }catch(Exception ex){
                            ex.printStackTrace();
                            Utils.showDialog(jc, ex.getMessage());
                        }
                        finally {
                            Utils.showReport();
                        }
                    }
                } );

    }

    private void setFrameIcon(){
        try {
            Image img = ImageIO.read(getClass().getResource(FAVICON_ICO));
            ImageIcon icon = new ImageIcon(img);
            this.setIconImage(img);
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private void setIcon(JButton button, String iconFullPath, String hoverIconFullPath){
        try {
            Image img = ImageIO.read(getClass().getResource(iconFullPath));
            Image img1 = ImageIO.read(getClass().getResource(hoverIconFullPath));
            ImageIcon icon = new ImageIcon(img);
            ImageIcon icon1 = new ImageIcon(img1);

            button.setIcon(  (icon) );

            button.setBorderPainted(false);
            button.setBorder(null);
            //button.setFocusable(false);
            button.setFocusPainted(false);
            button.setMargin(new Insets(0, 0, 0, 0));
            button.setContentAreaFilled(false);
            button.setRolloverIcon(icon1 );
            //button.setPressedIcon(myIcon3);

        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private Icon getGray(Icon icon) {
        final int w = icon.getIconWidth();
        final int h = icon.getIconHeight();
        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        BufferedImage image = gc.createCompatibleImage(w, h);
        Graphics2D g2d = image.createGraphics();
        g2d.setPaint(new Color(0x00f0f0f0));
        g2d.fillRect(0, 0, w, h);
        icon.paintIcon(null, g2d, 0, 0);
        BufferedImage gray = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(
                image.getColorModel().getColorSpace(),
                gray.getColorModel().getColorSpace(), null);
        op.filter(image, gray);
        return new ImageIcon(gray);
    }

    private static ImageIcon dye(ImageIcon icon, Color color) {
        Image img = icon.getImage();

        BufferedImage image = (BufferedImage) img;
        int w = image.getWidth();
        int h = image.getHeight();
        BufferedImage dyed = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = dyed.createGraphics();
        g.drawImage(image, 0,0, null);
        g.setComposite(AlphaComposite.SrcAtop);
        g.setColor(color);
        g.fillRect(0,0,w,h);
        g.dispose();

        return new ImageIcon(dyed);
    }

    private JPanel getActionComponent0(){
        //initComponent();

        JPanel contentPane = new JPanel();
        //contentPane.setBorder(new BevelBorder(BevelBorder.RAISED));
        GridBagLayout gridbag = new GridBagLayout();

        GridBagConstraints c = new GridBagConstraints();
        contentPane.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 00;
        c.weightx = 0.5;

        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        gridbag.setConstraints(_configPane, c);
        contentPane.add(_configPane);

        JLabel runLabel = new JLabel("Run Test:");
        c.gridx = 0;
        c.gridy = 2;
        gridbag.setConstraints(runLabel, c);
        contentPane.add(runLabel);

        JLabel runLabel1 = new JLabel("Run Test1:");
        c.gridx = 0;
        c.gridy = 3;
        gridbag.setConstraints(runLabel1, c);
        contentPane.add(runLabel1);

        c.ipadx = 00;
        c.gridx = 0;
        c.gridy = 4;
        //c.insets = new Insets(10,10,10,10);  //top padding
        c.weightx = 0.5;
        gridbag.setConstraints(_buttonGC, c);
        contentPane.add(_buttonGC);



        return contentPane;
    }


    private JPanel getActionComponent(){
        initComponent();

        JPanel contentPane = new JPanel();
        //contentPane.setBorder(new BevelBorder(BevelBorder.RAISED));
        GridBagLayout gridbag = new GridBagLayout();

        GridBagConstraints c = new GridBagConstraints();
        contentPane.setLayout(gridbag);
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 40;
        c.weightx = 0.5;

        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(10,10,10,10);  //top padding
        gridbag.setConstraints(_buttonGC, c);
        contentPane.add(_buttonGC);

        c.gridx = 1;
        c.gridy = 1;
        gridbag.setConstraints(_buttonFF, c);
        contentPane.add(_buttonFF);

        c.gridx = 2;
        c.gridy = 1;
        gridbag.setConstraints(_buttonIE, c);
        contentPane.add(_buttonIE);


        c.ipady = 40;      //make this component tall
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        gridbag.setConstraints(_configPane, c);
        contentPane.add(_configPane);

        JLabel runLabel = new JLabel("Click on the Browser to Start the Automated Testing");
        //c.ipady = 40;      //make this component tall
        //c.weightx = 0.0;
        //c.gridwidth = 3;

        c.gridx = 0;
        c.gridy = 3;
        gridbag.setConstraints(runLabel, c);
        contentPane.add(runLabel);



        return contentPane;
    }
}
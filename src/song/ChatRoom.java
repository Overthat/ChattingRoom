package song;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.FileDialog;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.WindowConstants;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * 聊天室主类
 */
public class ChatRoom {
    static final String IP = "127.0.0.1";
    static final int PORT = 4700;

    public static void main(String[] args) {
        System.out.println("欢迎进入简易聊天室!");
        // 创建客户端GUI
        new ClientWin();
    }
}

/**
 * 客户端GUI类
 */
class ClientWin extends JFrame {
    public String name; // 用户名称
    public String text; // 输入文本
    private ClientLoginDia dia; // 登录对话框
    public JTextArea inputText; // 消息输入文本框
    public JTextArea outputText; // 消息显示文本框
    public JMenu menu1; // 菜单一:读取聊天记录
    public JMenu menu2; // 菜单二:保存聊天记录
    public JMenu menu3; // 菜单三:登录
    private transient TalkClient client; // 与网络通信交互的对象
    private FileDialog fd; // 文件读取对话框
    private FileDialog fw; // 文件写入对话框

    // 构造方法,初始化各GUI部件
    public ClientWin() {
        // 登录对话框
        dia = new ClientLoginDia(this);
        // 设置窗口
        setBounds(400, 300, 400, 400);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("简易聊天室");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        // 注册额外退出行为,关闭client
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                client.close();
            }
        });

        // 设置菜单
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);

        menu1 = new JMenu("读取聊天记录");
        menubar.add(menu1);
        fd = new FileDialog(this, "open", FileDialog.LOAD);
        menu1.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                fd.setVisible(true);
                try (BufferedReader f = new BufferedReader(new FileReader(fd.getDirectory() + fd.getFile()))) {
                    String line;
                    outputText.setText("");
                    while ((line = f.readLine()) != null) {
                        outputText.append(line + "\n");
                    }
                } catch (IOException ie) {
                    // Todo
                }
            }

            public void menuCanceled(MenuEvent e) {
                // pass
            }

            public void menuDeselected(MenuEvent e) {
                // pass
            }
        });

        menu2 = new JMenu("保存聊天记录");
        menubar.add(menu2);
        fw = new FileDialog(this, "save", FileDialog.SAVE);
        menu2.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                fw.setVisible(true);
                try (BufferedWriter f = new BufferedWriter(new FileWriter(fw.getDirectory() + fw.getFile()))) {
                    f.write(outputText.getText());
                } catch (IOException ie) {
                    // Todo
                }
            }

            public void menuCanceled(MenuEvent e) {
                // pass
            }

            public void menuDeselected(MenuEvent e) {
                // pass
            }
        });

        menu3 = new JMenu("登录");
        menubar.add(menu3);
        menu3.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                dia.setVisible(true);
            }

            public void menuCanceled(MenuEvent e) {
                // pass
            }

            public void menuDeselected(MenuEvent e) {
                // pass
            }
        });

        // 设置聊天框
        inputText = new JTextArea(10, 20);
        JPanel panel = new JPanel();
        panel.add(inputText);
        panel.setBackground(Color.lightGray);

        outputText = new JTextArea(10, 20);
        outputText.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputText, ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,
                ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        // 设置发送按钮
        JButton button = new JButton("发送");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Calendar time = Calendar.getInstance();
                String t = String.format("%tT", time);
                if (inputText.getText().length() != 0) {
                    setText(name + "@" + t + "\n" + inputText.getText() + "\n");
                    inputText.setText("");
                    myNotify();
                }
            }
        });

        contentPane.add(scrollPane, BorderLayout.NORTH);
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(button, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 加载网络通信客户端
    public void loadNetClient(String ip, int port) {
        client = new TalkClient(ip, port, this);
        client.start();
    }

    // 从客户端加载消息
    public void loadComments(String s) {
        outputText.append(s);
    }

    // 设置name
    public void setMyName(String name) {
        this.name = name;
    }

    // 设置text
    private void setText(String text) {
        this.text = text;
    }

    // 读取文本的阻塞方法
    public synchronized String getText() throws InterruptedException {
        while (true) {
            if (text != null && !text.equals("")) {
                break;
            }
            wait();
        }
        String msg = text;
        text = null;
        return msg;
    }

    // 解除阻塞
    private synchronized void myNotify() {
        notify();
    }
}

/**
 * 登录对话框类
 */
class ClientLoginDia extends JDialog {

    // 构造方法,并初始化
    public ClientLoginDia(ClientWin jFrame) {

        super(jFrame, "连接至聊天室", true);
        Container container = getContentPane();

        var tf1 = new JTextField(ChatRoom.IP);
        var tf2 = new JTextField(String.valueOf(ChatRoom.PORT));
        var tf3 = new JTextField("your name");
        tf1.setBackground(Color.lightGray);
        tf1.addMouseListener(new MyMouseListener());
        tf2.setBackground(Color.lightGray);
        tf2.addMouseListener(new MyMouseListener());
        tf3.setBackground(Color.lightGray);
        tf3.addMouseListener(new MyMouseListener());
        var panel = new JPanel();
        panel.setLayout(new GridLayout(1, 3));
        panel.add(tf1);
        panel.add(tf2);
        panel.add(tf3);

        var b = new JButton("连接");
        b.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String s1 = tf1.getText();
                String s2 = tf2.getText();
                String s3 = tf3.getText();
                if (!s1.equals("") && !s2.equals("") && !s3.equals("")) {
                    jFrame.setMyName(s3);
                    jFrame.menu3.setEnabled(false);
                    // 按下连接按钮,调用loadNetClient方法创建网络客户端
                    jFrame.loadNetClient(s1, Integer.parseInt(s2));
                    setVisible(false);
                }
            }
        });

        container.setLayout(new BorderLayout());
        container.add(panel, BorderLayout.CENTER);
        container.add(b, BorderLayout.EAST);
        setBounds(450, 400, 350, 70);
    }

}

/**
 * 鼠标点击事件实现类
 */
class MyMouseListener implements MouseListener {
    public void mouseClicked(MouseEvent e) {
        JTextField tf = (JTextField) e.getSource();
        tf.setText("");
    }

    public void mousePressed(MouseEvent e) {
        // pass
    }

    public void mouseReleased(MouseEvent e) {
        // pass
    }

    public void mouseEntered(MouseEvent e) {
        // pass
    }

    public void mouseExited(MouseEvent e) {
        // pass
    }
}

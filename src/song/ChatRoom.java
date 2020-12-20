package song;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;

import java.util.*;

public class ChatRoom {
    static final String IP1 = "172.16.122.59";
    static final String IP2 = "127.0.0.1";
    static final String IP3 = "192.168.1.103";
    static final int PORT = 4700;

    public static void main(String[] args) {
        System.out.println("欢迎进入简易聊天室!");
        var cw = new ClientWin();
    }
}

class ClientWin extends JFrame {
    public String name;
    private ClientLoginDia dia;
    private TalkClient client;
    public JTextArea inputText;
    public JTextArea outputText;
    public JMenu menu2;

    public ClientWin() {
        dia = new ClientLoginDia(this);
        // 设置窗口
        setBounds(400, 300, 400, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("简易聊天室");
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        // 设置菜单
        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu menu1 = new JMenu("聊天记录");
        menubar.add(menu1);
        menu2 = new JMenu("登录");
        menubar.add(menu2);
        menu2.addMenuListener(new MenuListener() {
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
        JMenuItem item1 = new JMenuItem("读取上次聊天记录");
        menu1.add(item1);
        JMenuItem item2 = new JMenuItem("保存聊天记录");
        menu1.add(item2);

        // 设置聊天框
        inputText = new JTextArea(10, 20);
        outputText = new JTextArea(10, 20);
        outputText.setEditable(false);
        JButton button = new JButton("发送");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                Calendar time = Calendar.getInstance();
                String t = String.format("%tT", time);
                if (inputText.getText().length() != 0) {
                    outputText.append("QAQ: " + t + "\n" + inputText.getText() + "\n");
                    inputText.setText("");
                }
            }
        });
        JPanel panel = new JPanel();
        panel.add(inputText);
        panel.setBackground(Color.lightGray);

        contentPane.add(outputText, BorderLayout.NORTH);
        contentPane.add(panel, BorderLayout.CENTER);
        contentPane.add(button, BorderLayout.SOUTH);

        setVisible(true);
    }

    // 加载网络通信客户端
    public void loadNetClient(String ip, int port) {
        client = new TalkClient(ip, port, this);
        client.start();
    }

    // 从服务端加载消息
    public void loadComments(String s) {
        outputText.append(name + " :" + s + "\n");
    }
    public void setMyName(String name){
        this.name = name;
    }

}

class ClientLoginDia extends JDialog {

    public ClientLoginDia(ClientWin jFrame) {
        super(jFrame, "连接至聊天室", true);
        Container container = getContentPane();
        var tf1 = new JTextField(ChatRoom.IP2);
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
                    jFrame.menu2.setEnabled(false);
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
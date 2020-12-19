package song;

import javax.swing.*;
import java.awt.event.*;
import javax.swing.event.*;
import java.awt.*;

/**
 * 文本域
 */
public class GUITest extends JFrame {
    public GUITest() {
        setBounds(200, 200, 400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = getContentPane();
        contentPane.setLayout(new FlowLayout());

        JMenuBar menubar = new JMenuBar();
        setJMenuBar(menubar);
        JMenu menu1 = new JMenu("游戏");
        menubar.add(menu1);
        JMenuItem item1 = new JMenuItem("帮助");
        menu1.add(item1);
        // 创建文本域
        JTextArea jTextArea = new JTextArea();
        // 设定文本内容
        jTextArea.setText("初始内容");
        // 设定行
        jTextArea.setRows(5);
        // 设定列数
        jTextArea.setColumns(20);
        // 添加内容
        jTextArea.append("添加内容");
        // 在第二个字符后面插入内容
        jTextArea.insert("【插入内容】", 2);
        // 给文本域添加滚动条
        jTextArea.addMouseListener(new MouseListener(){
            public void mouseClicked(MouseEvent e) {
                System.out.println("hi");
            }
        
            public void mousePressed(MouseEvent e) {
                System.out.println("hi");
            }
        
            public void mouseReleased(MouseEvent e) {
                System.out.println("hi");
            }
        
            public void mouseEntered(MouseEvent e) {
                System.out.println("hi");
            }
        
            public void mouseExited(MouseEvent e) {
                System.out.println("hi");
            }
        });
        JScrollPane jScrollPane = new JScrollPane(jTextArea);
        contentPane.add(jScrollPane);
        setVisible(true);
    }

    public static void main(String[] args) {
        new GUITest();
    }
}

class MyMouse implements MouseListener {
    public void mouseClicked(MouseEvent e) {
        System.out.println("hi");
    }

    public void mousePressed(MouseEvent e) {
        System.out.println("hi");
    }

    public void mouseReleased(MouseEvent e) {
        System.out.println("hi");
    }

    public void mouseEntered(MouseEvent e) {
        System.out.println("hi");
    }

    public void mouseExited(MouseEvent e) {
        System.out.println("hi");
    }
}
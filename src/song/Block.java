package song;

import static java.lang.System.out;

public class Block {
    ClientWin win;
    public void block() throws InterruptedException {
        synchronized (this) {
            out.println("阻塞测试");
            wait();
            out.println("阻塞状态已解除");
        }
    }

    public void unblock() {
        synchronized (this) {
            notify();
            out.println("解除阻塞");
        }
    }

    public Block(ClientWin win) {
        this.win = win;
    }
    
}

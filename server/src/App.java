import javax.swing.JFrame;

public class App
{
    public static void main(String[] args) throws Exception
    {
        server s=new server();
        s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        s.startRunning();
    }
}

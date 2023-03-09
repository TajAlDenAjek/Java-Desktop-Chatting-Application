import javax.swing.JFrame;

public class App
{
    public static void main(String[] args) throws Exception
    {
        client c; 
        c=new client("127.0.0.1");
        c.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        c.startRunning();
    }
}

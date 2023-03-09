import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class server extends JFrame
{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;

    public server()
    {
        super("TajBook Messenger - Server-");
        userText=new JTextField();
        userText.setEditable(false);
        userText.addActionListener
        (
            new ActionListener()
            {
                public void actionPerformed(ActionEvent event)
                {
                    sendMessage(event.getActionCommand());
                    userText.setText("Enter the text here");
                }
            }
        );
        add(userText,BorderLayout.SOUTH);
        chatWindow=new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(400,500);
        setVisible(true);
    }
    public void startRunning()
    {
        try
        {
            server=new ServerSocket(6789,100);
            while(true)
            {
                try
                {
                    waitForConnection();
                    setupStreams();
                    whileChatting();
                }
                catch(EOFException eofException)
                {
                    showMessage("\n server crashed");
                }
                finally
                {
                    closeCrap();
                }
            }

        }catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
    private void waitForConnection()throws IOException
    {
        showMessage("\nwaiting for someone to connect .. \n");
        connection=server.accept();
        showMessage("Now connect to "+connection.getInetAddress().getHostName());
    }
    private void setupStreams()throws IOException
    {
        output=new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input=new ObjectInputStream(connection.getInputStream());
        showMessage("\nstreams are now ready \n");
    }
    private void whileChatting()throws IOException
    {
        String message="You are now connected";
        showMessage(message);
        ableToType(true);
        do
        {
            try
            {
                message=(String)input.readObject();
                showMessage("\n"+message);
            }
            catch(ClassNotFoundException classNotFoundException)
            {
                showMessage("There is something wrong");
            }

        }while(!message.equals("CLIENT - END"));
    }
    private void closeCrap()
    {
        showMessage("\nclosing the crap down ..\n");
        ableToType(false);
        try
        {
            output.close();
            input.close();
            connection.close();
        }
        catch(IOException IOException)
        {
            IOException.printStackTrace();
        }
    }
    private void sendMessage(String m)
    {
        try
        {
            output.writeObject("SERVER - "+m);
            output.flush();
            showMessage("\nSERVER - "+m);
        }
        catch(IOException ioException)
        {
            chatWindow.append("\nAn Error occurced");
        }
    }
    private void showMessage(final String m)
    {
        SwingUtilities.invokeLater
        (
            new Runnable()
            {
                public void run()
                {
                    chatWindow.append(m);
                }
            }
        );
    }
    private void ableToType(final boolean tof)
    {
        SwingUtilities.invokeLater
        (
            new Runnable()
            {
                public void run()
                {
                    userText.setEditable(tof);
                }
            }
        );
    }
}

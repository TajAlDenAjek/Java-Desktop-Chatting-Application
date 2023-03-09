import java.util.*;
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class client extends JFrame
{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private String serverIP;
    private String message="";
    private Socket connection;
    public client(String host)
    {
        super("TajBook Messenger - CLIENT-");
        serverIP=host;
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
            connectToServer();
            setupStream();
            whileChatting();
        }
        catch(EOFException eofException)
        {
            showMessage("\nclient terminnated the connection\n");
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
        finally
        {
            closeCrap();
        }
    }
    private void connectToServer()throws IOException
    {
        showMessage("Attempting connection ...");
        connection=new Socket(InetAddress.getByName(serverIP),6789);
        showMessage("\nConnected to: "+connection.getInetAddress().getHostName());
    }
    private void setupStream()throws IOException
    {
        output=new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input=new ObjectInputStream(connection.getInputStream());
        showMessage("\nstreams are ready!");
    }
    private void whileChatting()throws IOException
    {
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
                showMessage("I don't know what the hack is going on ");
            }

        }while(!message.equals("SERVER - END"));
    }
    private void closeCrap()
    {
        showMessage("\nclosing the crap down ..");
        ableToType(false);
        try
        {
            output.close();
            input.close();
            connection.close();
        }
        catch(IOException ioException)
        {
            ioException.printStackTrace();
        }
    }
    private void sendMessage(String m)
    {
        try
        {
            output.writeObject("CLIENT - "+m);
            output.flush();
            showMessage("\nCLIENT - "+m);
        }
        catch(IOException ioException)
        {
            chatWindow.append("\nsomething went wrong");
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


package chat.client;

import chat.network.TCPConnection;
import chat.network.TCPConnectionListener;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ClientWindow  extends JFrame  implements ActionListener, TCPConnectionListener {
    public static final String IP_ADDR="127.0.0.1";
    public static final int PORT=8189;
    public static final int WIDTH=600;
    public static final int HEIGHT=400;


    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                new ClientWindow();
            }
        });

    }
    private final JTextArea log = new JTextArea();
    private final JTextField nickname = new JTextField("Alex");
    private final JTextField fieldInput = new JTextField();
    private TCPConnection connection;

    private ClientWindow() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH,HEIGHT);
        setLocationRelativeTo(null);
        setAlwaysOnTop(true);
        setVisible(true);
        try {
            connection = new TCPConnection(this, IP_ADDR, PORT);
        } catch (IOException e) {
            printMsg("Connection Exception: " + e);
        }

        log.setEditable(false);
        log.setLineWrap(true);

        fieldInput.addActionListener(this);
        add(log, BorderLayout.CENTER);
        add(fieldInput,BorderLayout.SOUTH);
        add(nickname, BorderLayout.NORTH);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String Msg = fieldInput.getText();
        if(Msg.equals("")) return;
        fieldInput.setText(null);
        connection.sendString(nickname.getText() + ": " + Msg);
    }

    @Override
    public void onConnectionReady(TCPConnection tcpConnection) {
        printMsg("Connection ready");

    }

    @Override
    public void onReceiveString(TCPConnection tcpConnection, String value) {
        printMsg(value);

    }

    @Override
    public void onDisconnect(TCPConnection tcpConnection) {
        printMsg("Connection close");

    }

    @Override
    public void onException(TCPConnection tcpConnection, Exception e) {
        printMsg("Connection Exception: " + e);

    }
    private synchronized void printMsg(String msg) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                log.append(msg+"\n");
                log.setCaretPosition(log.getDocument().getLength());
            }
        });
    }
}

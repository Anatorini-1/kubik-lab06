package com.anatorini.lab06.TEST;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);
        Socket s = new Socket();

        while(true){
            System.out.println("Enter a command:");
            String command = sc.nextLine();
            String[] data = command.split(";");
            if(command.equals("exit")){
                break;
            }
            s.connect(new InetSocketAddress(data[0], Integer.parseInt(data[1])));
            s.getOutputStream().write(data[2].getBytes());
            s.getOutputStream().flush();
            s.close();
        }
    }
}

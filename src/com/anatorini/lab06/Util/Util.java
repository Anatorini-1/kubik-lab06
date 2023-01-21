package com.anatorini.lab06.Util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class Util {
    public static String sendMessange(String message, String host, int port) {
        try(Socket socket = new Socket(host, port)) {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.println(message);
            BufferedReader in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));
            return in.readLine();

        } catch (IOException e) {
            return "ERROR";

            }
        }
    }

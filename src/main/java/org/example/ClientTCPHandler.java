package org.example;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ClientTCPHandler {

    private Socket clientSocket;
    private static BufferedReader in = null;
    private static PrintWriter out = null;

    public ClientTCPHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void handle() {
        in = allocateReader(clientSocket);
        out = allocateWriter(clientSocket);
        handleInput();
    }

    public void handleInput() {
        String userInput;
        try {
            out.println("Inserisci una stringa:\n");

            while ((userInput = in.readLine()) != null) {
                out.println(process(userInput));
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }
    }

    public String process(String input) {
        Gson gson = new Gson();

        int c = 1;

        for (int i = 0; i < input.length(); i++) {
            char chr = input.charAt(i);
            if (chr == ' ') {
                c = c + 1;
            }
        }

        int letterCount = input.length();
        int wordsCount = c;
        String replacedString = input.replace(" ", "-");

        Result result = new Result();
        Frase frase = new Frase(letterCount, wordsCount, replacedString);

        result.AggiungiFrase(frase);

        String json = gson.toJson(result);
        return json;
    }

    private BufferedReader allocateReader(Socket clientSocket) {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        } catch (IOException e) {
            System.out.println("Reader failed" + e);
            return null;
        }
        return in;
    }

    private PrintWriter allocateWriter(Socket clientSocket) {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return out;
    }
}
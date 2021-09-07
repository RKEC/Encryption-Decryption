package com.dkit.sd2a.richardcollins;

import java.io.*;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;


public class App {
    public static void main(String[] args) {
        App app = new App();
        app.start();
    }


    private void start() {
        doMenuLoop();
    }

    private void doMenuLoop() {
        String password = "password";
        Scanner keyboard = new Scanner(System.in);
        boolean loop = true;
        Menu menuOption;
        int option;
        while (loop) {
            printMenu();
            try {
                String input = keyboard.nextLine();
                if (input.isEmpty()) {
                    throw new IllegalArgumentException();
                } else {
                    option = Integer.parseInt(input);
                }
                menuOption = Menu.values()[option];
                switch (menuOption) {
                    case QUIT_MENU:
                        loop = false;
                        break;
                    case ENCRYPTION:
                        System.out.print(Colours.ORANGE + "Type the name of the file to be encrypted (input\\plaintext.txt) :>" + Colours.RESET);
                        try {
                            String inputtedFileName = keyboard.nextLine().trim();
                            if (inputtedFileName.isEmpty()) {
                                inputtedFileName = "input\\plaintext.txt";
                            }

                            String message = readFile(inputtedFileName);
                            if(message == null)
                            {
                                throw new NullPointerException();
                            }
                            String salt = Password.generateRandomSalt();
                            String masterEKey = new Password(password, salt).generateHash();
                            String ciphertext = Cipher.encryptString(message, masterEKey);
                            System.out.println(Colours.ORANGE + "\nCipher text :> " + Colours.GREEN + ciphertext + Colours.RESET);
                            System.out.println(Colours.ORANGE + "The key was :> " + Colours.GREEN + masterEKey + Colours.RESET);
                            writeToFile("output\\ciphertext.txt", ciphertext);
                            System.out.println(Colours.ORANGE + "Cipher was saved to :> " + Colours.GREEN + "output/ciphertext.txt" + Colours.RESET);
                        } catch (IllegalArgumentException iae) {
                            System.out.println(Colours.RED + "Enter a valid file name (yourFile.txt)" + Colours.RESET);
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }catch (NullPointerException npe)
                        {
                            System.out.println();
                        }

                        break;
                    case DECRYPTION:
                        System.out.print(Colours.ORANGE + "Type the name of the file to be decrypted (output\\ciphertext.txt) :>" + Colours.RESET);
                        try {
                            String inputtedFileName = keyboard.nextLine().trim();
                            if (inputtedFileName.isEmpty()) {
                                inputtedFileName = "output\\ciphertext.txt";
                            }

                            String ciphertext = readFile(inputtedFileName);
                            System.out.print(Colours.ORANGE + "Enter key :>" + Colours.RESET);
                            String masterEKey = keyboard.nextLine().trim();
                            String plaintext = Cipher.decryptString(ciphertext, masterEKey);
                            System.out.println(Colours.GREEN + "\nPlain text :>" + Colours.RESET);
                            System.out.println(plaintext);
                            writeToFile("output\\plaintext.txt", plaintext);
                        } catch (IllegalArgumentException iae) {
                            System.out.println(Colours.RED + "Enter a valid file name (yourFile.txt)" + Colours.RESET);
                        }catch (CipherException ce)
                        {
                            System.out.println(Colours.RED + "A 128, 192, or 256-byte key is required (encoded as Base64)" + Colours.RESET);
                        }

                        break;
                }
            } catch (IllegalArgumentException iae) {
                System.out.println(Colours.RED + "Please enter a valid option" + Colours.RESET);
            } catch (Exception e)
            {
                System.out.println(Colours.RED + "Please enter a valid option" + Colours.RESET);
            }
        }
        System.out.println("Thanks for using the app");
    }

    private void writeToFile(String path, String text) {
        try (BufferedWriter cipherFile = new BufferedWriter(new FileWriter(path))) {
            cipherFile.write(text);
            cipherFile.flush();
            cipherFile.close();
        } catch (IOException ioe) {
            System.out.println(Colours.RED + "Cipher wasn't saved" + Colours.RESET);
        }
    }

    private void printMenu() {
        System.out.println("\n Options to select");
        for (int i = 0; i < Menu.values().length; i++) {
            System.out.println("\t" + Colours.PURPLE + i + ". " + Menu.values()[i].toString() + Colours.RESET);
        }
        System.out.println("Enter a number to select option (enter 0 to quit):>");

    }

    protected static String readFile(String inputFile) {
        File input = new File(inputFile);
        try (Scanner loadedFile = new Scanner(input)) {
            return loadedFile.nextLine();
        } catch (FileNotFoundException fne) {
            System.out.println(Colours.RED + "File couldn't be found" + Colours.RESET);
        }
        return null;
    }

}

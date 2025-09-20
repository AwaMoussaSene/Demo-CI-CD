package com.ecole221.devOps;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Hello and welcome! My name's Awa, i am a student");

        // Boucle infinie pour que Render garde le service actif
        while (true) {
            Thread.sleep(1000); // pause 1 seconde
        }
    }
}
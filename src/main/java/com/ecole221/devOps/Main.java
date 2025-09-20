package com.ecole221.devOps;

import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        // Démarrer le serveur sur le port 8080
        port(8080);

        // Route pour la page principale
        get("/", (req, res) -> "<h1>Hello World!</h1><p>My name's Awa</p><img src='https://via.placeholder.com/150'>");
    }
}

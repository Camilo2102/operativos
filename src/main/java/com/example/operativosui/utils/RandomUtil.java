package com.example.operativosui.utils;

import java.util.Random;

public class RandomUtil {

    private static final String[] APLICATIONS = {
            "Google Chrome", "IntelliJ IDEA", "Visual Studio Code", "Eclipse", "Photoshop",
            "WhatsApp", "Zoom", "Slack", "Microsoft Word", "Excel", "PowerPoint", "Outlook",
            "Adobe Illustrator", "Sublime Text", "Notepad++", "NetBeans", "Android Studio",
            "Xcode", "Unity", "Firefox", "Safari", "Opera", "Microsoft Teams", "Trello",
            "GitHub", "GitLab", "Bitbucket", "Jira", "Asana", "Figma", "Sketch", "InVision",
            "Adobe XD", "Blender", "Maya", "AutoCAD", "SketchUp", "GIMP", "Audacity",
            "FL Studio", "Logic Pro", "Pro Tools", "ZoomIt", "PuTTY", "VirtualBox", "VMware",
            "Docker", "Kubernetes", "Amazon AWS", "Microsoft Azure"
    };

    private static final String[] ACTIONS = {
            "Interfaz Gráfica", "Plataforma", "Framework", "Entorno", "Módulo", "Componente",
            "Sistema", "Herramienta", "Librería", "Motor", "Panel", "Widget", "Menú", "Editor",
            "Visor", "Explorador", "Generador", "Depurador", "Analizador", "Controlador",
            "Adaptador", "Visualizador", "Compilador", "Simulador", "Intérprete", "Monitor",
            "Comunicador", "Gestor", "Organizador", "Constructor", "Maquetador", "Reproductor",
            "Conversor", "Calculadora", "Planificador", "Generador de Informes", "Traductor",
            "Creador", "Diseñador", "Modelador", "Transformador", "Configurador", "Navegador",
            "Motor de Búsqueda", "Traductor", "Analista", "Optimizador", "Encriptador",
            "Descifrador", "Extractor"
    };

    public static String generateRandomName() {
        Random random = new Random();
        String aplicacion = APLICATIONS[random.nextInt(APLICATIONS.length)];
        String sustantivo = ACTIONS[random.nextInt(ACTIONS.length)];
        return aplicacion + " - " + sustantivo;
    }
    public static int randomNumber(int maxNumber){
        return (int) (Math.random() * maxNumber) + 1;
    }

    public static int randomNumber(int maxNumber, int minNumber){
        return (int) (Math.random() * maxNumber) + minNumber;
    }
}

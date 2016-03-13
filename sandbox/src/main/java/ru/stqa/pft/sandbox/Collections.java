package ru.stqa.pft.sandbox;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Collections {

    public static void main (String[] args) {
        //String[] langs = new String[4]; - объявление
        String[] langs = {"Java", "C#", "Python", "PHP"};

        for (int i = 0; i < langs.length; i++) {
            System.out.println("I want to learn " + langs[i]);
        }

        //Вариант 2
        for (String l : langs) {
            System.out.println("I want to learn " + l);
        }

        List<String> languages = new ArrayList<String>();   //Список
        languages.add("Java");
        languages.add("C#");
        languages.add("Python");

        //Вариант заполнения списка оной строкой
        //List<String> languages = Arrays.asList("Java", "C#", "Python", "PHP");

        System.out.println("----------------------------");//Просто разделитель

        for (int i = 0; i < languages.size(); i++) {
            System.out.println("Я хочу выучить " + languages.get(i));
        }

        System.out.println("----------------------------");//Просто разделитель

        for (String l: languages) {
            System.out.println("Я хочу выучить " + l);
        }
    }
}

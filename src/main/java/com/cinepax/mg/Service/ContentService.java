package com.cinepax.mg.Service;

import com.cinepax.mg.Repository.ContentRepository;
import com.cinepax.mg.view.Language;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class ContentService {

    @Autowired
    ContentRepository contentRepository;

    private static String readJsonFile(String path) {
        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return content.toString();
    }

    public HashMap<String,String> getContentFromFile(String lang) throws IOException {

        String path=lang+".json";

        String json = ContentService.readJsonFile(path);

        Gson gson = new Gson();
        HashMap<String, String> map = gson.fromJson(json, HashMap.class);

        // Affichage de la HashMap
        /*System.out.println("HashMap récupérée : " + map);

        // Accès aux valeurs individuelles
        String accueil = map.get("Accueil");
        String message = map.get("Message");
        String profile = map.get("Profile");

        System.out.println("Accueil : " + accueil);
        System.out.println("Message : " + message);
        System.out.println("Profile : " + profile);*/

        return map;

    }
    public HashMap<String,String> getContentByLanguage(String lang){
        HashMap<String,String> res = new HashMap<>();
        List<Language> allL = new ArrayList<>();
        if(lang.equals("fr") || lang.equals(" ")){
            allL = contentRepository.getContentFrench();
        } else if (lang.equals("gr")) {
            allL = contentRepository.getContentAllemand();
        } else if (lang.equals("ch")) {
            allL = contentRepository.getContentChinois();
        }

        for (Language l:allL
             ) {
            res.put(l.getKey(),l.getContent());
        }
        return res;
    }
}

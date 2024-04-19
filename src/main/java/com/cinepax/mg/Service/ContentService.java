package com.cinepax.mg.Service;

import com.cinepax.mg.Repository.ContentRepository;
import com.cinepax.mg.view.Language;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class ContentService {

    @Autowired
    ContentRepository contentRepository;


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

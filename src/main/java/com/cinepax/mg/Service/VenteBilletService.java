package com.cinepax.mg.Service;

import com.cinepax.mg.Model.*;
import com.cinepax.mg.Repository.DataCsvRepository;
import com.cinepax.mg.Repository.VenteBilletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Service
public class VenteBilletService {

    @Autowired
    VenteBilletRepository venteBilletRepository;

    @Autowired
    DataCsvRepository dataCsvRepository;

    public String insertAllData(MultipartFile file) throws Exception {
        String messError = " ";
        if (file.isEmpty()) {
            throw new Exception("File vide");
        }
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int ligne =1;

            while ((line = reader.readLine()) != null) {
                if (ligne==1){
                    ligne+=1;

                    continue;
                }
                String[] data = line.split(",");

                String numSeance = data[0];
                String titreFilm = data[1];
                String genreFilm = data[2];
                String nomSalle = data[3];
                String daty = data[4];
                String lera = data[5];

                try{
                    SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                    format.setLenient(false);
                    Date date = format.parse(daty);

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalTime time = LocalTime.parse(lera, timeFormatter);

                    DataCsv d = new DataCsv();
                    d.setFilm(titreFilm);
                    d.setCategorie(genreFilm);
                    d.setNumSeance(numSeance);
                    d.setSalle(nomSalle);
                    d.setDaty(date);
                    d.setHeure(time);

                    dataCsvRepository.save(d);
                   }catch (Exception e){
                    messError+=e.getMessage()+" Ligne : "+ligne;
                }
                ligne++;
            }
        }catch (Exception e){
            messError+=e.getMessage();
        }
        return messError;
    }

}

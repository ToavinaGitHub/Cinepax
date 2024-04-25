package com.cinepax.mg.Service;

import com.cinepax.mg.Model.*;
import com.cinepax.mg.Repository.DataCsvRepository;
import com.cinepax.mg.Repository.VenteBilletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.ParseException;
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


    public static Date parseDate(String dateString) throws ParseException {
        String[] formats = {"yyyy-MM-dd", "yyyy/MM/dd","dd/MM/yyyy", "dd-MM-yyyy"};
        for (String format : formats) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat(format);

                return sdf.parse(dateString);
            } catch (ParseException e) {
                continue;
            }
        }
        throw new ParseException("Format de date invalide", 0);
    }

    public String[] insertAllData(MultipartFile file) throws Exception {
        String[] mess = new String[2];
        int nombreTafiditra = 0;
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
                Date date = null;
                try{
                    String[] formats = {"yyyy-MM-dd", "dd/MM/yy"};
                    int count = 0;
                    //SimpleDateFormat format = new SimpleDateFormat("dd/MM/yy");
                    for (String format : formats) {
                        SimpleDateFormat sdf = new SimpleDateFormat(format);
                        sdf.setLenient(false);
                        try {
                            date = sdf.parse(daty);
                            break;
                        }catch (Exception p){
                            count++;
                            continue;
                        }
                    }
                    if(count==formats.length){
                        messError+="Format Date  Ligne : "+ligne;
                        ligne+=1;
                        continue;
                    }

                    //format.setLenient(false);
                    //Date date = format.parse(daty);

                    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
                    LocalTime time = LocalTime.parse(lera, timeFormatter);

                    DataCsv d = new DataCsv();
                    d.setFilm(titreFilm);
                    d.setCategorie(genreFilm);
                    d.setNumSeance(numSeance);
                    d.setSalle(nomSalle);
                    d.setDaty(date);
                    d.setHeure(time);

                    nombreTafiditra+=1;
                    dataCsvRepository.save(d);
                   }catch (Exception e){
                    messError+=e.getMessage()+" Ligne : "+ligne;
                }
                ligne++;
            }
        }catch (Exception e){
            messError+=e.getMessage();
        }

        mess[0] = Integer.toString(nombreTafiditra);
        mess[1] = messError;
        return mess;
    }

}

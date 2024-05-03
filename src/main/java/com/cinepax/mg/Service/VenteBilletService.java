package com.cinepax.mg.Service;

import com.cinepax.mg.Exception.ValeurInvalideException;
import com.cinepax.mg.Model.*;
import com.cinepax.mg.Repository.DataCsvRepository;
import com.cinepax.mg.Repository.VenteBilletRepository;
import jakarta.transaction.Transactional;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

        if(!mess[1].equals(" ")){
            dataCsvRepository.deleteAll();
        }else{
            // Insertion dans chaque table

            dataCsvRepository.deleteAll();
        }
        return mess;
    }

    public  String[] excelToDataCsv(InputStream is) {
        String SHEET = "dataCsv";
        String[] mess = new String[2];
        int nombreTafiditra = 0;
        String messError = " ";
        int ligne = 1;
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);

            Iterator<Row> rows = sheet.iterator();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();
                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                try{
                    DataCsv d = new DataCsv();
                    int cellIdx = 0;
                    while (cellsInRow.hasNext()) {
                        Cell currentCell = cellsInRow.next();
                        Date date = null;
                        SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                        inputFormat.setLenient(false);
                        switch (cellIdx) {
                                case 0:
                                    int temp = (int) currentCell.getNumericCellValue();
                                    d.setNumSeance(Integer.toString(temp));
                                    System.out.println(temp+" 0");
                                    break;

                                case 1:
                                    d.setFilm(currentCell.getStringCellValue());
                                    System.out.println(currentCell.getStringCellValue()+" 1");
                                    break;

                                case 2:
                                    d.setCategorie(currentCell.getStringCellValue());
                                    System.out.println(currentCell.getStringCellValue()+" 2");
                                    break;
                                case 3:
                                    d.setSalle(currentCell.getStringCellValue());
                                    System.out.println(currentCell.getStringCellValue()+" 3");
                                    break;
                                case 4:

                                    try{
                                        Date daty = currentCell.getDateCellValue();
                                        Date dat = inputFormat.parse(daty.toString());
                                        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                        outputFormat.setLenient(false);
                                        String formattedDateStr = outputFormat.format(dat);
                                        Date formattedDate = outputFormat.parse(formattedDateStr);
                                        d.setDaty(formattedDate);
                                    }catch (Exception e){
                                        messError+="Blem daty Ligne : "+ligne;
                                    }

                                    break;
                                case 5:

                                    try{
                                        double timeValue = currentCell.getNumericCellValue();
                                        LocalTime heureJava = LocalTime.ofSecondOfDay((long) (timeValue * 24 * 60 * 60));
                                        d.setHeure(heureJava);
                                    }catch (Exception e){
                                        messError+="Blem lera Ligne : "+ligne;
                                    }

                                    break;
                                default:
                                    break;
                            }
                            cellIdx++;
                    }
                    dataCsvRepository.save(d);
                    nombreTafiditra+=1;

                }catch (Exception e){
                    messError+=e.getMessage()+" Ligne : "+ligne;
                }
                ligne+=1;
            }
            workbook.close();
            mess[0] = Integer.toString(nombreTafiditra);
            mess[1] = messError;

            /*if(!mess[1].equals(" ")){
                dataCsvRepository.deleteAll();
            }else{
                // Insertion dans chaque table
                dataCsvRepository.deleteAll();
            }*/
            return mess;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }

}

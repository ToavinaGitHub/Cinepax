package com.cinepax.mg.Helper;


import com.cinepax.mg.Exception.ValeurInvalideException;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ExcelHelper {

    public static String TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

    public static boolean hasExcelFormat(MultipartFile file) {

        if (!TYPE.equals(file.getContentType())) {
            return false;
        }
        return true;
    }



    /*public static ByteArrayInputStream nombreMontantToExcel(List<V_nombre_montant_pack> nombreMontant) {
        String[] HEADERs = { "Id_pack","Nom", "Nombre", "Montant (MGA)" };
        String SHEET = "ParPack";
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (V_nombre_montant_pack ing : nombreMontant) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(ing.getId_pack());

                CellStyle cellStyle = row.getCell(0).getCellStyle();

                if (cellStyle == null) {
                    cellStyle =  row.getCell(0).getSheet().getWorkbook().createCellStyle();
                }
                cellStyle.setFillForegroundColor(IndexedColors.RED.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                row.getCell(0).setCellStyle(cellStyle);

                row.createCell(1).setCellValue(ing.getNom());
                row.createCell(2).setCellValue(ing.getNombre());
                row.createCell(3).setCellValue(ing.getMontant());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }*/

    /*
    public static ByteArrayInputStream IngredientToExcel(List<Ingredient> ingredientList) {
        String[] HEADERs = { "Id", "Nom", "Categorie" };
        String SHEET = "Tutorials";
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream();) {
            Sheet sheet = workbook.createSheet(SHEET);

            // Header
            Row headerRow = sheet.createRow(0);

            for (int col = 0; col < HEADERs.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERs[col]);
            }

            int rowIdx = 1;
            for (Ingredient ing : ingredientList) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(ing.getIdIngredient());
                row.createCell(1).setCellValue(ing.getNomIngredient());
                row.createCell(2).setCellValue(ing.getCategorieIngredient().getNomCategorieIngredient());

            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to import data to Excel file: " + e.getMessage());
        }
    }

    public static List<Ingredient> excelToIngredient(InputStream is) {
        String SHEET = "Tutorials";
        try {
            Workbook workbook = new XSSFWorkbook(is);

            Sheet sheet = workbook.getSheet(SHEET);
            Iterator<Row> rows = sheet.iterator();

            List<Ingredient> ingredients = new ArrayList<Ingredient>();

            int rowNumber = 0;
            while (rows.hasNext()) {
                Row currentRow = rows.next();

                // skip header
                if (rowNumber == 0) {
                    rowNumber++;
                    continue;
                }

                Iterator<Cell> cellsInRow = currentRow.iterator();

                Ingredient ing = new Ingredient();

                int cellIdx = 0;
                while (cellsInRow.hasNext()) {
                    Cell currentCell = cellsInRow.next();

                    switch (cellIdx) {
                        case 0:
                            ing.setIdIngredient((int) currentCell.getNumericCellValue());
                            break;

                        case 1:
                            ing.setNomIngredient(currentCell.getStringCellValue());
                            break;

                        case 2:
                            ing.setNomCategorie(currentCell.getStringCellValue());
                            break;
                        default:
                            break;
                    }

                    cellIdx++;
                }

                ingredients.add(ing);
            }

            workbook.close();

            return ingredients;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse Excel file: " + e.getMessage());
        }
    }*/
}

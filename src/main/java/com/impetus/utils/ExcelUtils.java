package com.impetus.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

public class ExcelUtils {


    // Expected column headers in the correct order
    public static final String[] EXPECTED_HEADERS = {
            "Segment", "Family", "ClassName", "TopBrick", "Brick", "FinalBrick",
            "BrickCode", "BrickName", "Enrichment", "MRPMin", "MRPMax", "OptionsCount",
            "OTBNumber", "ODM Quantity", "OEM Quantity", "TotalQty", "FillPercentage"
    };

    // Expected column headers for Download Sample file
    public static final String[] DOWNLOAD_SAMPLE_EXPECTED_HEADERS = {
            "Segment", "Family", "ClassName", "TopBrick", "Brick", "FinalBrick",
            "BrickCode", "BrickName", "Enrichment", "MRPMin", "MRPMax", "OptionsCount",
            "OTBNumber",
    };
//    public static List<String> getExcelHeaders(String filePath) {
//        List<String> headers = new ArrayList<>();
//        try (FileInputStream fis = new FileInputStream(new File(filePath));
//             Workbook workbook = new XSSFWorkbook(fis)) {
//
//            Sheet sheet = workbook.getSheetAt(0);
//            Row headerRow = sheet.getRow(0);
//
//            for (Cell cell : headerRow) {
//                headers.add(cell.getStringCellValue().trim());
//            }
//        } catch (Exception e) {
//            throw new RuntimeException("Error reading Excel file: " + e.getMessage());
//        }
//        return headers;
//    }
//}
        public static List<Map<String, String>> readExcelData(String filePath, String sheetName) throws IOException {
            List<Map<String, String>> data = new ArrayList<>();
            try (FileInputStream fis = new FileInputStream(new File(filePath));
                 XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

                XSSFSheet sheet = workbook.getSheet(sheetName);
                if (sheet == null) {
                    throw new IllegalArgumentException("Sheet '" + sheetName + "' not found");
                }
                Row headerRow = sheet.getRow(0);
                if (headerRow == null) {
                    throw new IllegalArgumentException("Header row not found");
                }

                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row currentRow = sheet.getRow(i);
                    Map<String, String> rowData = new HashMap<>();

                    for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                        String header = headerRow.getCell(j).getStringCellValue();
                        if (!header.equalsIgnoreCase("Segment")) {
                            Cell cell = currentRow.getCell(j);
                            rowData.put(header, getCellValueAsString(cell));
                        }
                    }
                    data.add(rowData);
                }
            }
            return data;
        }

    public static List<Map<String, String>> readMRPMultiplierExcelData(String filePath, String sheetName) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                throw new IllegalArgumentException("Sheet '" + sheetName + "' not found");
            }
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                throw new IllegalArgumentException("Header row not found");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row currentRow = sheet.getRow(i);
                Map<String, String> rowData = new HashMap<>();

                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String header = headerRow.getCell(j).getStringCellValue();
                    if (!header.equalsIgnoreCase("Tenant ID")) {
                        Cell cell = currentRow.getCell(j);
                        rowData.put(header, getCellValueAsString(cell));
                    }
                }
                data.add(rowData);
            }
        }
        return data;
    }

    // 2. VALIDATE FILE NAMING CONVENTION
    public static boolean validateFileNamingConvention(String fileName) {
        // Expected format: RAID_Export_YYYYMMDD_HHMMSS.xlsx or similar
        Pattern pattern = Pattern.compile("^RA_RA-\\d{6}-\\d{4}( \\(\\d+\\))?\\.xlsx$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(fileName).matches();
    }

    // 3. VALIDATE EXCEL HEADERS
    public static boolean validateExcelHeaders(String filePath, String sheetName) throws IOException {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheet(sheetName);
            if (sheet == null) {
                System.out.println("❌ Sheet not found with name: " + sheetName);
                for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                    System.out.println("Available sheet: " + workbook.getSheetName(i));
                }
                return false;
            }
            Row headerRow = sheet.getRow(0);
            if (headerRow == null) {
                System.out.println("❌ Header row is null (Row 0)");
                return false;
            }
            if (headerRow.getLastCellNum() != EXPECTED_HEADERS.length) {
                System.out.println("❌ Header column count mismatch: found " +
                        headerRow.getLastCellNum() + ", expected " + EXPECTED_HEADERS.length);
                return false;
            }

            for (int i = 0; i < EXPECTED_HEADERS.length; i++) {
                String actualHeader = getCellValueAsString(headerRow.getCell(i)).trim();
                if (!EXPECTED_HEADERS[i].trim().equalsIgnoreCase(actualHeader.trim())) {
                    System.out.println("❌ Header mismatch at index " + i +
                            ": expected '" + EXPECTED_HEADERS[i] + "', found '" + actualHeader + "'");
                    return false;
                }
            }
            System.out.println("✅ All headers matched successfully");
            return true;
        }
    }

    // 4. VALIDATE DATA TYPES
    public static boolean validateDataTypes(List<Map<String, String>> data) {
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> row = data.get(i);

            // Validate integer fields
            if (!isValidInteger(row.get("MRPMin")) ||
                    !isValidInteger(row.get("MRPMax")) ||
                    !isValidInteger(row.get("BrickCode")) ||
                    !isValidInteger(row.get("OptionsCount")) ||
                    !isValidInteger(row.get("ODM Quantity")) ||
                    !isValidInteger(row.get("OEM Quantity")) ||
                    !isValidInteger(row.get("TotalQty"))) {
                System.out.println("❌ Invalid integer data type in row " + (i + 2)); // +2 for actual Excel row (header + 0-based index)
                return false;
            }

            // Validate string fields
            if (!isValidString(row.get("OTBNumber")) ||
                    !isValidString(row.get("Family")) ||
                    !isValidString(row.get("ClassName")) ||
                    !isValidString(row.get("TopBrick")) ||
                    !isValidString(row.get("Brick")) ||
                    !isValidString(row.get("BrickName")) ||
                    !isValidString(row.get("Enrichment"))) {
                System.out.println("❌ Invalid string data in row " + (i + 2));
                return false;
            }

            // Validate percentage field
            if (!isValidPercentage(row.get("FillPercentage"))) {
                System.out.println("❌ Invalid percentage format in row " + (i + 2));
                return false;
            }
        }
        return true;
    }

    // 5. VALIDATE TOTAL QTY CALCULATION (ODM + OEM = Total)
    public static boolean validateTotalQtyCalculation(List<Map<String, String>> data) {
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> row = data.get(i);

            try {
                int odm = Integer.parseInt(row.get("ODM Quantity"));
                int oem = Integer.parseInt(row.get("OEM Quantity"));
                int total = Integer.parseInt(row.get("TotalQty"));

                if (odm + oem != total) {
                    System.out.println("TotalQty calculation error in row " + (i + 1) +
                            ": ODM(" + odm + ") + OEM(" + oem + ") != Total(" + total + ")");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format in row " + (i + 1));
                return false;
            }
        }
        return true;
    }

    // 6. VALIDATE FILL PERCENTAGE CALCULATION (Total/OptionsCount * 100)
    public static boolean validateFillPercentageCalculation(List<Map<String, String>> data) {
        for (int i = 0; i < data.size(); i++) {
            Map<String, String> row = data.get(i);

            try {
                int totalQty = Integer.parseInt(row.get("TotalQty"));
                int optionsCount = Integer.parseInt(row.get("OptionsCount"));
                String fillPercentageStr = row.get("FillPercentage").replace("%", "").trim();
                double actualFillPercentage = Double.parseDouble(fillPercentageStr);

                double expectedFillPercentage = optionsCount > 0 ?
                        (double) totalQty / optionsCount * 100 : 0;

                // Allow small tolerance for floating point comparison
                if (Math.abs(expectedFillPercentage - actualFillPercentage) > 0.1) {
                    System.out.println("FillPercentage calculation error in row " + (i + 1) +
                            ": expected " + String.format("%.1f", expectedFillPercentage) +
                            "%, actual " + actualFillPercentage + "%");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid number format for fill percentage in row " + (i + 1));
                return false;
            }
        }
        return true;
    }

    // 7. CHECK IF FILE EXISTS AND IS VALID EXCEL
    public static boolean isValidExcelFile(String filePath) {
        File file = new File(filePath);
        if (!file.exists() || !file.isFile()) {
            return false;
        }

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {
            return workbook.getNumberOfSheets() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    // 8. GET FILE SIZE
    public static long getFileSizeInBytes(String filePath) {
        File file = new File(filePath);
        return file.exists() ? file.length() : -1;
    }

    // 9. COMPARE TWO EXCEL FILES
    public static boolean compareExcelData(List<Map<String, String>> expected,
                                           List<Map<String, String>> actual,
                                           List<String> columnsToCompare) {
        if (expected.size() != actual.size()) {
            System.out.println("Row count mismatch: expected " + expected.size() +
                    ", actual " + actual.size());
            return false;
        }

        for (int i = 0; i < expected.size(); i++) {
            Map<String, String> expectedRow = expected.get(i);
            Map<String, String> actualRow = actual.get(i);

            for (String column : columnsToCompare) {
                if (!Objects.equals(expectedRow.get(column), actualRow.get(column))) {
                    System.out.println("Data mismatch in row " + (i + 1) + ", column '" + column +
                            "': expected '" + expectedRow.get(column) +
                            "', actual '" + actualRow.get(column) + "'");
                    return false;
                }
            }
        }
        return true;
    }

    // 10. GET DIFFERENCES BETWEEN TWO DATASETS
    public static List<String> findDataDifferences(List<Map<String, String>> expected,
                                                   List<Map<String, String>> actual) {
        List<String> differences = new ArrayList<>();

        if (expected.size() != actual.size()) {
            differences.add("Row count mismatch: expected " + expected.size() +
                    ", actual " + actual.size());
        }

        int minSize = Math.min(expected.size(), actual.size());
        for (int i = 0; i < minSize; i++) {
            Map<String, String> expectedRow = expected.get(i);
            Map<String, String> actualRow = actual.get(i);

            for (String key : expectedRow.keySet()) {
                if (!Objects.equals(expectedRow.get(key), actualRow.get(key))) {
                    differences.add("Row " + (i + 1) + ", Column '" + key +
                            "': expected '" + expectedRow.get(key) +
                            "', actual '" + actualRow.get(key) + "'");
                }
            }
        }

        return differences;
    }

    public static String getCellValueAsString(Cell cell) {
            if (cell == null) return "";

            switch (cell.getCellType()) {
                case STRING: return cell.getStringCellValue();
                case NUMERIC:
                    if (DateUtil.isCellDateFormatted(cell)) {
                        return cell.getDateCellValue().toString();
                    } else {
                        return String.valueOf((long)cell.getNumericCellValue());
                    }
              //  case NUMERIC: return String.valueOf((int)cell.getNumericCellValue());
                case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
                case FORMULA: return cell.getCellFormula();
                default: return "";
            }
        }

    private static boolean isValidInteger(String value) {
        try {
            if (value == null || value.trim().isEmpty()) return false;
            Integer.parseInt(value.trim());
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }


    private static boolean isValidString(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private static boolean isValidPercentage(String value) {
        try {
            if (value == null || value.trim().isEmpty()) {
                System.out.println("⚠️ Empty or null value for percentage");
                return false;
            }
            String cleanValue = value.replace("%", "").trim();
            double percentage = Double.parseDouble(cleanValue);
            return percentage >= 0 && percentage <= 100;
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Invalid number format: " + value);
            return false;
        }
    }

    // 12. GET EXPECTED HEADERS
    public static String[] getExpectedHeaders() {
        return EXPECTED_HEADERS.clone();
    }

    // 13. VALIDATE SPECIFIC COLUMN DATA
    public static boolean validateColumnData(List<Map<String, String>> data,
                                             String columnName,
                                             String dataType) {
        for (int i = 0; i < data.size(); i++) {
            String value = data.get(i).get(columnName);

            switch (dataType.toLowerCase()) {
                case "integer":
                    if (!isValidInteger(value)) {
                        System.out.println("Invalid integer in row " + (i + 1) +
                                ", column '" + columnName + "': " + value);
                        return false;
                    }
                    break;
                case "string":
                    if (!isValidString(value)) {
                        System.out.println("Invalid string in row " + (i + 1) +
                                ", column '" + columnName + "': " + value);
                        return false;
                    }
                    break;
                case "percentage":
                    if (!isValidPercentage(value)) {
                        System.out.println("Invalid percentage in row " + (i + 1) +
                                ", column '" + columnName + "': " + value);
                        return false;
                    }
                    break;
                default:
                    return false;
            }
        }
        return true;
    }

        public static void writeExcelData(String filePath, String sheetName, List<Map<String, String>> data) throws IOException {
            try (XSSFWorkbook workbook = new XSSFWorkbook()) {
                XSSFSheet sheet = workbook.createSheet(sheetName);

                // Create header row
                Row headerRow = sheet.createRow(0);
                Map<String, String> firstRow = data.get(0);
                int cellIdx = 0;
                for (String header : firstRow.keySet()) {
                    Cell cell = headerRow.createCell(cellIdx++);
                    cell.setCellValue(header);
                }

                // Create data rows
                for (int i = 0; i < data.size(); i++) {
                    Row row = sheet.createRow(i + 1);
                    Map<String, String> rowData = data.get(i);
                    cellIdx = 0;
                    for (String value : rowData.values()) {
                        Cell cell = row.createCell(cellIdx++);
                        cell.setCellValue(value);
                    }
                }

                // Write to file
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    workbook.write(fos);
                }
            }
        }

    /**
     * Validate Download Sample Excel file headers
     */
    public static boolean validateDownloadSampleHeaders(String filePath) throws IOException {
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0); // Get first sheet
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                System.out.println("❌ Header row is null (Row 0)");
                return false;
            }

            if (headerRow.getLastCellNum() != DOWNLOAD_SAMPLE_EXPECTED_HEADERS.length) {
                System.out.println("❌ Header column count mismatch: found " +
                        headerRow.getLastCellNum() + ", expected " + DOWNLOAD_SAMPLE_EXPECTED_HEADERS.length);
                return false;
            }

            for (int i = 0; i < DOWNLOAD_SAMPLE_EXPECTED_HEADERS.length; i++) {
                String actualHeader = getCellValueAsString(headerRow.getCell(i)).trim();
                if (!DOWNLOAD_SAMPLE_EXPECTED_HEADERS[i].trim().equalsIgnoreCase(actualHeader.trim())) {
                    System.out.println("❌ Header mismatch at index " + i +
                            ": expected '" + DOWNLOAD_SAMPLE_EXPECTED_HEADERS[i] + "', found '" + actualHeader + "'");
                    return false;
                }
            }
            System.out.println("✅ All Download Sample headers matched successfully");
            return true;
        }
    }

    /**
     * Validate Download Sample file data types
     */
    public static boolean validateDownloadSampleDataTypes(String filePath) throws IOException {
        List<Map<String, String>> data = readDownloadSampleData(filePath);

        for (int i = 0; i < data.size(); i++) {
            Map<String, String> row = data.get(i);

            // Validate string fields
            if (!isValidString(row.get("Family")) ||
                    !isValidString(row.get("ClassName")) ||
                    !isValidString(row.get("BrickName")) ||
                    !isValidString(row.get("TopBrick")) ||
                    !isValidString(row.get("Final brick")) ||
                    !isValidString(row.get("Brick")) ||
                    !isValidString(row.get("Enrichment"))) {
                System.out.println("❌ Invalid string data in row " + (i + 2));
                return false;
            }

            // Validate integer fields
            if (!isValidInteger(row.get("Brick Code")) ||
                    !isValidInteger(row.get("MRPMAx")) ||
                    !isValidInteger(row.get("MRPMin")) ||
                    !isValidInteger(row.get("OptionsCount"))) {
                System.out.println("❌ Invalid integer data type in row " + (i + 2));
                return false;
            }

            // Validate OTBNumber (can be string or alphanumeric)
            if (!isValidString(row.get("OTBNumber"))) {
                System.out.println("❌ Invalid OTBNumber data in row " + (i + 2));
                return false;
            }
        }
        return true;
    }

    /**
     * Read Download Sample Excel data
     */
    public static List<Map<String, String>> readDownloadSampleData(String filePath) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            XSSFSheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                throw new IllegalArgumentException("Header row not found");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String header = getCellValueAsString(headerRow.getCell(j));
                    Cell cell = currentRow.getCell(j);
                    rowData.put(header, getCellValueAsString(cell));
                }
                data.add(rowData);
            }
        }
        return data;
    }

    /**
     * Validate Download Sample file naming convention
     */
    public static boolean validateDownloadSampleFileNaming(String fileName) {
        // Expected format for sample files: might be different from regular downloads
        Pattern pattern = Pattern.compile("^.*Sample.*.xlsx$", Pattern.CASE_INSENSITIVE);
        return pattern.matcher(fileName).matches() || fileName.toLowerCase().contains("sample");
    }



    public static List<Map<String, String>> readMultiplierData(String filePath) throws IOException {
        List<Map<String, String>> data = new ArrayList<>();
        try (FileInputStream fis = new FileInputStream(new File(filePath));
             XSSFWorkbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            Row headerRow = sheet.getRow(0);

            if (headerRow == null) {
                throw new IllegalArgumentException("Header row not found");
            }

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row currentRow = sheet.getRow(i);
                if (currentRow == null) continue;

                Map<String, String> rowData = new HashMap<>();
                for (int j = 0; j < headerRow.getLastCellNum(); j++) {
                    String header = getCellValueAsString(headerRow.getCell(j));
                    Cell cell = currentRow.getCell(j);
                    rowData.put(header, getCellValueAsString(cell));
                }
                data.add(rowData);
            }
        }
        return data;
    }

    public static boolean validateMultiplierDataContent(List<Map<String, String>> data) {
        for (Map<String, String> row : data) {
            // Validate that RA Min MRP <= RA Max MRP
            try {
                double minMRP = Double.parseDouble(row.get("RA Min MRP"));
                double maxMRP = Double.parseDouble(row.get("RA Max MRP"));
                if (minMRP > maxMRP) {
                    System.out.println("❌ RA Min MRP should be <= RA Max MRP");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid MRP values");
                return false;
            }

            // Validate that Minimum Cost <= Maximum Cost
            try {
                double minCost = Double.parseDouble(row.get("Minimum Cost"));
                double maxCost = Double.parseDouble(row.get("Maximum Cost"));
                if (minCost > maxCost) {
                    System.out.println("❌ Minimum Cost should be <= Maximum Cost");
                    return false;
                }
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid Cost values");
                return false;
            }
        }
        return true;
    }
    }

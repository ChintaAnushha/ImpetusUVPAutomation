package com.impetus.utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.impetus.constants.ApplicationConstants;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CommonUtils {
   private static ExtentTest test ;
    private static final String SCREENSHOT_DIR = "test-output/Screenshots/";


    public static String captureScreenshot(WebDriver driver, String screenshotName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File source = ts.getScreenshotAs(OutputType.FILE);
            String timestamp = new SimpleDateFormat(ApplicationConstants.TIMESTAMP_FORMAT).format(new Date());
            String destination = ApplicationConstants.SCREENSHOT_PATH + "/" + screenshotName + "_" + timestamp + ".png";
            File finalDestination = new File(destination);
            FileUtils.copyFile(source, finalDestination);
            return destination;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void scrollToElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public static void clickUsingJS(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].click();", element);
    }

    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public static String getCurrentTimestamp() {
        return new SimpleDateFormat(ApplicationConstants.TIMESTAMP_FORMAT).format(new Date());
    }

    public static String getCurrentDate() {
        return new SimpleDateFormat(ApplicationConstants.DATE_FORMAT).format(new Date());
    }

//    public static void uploadFileUsingRobot(String filePath) throws Exception {
//        StringSelection selection = new StringSelection(filePath);
//        Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
//
//
//        Robot robot = new Robot();
//        robot.setAutoDelay(100);// Wait for dialog to appear
//       // robot.setAutoDelay(1000);
//    //    robot.delay(1000);
////        Robot robot = new Robot();
//       // robot.setAutoDelay(100);
//
////        robot.keyPress(KeyEvent.VK_META);
////        robot.keyPress(KeyEvent.VK_SPACE);
////        robot.keyRelease(KeyEvent.VK_SPACE);
////        robot.keyRelease(KeyEvent.VK_META);
//        // Press CMD + SHIFT + G (Go to folder)
//
//        robot.keyPress(KeyEvent.VK_META);          // Command
//        robot.keyPress(KeyEvent.VK_SHIFT);
//        robot.keyPress(KeyEvent.VK_G);
//        robot.keyRelease(KeyEvent.VK_G);
//        robot.keyRelease(KeyEvent.VK_SHIFT);
//        robot.keyRelease(KeyEvent.VK_META);
//
//        robot.delay(1000);
//
//        // Paste file path (CMD + V)
//        robot.keyPress(KeyEvent.VK_META);
//        robot.keyPress(KeyEvent.VK_V);
//        robot.keyRelease(KeyEvent.VK_V);
//        robot.keyRelease(KeyEvent.VK_META);
//      //  robot.keyRelease(KeyEvent.VK_V);
//
//        robot.delay(1000);
//
//        // Press Enter (go to folder)
//
//        robot.keyPress(KeyEvent.VK_ENTER);
//        robot.keyRelease(KeyEvent.VK_ENTER);
//
////        robot.delay(2000);
////
////        // Tab to the file (if needed)
////        robot.keyPress(KeyEvent.VK_TAB);
////        robot.keyRelease(KeyEvent.VK_TAB);
////
////        robot.delay(500);
////
//

    /// /        // Press Enter again (select file)
//        robot.keyPress(KeyEvent.VK_ENTER);
//        robot.keyRelease(KeyEvent.VK_ENTER);
//
//        robot.delay(1000); // Wait for file selection to complete
//    }
    public static void uploadFileUsingRobot(String filePath) throws IOException, InterruptedException {
        // Prepare the AppleScript command
        String escapedPath = filePath.replace(" ", "\\ ").replace("\"", "\\\"");

        String[] appleScript = {
                "osascript",
                "-e", "tell application \"System Events\"",
                "-e", "delay 2", // Increased initial delay
                "-e", "keystroke \"g\" using {command down, shift down}",
                "-e", "delay 1.5",
                "-e", String.format("keystroke \"%s\"", escapedPath),
                "-e", "delay 1.5",
                "-e", "keystroke return",
                "-e", "delay 2", // Increased delay after navigation
                "-e", "keystroke return",
                "-e", "delay 2", // Increased delay before button click
                // More robust button clicking logic
                "-e", "try",
                "-e", "if exists button \"Open\" of window 1 then",
                "-e", "click button \"Open\" of window 1",
                "-e", "else if exists button \"Open\" of sheet 1 of window 1 then",
                "-e", "click button \"Open\" of sheet 1 of window 1",
                "-e", "else if exists button \"Choose\" of window 1 then",
                "-e", "click button \"Choose\" of window 1",
                "-e", "end if",
                "-e", "end try",
                "-e", "delay 2", // Added delay after button click
                "-e", "end tell"
        };

        // Execute the AppleScript with timeout
        ProcessBuilder pb = new ProcessBuilder(appleScript);
        Process process = pb.start();

        boolean completed = process.waitFor(30, TimeUnit.SECONDS); // Added timeout
        if (!completed) {
            process.destroyForcibly();
            throw new RuntimeException("File upload script timed out");
        }

        Thread.sleep(3000); // Final wait to ensure upload completes
    }

    public static boolean validateExcelAndUIData(List<Map<String, String>> excelData, List<Map<String, String>> uiData) {
        if (excelData.size() != uiData.size()) {
            String msg = "❌ Row count mismatch. Excel: " + excelData.size() + ", UI: " + uiData.size();
            System.out.println(msg);
            return false;
        }

        // Define the fields to validate
        Set<String> fieldsToValidate = new HashSet<>(Arrays.asList(
                "Family", "ClassName", "BrickName", "TopBrick", "Brick",
                "Enrichment", "MRP Range", "OptionsCount", "ODM Quantity",
                "OEM Quantity", "TotalQty", "FillPercentage"
        ));


        for (int i = 0; i < uiData.size(); i++) {
            Map<String, String> uiRow = uiData.get(i);
            Map<String, String> excelRow = excelData.get(i);

            for (String key : uiRow.keySet()) {
                // Skip if not in our validation list
                if (!fieldsToValidate.contains(key)) {
                    continue;
                }

                String uiValue = uiRow.getOrDefault(key, "").trim();
                String excelValue;

                if (key.equalsIgnoreCase("MRP Range")) {
                    String aspMin = excelRow.getOrDefault("ASPMin", "").trim();
                    String aspMax = excelRow.getOrDefault("ASPMax", "").trim();
                    if (aspMin.isEmpty() || aspMax.isEmpty()) {
                        String msg = "❌ Missing ASPMin or ASPMax at row " + (i + 1);
                        System.out.println(msg);
                        return false;
                    }
                    excelValue = aspMin + " - " + aspMax;
                } else if (key.equalsIgnoreCase("FillPercentage")) {
                    excelValue = excelRow.getOrDefault(key, "0") + "%";
                } else {
                    excelValue = excelRow.getOrDefault(key, "").trim();
                }

                if (!excelValue.equalsIgnoreCase(uiValue)) {
                    String msg = "❌ Mismatch at Row " + (i + 1) + ", Column: " + key
                            + "\n   ➤ Excel: '" + excelValue + "', UI: '" + uiValue + "'";
                    System.out.println(msg);
                    return false;
                }
            }
        }

        System.out.println("✅ All Excel and UI data matched.");
        return true;
    }

    public static boolean validateMultiplierExcelAndUIData(List<Map<String, String>> excelData, List<Map<String, String>> uiData) {
        if (excelData.size() != uiData.size()) {
            String msg = "❌ Row count mismatch. Excel: " + excelData.size() + ", UI: " + uiData.size();
            System.out.println(msg);
            return false;
        }

        // Define the fields to validate based on your screenshot
        Set<String> fieldsToValidate = new HashSet<>(Arrays.asList(
                "Tenant ID", "Final MRP", "RA MRP MIN", "RA MRP MAX",
                "Minimum Cost", "Maximum Cost", "Brick Code", "Enrichment"
        ));

        for (int i = 0; i < uiData.size(); i++) {
            Map<String, String> uiRow = uiData.get(i);
            Map<String, String> excelRow = excelData.get(i);

            for (String key : uiRow.keySet()) {
                // Skip if not in our validation list
                if (!fieldsToValidate.contains(key)) {
                    continue;
                }

                String uiValue = uiRow.getOrDefault(key, "").trim();
                String excelValue = excelRow.getOrDefault(key, "").trim();

                if (!excelValue.equalsIgnoreCase(uiValue)) {
                    String msg = "❌ Mismatch at Row " + (i + 1) + ", Column: " + key
                            + "\n   ➤ Excel: '" + excelValue + "', UI: '" + uiValue + "'";
                    System.out.println(msg);
                    return false;
                }
            }
        }

        System.out.println("✅ All Excel and UI data matched.");
        return true;
    }

    public static List<Map<String, String>> extractTableData(List<WebElement> uiRows) {
        List<Map<String, String>> uiData = new ArrayList<>();
        for (WebElement row : uiRows) {
            Map<String, String> rowMap = new HashMap<>();
            rowMap.put("Family", row.findElement(By.xpath(".//td[1]")).getText().trim());
            rowMap.put("Class Name", row.findElement(By.xpath(".//td[2]")).getText().trim());
            rowMap.put("Brick Name", row.findElement(By.xpath(".//td[3]")).getText().trim());
            rowMap.put("Top Brick", row.findElement(By.xpath(".//td[4]")).getText().trim());
            rowMap.put("Brick", row.findElement(By.xpath(".//td[5]")).getText().trim());
            rowMap.put("Enrichment", row.findElement(By.xpath(".//td[6]")).getText().trim());
            rowMap.put("MRP Range", row.findElement(By.xpath(".//td[7]")).getText().trim());
            // Assuming td[1] is Options (skip td[0] = OTB Number)
            rowMap.put("OptionsCount", row.findElement(By.xpath(".//td[8]")).getText().trim());
            rowMap.put("ODM Quantity", row.findElement(By.xpath(".//td[9]")).getText().trim());
            rowMap.put("OEM Quantity", row.findElement(By.xpath(".//td[10]")).getText().trim());
            rowMap.put("TotalQty", row.findElement(By.xpath(".//td[11]")).getText().trim());
            // Clean up Fill Rate (e.g., "0 %" -> "0%")
            String fillRate = row.findElement(By.xpath(".//td[12]")).getText().trim().replace(" ", "");
            rowMap.put("FillPercentage", fillRate);
            rowMap.put("Action",row.findElement(By.xpath(".//td[13]")).getText().trim());


            uiData.add(rowMap);
        }
        return uiData;
    }


    public static List<Map<String, String>> extractTableDataForMultiplier(List<WebElement> uiRows) {
        List<Map<String, String>> uiData = new ArrayList<>();
        for (WebElement row : uiRows) {
            Map<String, String> rowMap = new HashMap<>();
            rowMap.put("Tenant ID", row.findElement(By.xpath(".//td[1]")).getText().trim());
            rowMap.put("Final MRP", row.findElement(By.xpath(".//td[2]")).getText().trim());
            rowMap.put("RA MRP MIN", row.findElement(By.xpath(".//td[3]")).getText().trim());
            rowMap.put("RA MRP MAX", row.findElement(By.xpath(".//td[4]")).getText().trim());
            rowMap.put("Minimum Cost", row.findElement(By.xpath(".//td[5]")).getText().trim());
            rowMap.put("Maximum Cost", row.findElement(By.xpath(".//td[6]")).getText().trim());
            rowMap.put("Brick Code", row.findElement(By.xpath(".//td[7]")).getText().trim());
            rowMap.put("Enrichment", row.findElement(By.xpath(".//td[8]")).getText().trim());
            uiData.add(rowMap);
        }
        return uiData;
    }

    public static boolean validateTableHeaders(List<WebElement> headerElements, List<String> expectedHeaders) {
        if (headerElements.size() != expectedHeaders.size()) {
            System.out.println("❌ Header count mismatch. Expected: " + expectedHeaders.size() + ", Actual: " + headerElements.size());
            return false;
        }

        for (int i = 0; i < expectedHeaders.size(); i++) {
            String actualHeader = headerElements.get(i).getText().trim();
            String expectedHeader = expectedHeaders.get(i);

            if (!actualHeader.equalsIgnoreCase(expectedHeader)) {
                System.out.println("❌ Header mismatch at position " + (i + 1) + ". Expected: '" + expectedHeader + "', Actual: '" + actualHeader + "'");
                return false;
            }
        }

        System.out.println("✅ All table headers matched successfully.");
        return true;
    }

    public static void waitForPageLoad(WebDriver driver) {
        new WebDriverWait(driver, Duration.ofSeconds(40)).until(
                webDriver -> ((JavascriptExecutor) webDriver)
                        .executeScript("return document.readyState").equals("complete"));
    }

    public static void waitForVisibility(WebDriver driver, WebElement element, int timeoutInSec) {
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(timeoutInSec));
        wait.until(ExpectedConditions.visibilityOf(element));
    }

    public static boolean verifySortingSymbolsPresent(WebDriver driver, List<String> columnHeaders) {
        try {
            for (String header : columnHeaders) {
                WebElement headerElement = driver.findElement(
                     //   By.xpath("//th[.//span[contains(text(), '" + header + "')]]//span[contains(@class, 'sc-')][2]")
                        By.xpath("//th[.//*[text()='" + header + "']]//span[2]")
                );
                if (!headerElement.isDisplayed()) {
                    System.out.println("❌ Sort symbol not found for column: " + header);
                    return false;
                }
            }
            System.out.println("✅ Sort symbols present for all columns");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error verifying sort symbols: " + e.getMessage());
            return false;
        }
    }

public static boolean verifyColumnSorting(WebDriver driver, String columnName, List<WebElement> rows, int columnIndex) {
        try {
            // Get initial values
            List<String> initialValues = getColumnValues(driver, columnIndex);
            System.out.println("📊 Verifying sorting for column: " + columnName);

            // First click for ascending sort
            WebElement sortButton = driver.findElement(
                  //  By.xpath("//th[.//span[contains(text(), '" + columnName + "')]]//span[contains(@class, 'sc-')][2]")
                    By.xpath("//th[.//*[text()='" + columnName + "']]//span[2]")
            );

            // Ensure element is visible and clickable
            scrollToElement(driver, sortButton);
            waitForElementToBeClickable(driver, sortButton);

            // Click using JavaScript for more precise clicking
            clickUsingJS(driver, sortButton);
            Thread.sleep(2000);
            waitForPageLoad(driver);

//            sortButton.click();
//            Thread.sleep(2000);
//            waitForPageLoad(driver);

            // Get ascending sorted values
            List<String> ascendingValues = getColumnValues(driver, columnIndex);

            // Verify ascending order (small to big)
            boolean isAscendingCorrect = isInAscendingOrder(ascendingValues);
            if (!isAscendingCorrect) {
                System.out.println("❌ Not in ascending order for " + columnName);
                System.out.println("Values: " + ascendingValues);
                return false;
            }

            // Second click for descending sort
            sortButton.click();
            Thread.sleep(2000);
            waitForPageLoad(driver);

            // Get descending sorted values
            List<String> descendingValues = getColumnValues(driver, columnIndex);

            // Verify descending order (big to small)
            boolean isDescendingCorrect = isInDescendingOrder(descendingValues);
            if (!isDescendingCorrect) {
                System.out.println("❌ Not in descending order for " + columnName);
                System.out.println("Values: " + descendingValues);
                return false;
            }

            System.out.println("✅ Sorting verified for " + columnName);
            return true;

        } catch (Exception e) {
            System.out.println("❌ Error verifying sorting for " + columnName + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
}

    private static boolean isInAscendingOrder(List<String> values) {
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i).compareToIgnoreCase(values.get(i + 1)) > 0) {
                return false;
            }
        }
        return true;
    }

    private static boolean isInDescendingOrder(List<String> values) {
        for (int i = 0; i < values.size() - 1; i++) {
            if (values.get(i).compareToIgnoreCase(values.get(i + 1)) < 0) {
                return false;
            }
        }
        return true;
    }

//    private static List<String> getColumnValues(List<String> rows, int columnIndex) {
//        List<String> values = new ArrayList<>();
//        for (WebElement row : rows) {
//            String value = row.findElement(By.xpath(".//td[" + columnIndex + "]")).getText().trim();
//            values.add(value);
//        }
//        return values;
//    }
      private static List<String> getColumnValues(WebDriver driver, int columnIndex) {
        List<String> values = new ArrayList<>();
        List<WebElement> rows = driver.findElements(By.xpath("//table//tbody/tr"));
         for (WebElement row : rows) {
        values.add(row.findElement(By.xpath(".//td[" + columnIndex + "]")).getText().trim());
         }
         return values;
      }

    public static boolean verifyColumnOrder(WebDriver driver, List<String> expectedOrder) {
        try {
            List<WebElement> headers = driver.findElements(By.xpath("//th[contains(@role, 'columnheader')]"));
            List<String> actualOrder = new ArrayList<>();

            for (WebElement header : headers) {
                String headerText = header.getText().trim();
                if (!headerText.isEmpty()) {
                    actualOrder.add(headerText);
                }
            }

            if (!actualOrder.equals(expectedOrder)) {
                System.out.println("❌ Column order mismatch. Expected: " + expectedOrder + ", Actual: " + actualOrder);
                return false;
            }

            System.out.println("✅ Column order verified");
            return true;
        } catch (Exception e) {
            System.out.println("❌ Error verifying column order: " + e.getMessage());
            return false;
        }
    }

    public static boolean isElementPresent(WebDriver driver, By locator) {
        try {
            driver.findElement(locator);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public static File waitForFileDownload(String downloadDir, String filePrefix, int timeoutInSeconds) throws InterruptedException {
        File dir = new File(downloadDir);
      //  File[] matchedFiles;

        int waited = 0;
        while (waited < timeoutInSeconds) {
            File[] files = dir.listFiles((d, name) -> name.startsWith(filePrefix) && name.endsWith(".xlsx"));
            if (files != null && files.length > 0) {
                // Return the most recent matching file
                Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
                return files[0];
            }
            Thread.sleep(1000);
            waited++;
        }
        return null; // file not found
    }

    public static void highlightElement(WebDriver driver, WebElement element) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].style.border='3px solid red'", element);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        js.executeScript("arguments[0].style.border=''", element);
    }

    public static void waitForElementToBeClickable(WebDriver driver, WebElement element) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
            wait.until(ExpectedConditions.elementToBeClickable(element));
        } catch (TimeoutException e) {
            throw new RuntimeException("Element not clickable after waiting: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException("Error while waiting for element to be clickable: " + e.getMessage());
        }
    }
        public static void highlightElement(WebDriver driver, WebElement element, String color) {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            // Store original style
            String originalStyle = element.getAttribute("style");

            // Apply new style (blue background for ODM)
            js.executeScript(
                    "arguments[0].setAttribute('style', 'background-color: " + color + " !important; border: 2px solid " + color + ";');",
                    element
            );

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            // Restore original style
            if (originalStyle != null) {
                js.executeScript("arguments[0].setAttribute('style', arguments[1]);", element, originalStyle);
            } else {
                js.executeScript("arguments[0].removeAttribute('style');", element);
            }
        }

    public static void highlightRangeArchitecture(WebDriver driver, WebElement element) {
        String greyColor = "#E8E8E8"; // Light grey color matching the UI
        highlightElement(driver, element, greyColor);
    }

   // private static final String SCREENSHOT_DIR = "test-output/screenshots/";

    static {
        createScreenshotDirectory();
    }

    private static void createScreenshotDirectory() {
        File directory = new File(SCREENSHOT_DIR);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public static void takeScreenshot(WebDriver driver, String screenshotName) {
        try {
            ExtentTest test = ExtentTestManager.getTest();
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = screenshotName + "_" + timestamp + ".png";
            String fullPath = SCREENSHOT_DIR + fileName;
            String relativePath = "../Screenshots/" + fileName; // for Extent HTML to resolve

            File dest = new File(fullPath);
         //   String fileName = testName + "_" + timestamp + ".png";
         //   String fileName = screenshotName + "_" + timestamp + ".png";

         //   File dest = new File(SCREENSHOT_DIR + fileName);

            FileUtils.copyFile(src, dest);
            System.out.println("Screenshot saved: " + dest.getAbsolutePath());

            // ✅ Attach screenshot to Extent Report
          //  ExtentTestManager.getTest().addScreenCaptureFromPath(dest.getAbsolutePath(), screenshotName);
            ExtentTestManager.getTest().info(screenshotName,
                    MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build()
            );

        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
        }
    }

    public static String takeScreenshotAndReturnPath(WebDriver driver, String testName) {
        try {
            TakesScreenshot ts = (TakesScreenshot) driver;
            File src = ts.getScreenshotAs(OutputType.FILE);

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = testName + "_" + timestamp + ".png";
            File dest = new File(SCREENSHOT_DIR + fileName);

            FileUtils.copyFile(src, dest);
            return dest.getAbsolutePath();
        } catch (IOException e) {
            System.err.println("Failed to capture screenshot: " + e.getMessage());
            return null;
        }
    }

    /**
     * Simple wait method for specified seconds
     * @param seconds Number of seconds to wait
     */
    public static void waitForSeconds(int seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Wait interrupted", e);
        }
    }

    /**
     * Waits for element to be visible with custom timeout and polling
     * @param driver WebDriver instance
     * @param element WebElement to wait for
     * @param timeoutSeconds Maximum time to wait
     * @param pollingIntervalMs Polling interval in milliseconds
     * @return true if element becomes visible, false if timeout
     */
    public static boolean waitForElementVisibilityWithPolling(WebDriver driver, WebElement element, int timeoutSeconds, int pollingIntervalMs) {
        long endTime = System.currentTimeMillis() + (timeoutSeconds * 1000L);

        while (System.currentTimeMillis() < endTime) {
            try {
                if (element.isDisplayed()) {
                    return true;
                }
            } catch (Exception ignored) {
                // Element not found or not visible yet
            }

            try {
                Thread.sleep(pollingIntervalMs);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
}


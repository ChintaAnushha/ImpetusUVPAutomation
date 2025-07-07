    package com.impetus.utils;

    import com.aventstack.extentreports.ExtentReports;
    import com.aventstack.extentreports.ExtentTest;
    import com.aventstack.extentreports.reporter.ExtentSparkReporter;
    import com.aventstack.extentreports.reporter.configuration.Theme;

    import java.io.File;
    import java.util.Properties;

    public class ExtentManager {
        private static ExtentReports extent;
        private static final String REPORT_DIR = System.getProperty("user.dir") + "/test-output/ExtentReport";
        private static final String REPORT_PATH = REPORT_DIR + "/UVPImpetusReport.html";

        public static ExtentReports getInstance() {
            if (extent == null) {
                extent = createInstance();
            }
            return extent;
        }

        private static ExtentReports createInstance() {
            new File(REPORT_DIR).mkdirs();
            //   ExtentSparkReporter sparkReporter = new ExtentSparkReporter(ApplicationConstants.EXTENT_REPORT_PATH + "/TestReport.html");
            ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
            sparkReporter.config().setDocumentTitle("UVP Automation Report");
            sparkReporter.config().setReportName("Test Execution Report");
            sparkReporter.config().setTheme(Theme.DARK);

            ExtentReports extent = new ExtentReports();
            extent.attachReporter(sparkReporter);

            // Optional: Add environment/system info
            extent.setSystemInfo("Project", "UVP-RAModule");
            extent.setSystemInfo("Environment", "SIT");
            extent.setSystemInfo("Tester", "Anusha");
            extent.setSystemInfo("Browser", "Chrome");

            return extent;
        }
    }
//OB: ExtentReports extent instance created here. That instancex     can be reachable by getReporter() method.
//
//    public class ExtentManager {
//        Properties properties=new Properties();
//
//        private static ExtentReports extent;
//        private static ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
//        private static String reportFileName = "UVP Impetus Test Report"+".html";
//        private static String fileSeperator = System.getProperty("file.separator");
//        static String reportFilepath = System.getProperty("user.dir") +fileSeperator+ "ExtentReport";
//        private static String reportFileLocation =  reportFilepath +fileSeperator+reportFileName;
//
//        public static ExtentReports getInstance() {
//            if (extent == null)
//                createInstance();
//            return extent;
//        }
//
//        public static ExtentReports createInstance() {
//            System.out.println("File name is:::::"+reportFilepath);
//            String fileName = getReportPath(reportFilepath);
//            ExtentSparkReporter reporter = new ExtentSparkReporter(fileName);
//            reporter.config().setReportName(reportFileName);
//            reporter.config().setTheme(Theme.DARK);
//            extent = new ExtentReports();
//            extent.attachReporter(reporter);
//
//            // Optional: Add environment/system info
//            extent.setSystemInfo("Project", "UVP-RAModule");
//            extent.setSystemInfo("Environment", "UAT");
//            extent.setSystemInfo("Tester", "Anusha");
//            extent.setSystemInfo("Browser", "Chrome");
//
//            return extent;
//        }
//
//
//
//        //Create the report path
//        static String getReportPath(String path) {
//            File testDirectory = new File(path);
//            if (!testDirectory.exists()) {
//                if (testDirectory.mkdir()) {
//                    System.out.println("Directory: " + path + " is created!" );
//                    return reportFileLocation;
//                } else {
//                    System.out.println("Failed to create directory: " + path);
//                    return System.getProperty("user.dir");
//                }
//            } else {
//                System.out.println("Directory already exists: " + path);
//            }
//            return reportFileLocation;
//        }
//
//        // Manage ExtentTest per thread (per test)
//        public static void setExtentTest(ExtentTest test) {
//            extentTest.set(test);
//        }
//
//        public static ExtentTest getExtentTest() {
//            return extentTest.get();
//        }
//
//        public static void removeExtentTest() {
//            extentTest.remove();
//        }


   // }


package com.impetus.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

public class ExtentTestManager {

    private static final ThreadLocal<ExtentTest> extentTest = new ThreadLocal<>();
    private static final ExtentReports extent = ExtentManager.getInstance();


   // static Map<Integer, ExtentTest> extentTestMap = new HashMap<Integer, ExtentTest>();
   // static ExtentReports extent = ExtentManager.getInstance();;

    public static synchronized ExtentTest getTest() {
      //  return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));
        return extentTest.get();
    }

    public static synchronized void endTest() {
      //  ExtentManager.getInstance().flush();

      //  extent.flush();

        // ✅ Proper cleanup
        int threadId = (int) (long) Thread.currentThread().getId();
     //   extentTestMap.remove(threadId);  // Clean map
        extentTest.remove();
    }

    public static synchronized ExtentTest startTest(String testName) {
    //    ExtentReports extent = ExtentManager.getInstance();
        ExtentTest test = extent.createTest(testName);
      //  extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
        extentTest.set(test);
        return test;
    //    ExtentTest test = extent.createTest(testName);
      //  extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
//        return test;
    }
}

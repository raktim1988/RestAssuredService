/**
 * 
 */
package test.util.Listeners;

/**
 * @author 382022
 *
 */

import com.relevantcodes.extentreports.LogStatus;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import test.util.ExtentReports.ExtentManager;
import test.util.ExtentReports.ExtentTestManager;


public class TestListener implements ITestListener {

   private static String getTestMethodName(ITestResult iTestResult) {
       return iTestResult.getMethod().getConstructorOrMethod().getName();
   }
   
   //After ending all tests, below method runs.
   @Override
   public void onFinish(ITestContext iTestContext) {

       String urlReport = "D:/restAssured/Test/ExtentReports/ExtentReportResults.html";
       ExtentTestManager.endTest();
       ExtentManager.getReporter().flush();
   }

   @Override
   public void onTestStart(ITestResult iTestResult) {
       //Start operation for extentreports.
       ExtentTestManager.startTest(iTestResult.getMethod().getMethodName(),"");
   }

   @Override
   public void onTestSuccess(ITestResult iTestResult) {
       //Extentreports log operation for passed tests.
       ExtentTestManager.getTest().log(LogStatus.PASS, "Test passed");
   }

   @Override
   public void onTestFailure(ITestResult iTestResult) {
       Object testClass = iTestResult.getInstance();

       //Extentreports log and screenshot operations for failed tests.
       ExtentTestManager.getTest().log(LogStatus.FAIL,"Test Failed","TestCaseFailed");
   }

   @Override
   public void onTestSkipped(ITestResult iTestResult) {
       //Extentreports log operation for skipped tests.
       ExtentTestManager.getTest().log(LogStatus.SKIP, "Test Skipped");
   }

   @Override
   public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
       System.out.println("Test failed but it is in defined success ratio " + getTestMethodName(iTestResult));
   }

@Override
public void onStart(ITestContext context) {
	// TODO Auto-generated method stub
	
}

}
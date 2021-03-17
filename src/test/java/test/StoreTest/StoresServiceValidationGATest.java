package test.StoreTest;

import io.restassured.response.Response;
import junit.framework.Assert;
import org.testng.annotations.Test;
import test.bean.ObjectMapperBean;
import test.util.APITestingUtilities;
import test.util.ConstantFields;
import test.util.ExtentReports.ExtentTestManager;

import java.io.IOException;
import java.util.List;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;

/**
 * @author 382022
 *
 */
public class StoresServiceValidationGATest {
	
	/*
	 * This is the first Rest Assured Test for our Store Service
	 */
	@Test(description="Test JSON response, if it contains all required attributes")
	  public void testStoreService() {
		  
	    final String xAuth =  ConstantFields.authKey;
	      
		  Response resp = given()
	    		  .headers("X-Authorization",xAuth)
	    		  .expect()
	    		    .statusCode(200)
	    		    .log().ifError()
	              .when()
	              .get(ConstantFields.apiURLGA);
		  
		  ExtentTestManager.getTest().setDescription("Test Store Service Status Code");
	  }
	  
	  /*
	   * This is to validate the JSON body for the stores service response
	   */
	  
	  @Test(description = "Validate is JSON contains all the proper attributes")
	  public void testStoreJSON() {
		  
		  final String xAuth =  ConstantFields.authKey;
	      
		  Response resp = given()
	    		  .headers("X-Authorization",xAuth)
	    		  .expect()
	    		    .statusCode(200)
	    		    .log().ifError()
	              .when()
	              .get(ConstantFields.apiURLGA);
	      System.out.println(resp.asString());
		      //System.out.println(resp.asString());
		      
		      String respJSONString = resp.asString();
		      if (respJSONString != null )
		    		  {
		    	  Assert.assertTrue(respJSONString.contains("id"));
		    	  Assert.assertTrue(respJSONString.contains("name"));
		    	  Assert.assertTrue(respJSONString.contains("phone"));
		    	  Assert.assertTrue(respJSONString.contains("openingHours"));
		    	  Assert.assertTrue(respJSONString.contains("address"));
		    	  Assert.assertTrue(respJSONString.contains("geoLocation"));
		    	  
		    		  }
		      ExtentTestManager.getTest().setDescription("Json body contains proper attributes");
	  }
	  
	  /*
	   * JSON object for each field
	   */
	  @Test(description= "JSON response : all list of objects contains every necessary fields")
	  public void testJSONObject() throws IOException {

		  // API call which will return Json Object
		  final String xAuth =  ConstantFields.authKey;
	      
		  Response resp = given()
	    		  .headers("X-Authorization",xAuth)
	    		  .expect()
	    		    .statusCode(200)
	    		    .log().ifError()
	              .when()
	              .get(ConstantFields.apiURLGA);
	 /*
	  * Test for the JSON response	  
	  */
	      List<ObjectMapperBean> objMapperBeanList = APITestingUtilities.parseJSONdata(resp.asString());

	      try {
	    	  if ((objMapperBeanList != null)||(!objMapperBeanList.isEmpty()))
	    	  {
				  IntStream.rangeClosed(0, objMapperBeanList.size() - 1).forEach(i -> {
					  Assert.assertNotNull(objMapperBeanList.get(i).getName());
					  Assert.assertNotNull(objMapperBeanList.get(i).getPhone());
					  Assert.assertNotNull(objMapperBeanList.get(i).getOpeningHours());
					  Assert.assertNotNull(objMapperBeanList.get(i).getAddress());
					  Assert.assertNotNull(objMapperBeanList.get(i).getGeoLocation());
				  });
	    	  }
		} catch (Exception e) {
			// TODO catch block
			e.printStackTrace();
		}
	      
	      ExtentTestManager.getTest().setDescription("Deep Testing for the list of JSON objects to verify fields");
		}

	  
	  }

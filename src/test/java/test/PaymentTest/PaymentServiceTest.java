package test.PaymentTest;

import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.bean.PaymentBean;
import test.bean.PaymentServiceBean;
import test.util.APITestingUtilities;
import test.util.PaymentServiceTestHelper;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static io.restassured.RestAssured.given;

/**
 * This is the Test class to test Payment Service
 */
public class PaymentServiceTest {

    private PaymentServiceBean paymentServiceBean;
    private static String payID;

    {
        try {
            paymentServiceBean = PaymentServiceTestHelper.testPrerequisites();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    @Test(description = "Test Payment Service Status , if it's OK or Bad Request", priority = 1)
    public void startPaymentService() throws IOException {
        JSONObject paymentRequest = new JSONObject();
        paymentRequest.put( "orderId", paymentServiceBean.getOrderID() );
        paymentRequest.put( "description", "E2ETest" );
        paymentRequest.put( "returnUrl", "https://wijnbox-beta.ah.nl/" );
        paymentRequest.put( "paymentMethod", "ideal" );
        paymentRequest.put( "idealIssuerId", 10 );

        String urlPaymentStart = "https://api-tst.ah.nl/v2/payments/";

        //Check for the proper happy flow
        Response paymentResponse = given()
                .contentType( "application/json" ).
                        headers( "Authorization", "Bearer " + paymentServiceBean.getAccessToken() ).
                        body( paymentRequest.toJSONString() ).
                        expect().
                        statusCode( 201 ).
                        when().
                        post( urlPaymentStart );


        //Parse the resposne into a bean and and validate

        PaymentBean paymentBean = APITestingUtilities.mapJsonData( paymentResponse.asString() );

        String paymentID = paymentBean.getId();
        String returnUrl = paymentBean.getPaymentUrl();
        String status = paymentBean.getStatus();

        payID = paymentID;

        Assert.assertNotNull( paymentID );
        Assert.assertNotNull( returnUrl );
        Assert.assertNotNull( status );

    }

    @Test(description = "Check payment Status of the Txn", priority = 2)
    public void checkPaymentStatus() throws IOException {

        String checkpaymentUrl = "https://api-tst.ah.nl/v2/payments/" + payID + "/check-status";

        Response checkStatusResponse = given()
                .headers( "Authorization", "Bearer " + paymentServiceBean.getAccessToken() ).
                        expect().
                        statusCode( 200 ).
                        when().
                        get( checkpaymentUrl );

        //Parse the response in a bean and validate the response
        if (checkStatusResponse.statusCode() == 200) {
            PaymentBean paymentBean = APITestingUtilities.mapJsonData( checkStatusResponse.asString() );

            String paymentID = paymentBean.getId();
            String returnUrl = paymentBean.getPaymentUrl();
            String status = paymentBean.getStatus();

            Assert.assertNotNull( paymentID );
            Assert.assertNotNull( status );

            if (status.equals( "pending" )) {

                Assert.assertNotEquals( returnUrl, "null" );
            }

            if (returnUrl.equals( "null" )) {
                Assert.assertNotSame( status, "pending", "Payment Status has been moved from pending" );
            }
        }


    }

    @Test(description = "Check for the Ideal Issuer List", priority = 3)
    public void checkIdealIssuers() {

        String getIdealUrl = "http://api-tst.ah.nl/v2/payments/ideal/issuers";

        Response updateMembership = given()
                .headers( "Authorization", "Bearer " + paymentServiceBean.getAccessToken() ).
                        expect().
                        statusCode( 200 ).
                        when().
                        get( getIdealUrl );

        Assert.assertNotNull( updateMembership.asString() );
        //Parse the response and validate

        Map[] idealIssuerList = updateMembership.as( Map[].class );

        for (Map issuerElement : idealIssuerList) {

            Assert.assertNotNull( issuerElement.get( "id" ) );
            Assert.assertNotNull( issuerElement.get( "name" ) );
        }

    }
}

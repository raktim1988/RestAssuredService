package test.util;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;
import test.bean.PaymentServiceBean;

import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PaymentServiceTestHelper {

    /*
     * This is the helper method for Payment Service test
     */
    public static PaymentServiceBean testPrerequisites() throws IOException, NoSuchAlgorithmException {

        final String client_id = "wijnbox";
        final String client_secret = "f36d4f864fe8";

        //Call for getting the access token

        String requestParam = new StringBuilder().append( "?client_id=" ).append( client_id ).append( "&client_secret=" ).append( client_secret ).append( "&grant_type=" ).append( "client_credentials" ).toString();
        String urlTest = "https://api-tst.ah.nl/secure/oauth/token" + requestParam;

        RequestSpecification request = given();
        Response response = request.post( urlTest );
        Map responseJson = response.as( Map.class );

        String accessAuthToken = (String) responseJson.get( "access_token" );

        //Call for getting the anonymous Member ID

        String urlMember = "https://api-tst.ah.nl/member-service/v1/members/anonymous";

        JSONObject requestParams = new JSONObject();

        JSONObject jsonAddressbean = new JSONObject();
        JSONObject jsonNameBean = new JSONObject();

        jsonAddressbean.put( "city", "Heemskerk" );
        jsonAddressbean.put( "country", "NLD" );
        jsonAddressbean.put( "houseNumber", "55" );
        jsonAddressbean.put( "houseNumberExtra", "A" );
        jsonAddressbean.put( "postalCode", "1965NW" );
        jsonAddressbean.put( "street", "Zuilenburg" );

        jsonNameBean.put( "firstName", "test" );
        jsonNameBean.put( "lastName", "tester" );

        requestParams.put( "domain", "NLD" );
        requestParams.put( "emailAddress", "test@test.nl" );
        requestParams.put( "address", jsonAddressbean );
        requestParams.put( "id", 0 );
        requestParams.put( "name", jsonNameBean );
        requestParams.put( "password", "abc123" );

        Response resp = given()
                .contentType( "application/json" ).
                        headers( "Authorization", "Bearer " + accessAuthToken ).
                        body( requestParams.toJSONString() ).
                        when().
                        post( urlMember );

        Map resJson = resp.as( Map.class );

        //Generating Checksum

        String stringToHash = resJson.get( "id" ) + "735243";
        MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
        messageDigest.update( stringToHash.getBytes() );
        byte[] digiest = messageDigest.digest();
        String hashedOutput = DatatypeConverter.printHexBinary( digiest );

        //Call to get the actual token
        String requestParamsToken = new StringBuilder().append( "?client_id=" ).append( client_id ).append( "&client_secret=" ).append( client_secret )
                .append( "&grant_type=" ).append( "member" )
                .append( "&member=" ).append( resJson.get( "id" ) )
                .append( "&checksum=" ).append( hashedOutput )
                .toString();

        String authUrl = "https://api-tst.ah.nl/secure/oauth/token" + requestParamsToken;

        Response responseAuthToken = given()
                .contentType( ContentType.JSON ).
                        when().
                        post( authUrl );

        Map resAuthJson = responseAuthToken.as( Map.class );
        String accessToken = (String) resAuthJson.get( "access_token" );
        //Call for updating the Member ID


        String memberUpdateUrl = "https://api-tst.ah.nl/member-service/v1/members/" + resJson.get( "id" );

        JSONObject requestParamsMember = new JSONObject();

        jsonNameBean.put( "firstName", "loadtest" );
        jsonNameBean.put( "lastName", "loadtest" );

        jsonAddressbean.put( "city", "HEERHUGOWAARD" );
        jsonAddressbean.put( "country", "NLD" );
        jsonAddressbean.put( "houseNumber", "13" );
        jsonAddressbean.put( "houseNumberExtra", "" );
        jsonAddressbean.put( "postalCode", "1704wc" );
        jsonAddressbean.put( "street", "Potvis" );

        requestParamsMember.put( "gender", "male" );
        requestParamsMember.put( "name", jsonNameBean );
        requestParamsMember.put( "businessPage", "false" );
        requestParamsMember.put( "address", jsonAddressbean );
        requestParamsMember.put( "dateOfBirth", "1972-07-17" );
        requestParamsMember.put( "phoneNumber", "0650747029" );
        requestParamsMember.put( "emailAddress", "test_14_02_2019_16_49@mailinator.com" );

        Response responseUpdate = given()
                .contentType( ContentType.JSON ).
                        headers( "Authorization", "Bearer " + accessToken ).
                        body( requestParamsMember.toJSONString() ).
                        when().
                        put( memberUpdateUrl );

        //Call for adding the membership

        String addmemberUrl = "https://api-tst.ah.nl/member-service/v1/members/" + resJson.get( "id" ) + "/memberships";

        JSONObject memberCode = new JSONObject();
        memberCode.put( "code", "WIJNBOX" );

        Response updateMembership = given()
                .contentType( "application/json" ).
                        headers( "Authorization", "Bearer " + accessToken ).
                        body( memberCode.toJSONString() ).
                        when().
                        post( addmemberUrl );

        //Call for finding the slot

        String slotServiceUrl = "https://api-tst.ah.nl/slot-service/v0/slots/anonymous?postalCode=1704WC&productIds=350771";

        Response getSlot = given()
                .contentType( "application/json" ).
                        headers( "Authorization", "Bearer " + accessToken )
                .expect()
                .statusCode( 200 ).
                        when().
                        get( slotServiceUrl );

        Map[] getSlotDate = getSlot.as( Map[].class );
        //Call for creating an Order

        String orderUrl = "https://api-tst.ah.nl/order-service/orders/v1/";

        JSONObject orderRequestBody = new JSONObject();
        orderRequestBody.put( "deliveryDate", (getSlotDate[0].get( "date" )) );
        orderRequestBody.put( "deliveryLocationNumber", 100 );
        orderRequestBody.put( "deliveryShiftCode", "YA" );
        orderRequestBody.put( "orderMethodNo", 27 );


        Response createOrder = given()
                .contentType( "application/json" ).
                        headers( "Authorization", "Bearer " + accessToken ).
                        body( orderRequestBody.toJSONString() ).
                        when().
                        post( orderUrl );

        //Call for Submitting an Order
        String orderID = createOrder.getBody().asString();
        String submitOrderUrl = "https://api-tst.ah.nl/order-service/orders/v1/" + orderID + "/submit";

        Response submitOrder = given()
                .contentType( "application/json" ).
                        headers( "Authorization", "Bearer " + accessToken )
                .expect()
                .statusCode( 204 ).
                        when().
                        post( submitOrderUrl );
        PaymentServiceBean paymentServiceBean = new PaymentServiceBean();

        paymentServiceBean.setAccessToken( accessToken );
        paymentServiceBean.setOrderID( orderID );

        return paymentServiceBean;
    }
}

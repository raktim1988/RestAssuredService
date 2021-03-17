package test.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import test.bean.ObjectMapperBean;
import test.bean.PaymentBean;

import java.io.IOException;
import java.util.List;

/**
 * @author 382022
 *
 */
public class APITestingUtilities {
	  /*
	   * Private function to return back the object parsed from the JSON
	   */
		public static List<ObjectMapperBean> parseJSONdata(String jsonString)
                throws IOException {
			ObjectMapper mapper = new ObjectMapper();

			List<ObjectMapperBean> objectMapperBean = mapper.readValue(jsonString,
					new TypeReference<List<ObjectMapperBean>>() {
					});

			return objectMapperBean;
		}

	public static PaymentBean mapJsonData(String paymentJsonString) throws IOException {

		ObjectMapper mapper = new ObjectMapper();


		PaymentBean paymentBean = mapper.readValue( paymentJsonString,
				new TypeReference<PaymentBean>() {
				} );

		return paymentBean;

	}
}

/**
 * 
 */
package test.bean;

import java.util.List;

/**
 * @author 382022
 *
 */
public class ObjectMapperBean {
    
	private Integer id;
	private String name;
	private String phone;
	private List<OpeningHoursBean> openingHours;
	private AddressBean address;
	private GeoLocationBean geoLocation;
	private String storeType;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public List<OpeningHoursBean> getOpeningHours() {
		return openingHours;
	}

	public void setOpeningHours(List<OpeningHoursBean> openingHours) {
		this.openingHours = openingHours;
	}

	public AddressBean getAddress() {
		return address;
	}

	public void setAddress(AddressBean address) {
		this.address = address;
	}

	public GeoLocationBean getGeoLocation() {
		return geoLocation;
	}

	public void setGeoLocation(GeoLocationBean geoLocation) {
		this.geoLocation = geoLocation;
	}

	public String getStoreType() {
		return storeType;
	}

	public void setStoreType(String storeType) {
		this.storeType = storeType;
	}

}

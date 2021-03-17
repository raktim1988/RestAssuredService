/**
 * 
 */
package test.bean;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @author 382022
 *
 */
public class OpeningHoursBean {

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
	private Date date;

	private String openFrom;
	private String openUntil;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getOpenFrom() {
		return openFrom;
	}

	public void setOpenFrom(String openFrom) {
		this.openFrom = openFrom;
	}

	public String getOpenUntil() {
		return openUntil;
	}

	public void setOpenUntil(String openUntil) {
		this.openUntil = openUntil;
	}

}

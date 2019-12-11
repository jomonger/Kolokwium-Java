package pl.kolokwium.dc;

import java.util.Date;

import pl.kolokwium.org.User;

public class ApplicationForHolidays extends Document{

	private Date since;
	private Date to;
	private User userWhoRequestAboutHolidays;
	
	public Date getSince() {
		return since;
	}
	public void setSince(Date since) {
		this.since = since;
	}
	public Date getTo() {
		return to;
	}
	public void setTo(Date to) {
		this.to = to;
	}
	public User getUserWhoRequestAboutHolidays() {
		return userWhoRequestAboutHolidays;
	}
	public void setUserWhoRequestAboutHolidays(User userWhoRequestAboutHolidays) {
		this.userWhoRequestAboutHolidays = userWhoRequestAboutHolidays;
	}
	
	
	
}

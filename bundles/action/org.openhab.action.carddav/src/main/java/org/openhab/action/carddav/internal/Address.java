package org.openhab.action.carddav.internal;

public class Address {
	private String country;
	private String street;
	private String postalCode;
	private String city;
	private String label;
	private Location location;
	private Contact contact;
	
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getStreet() {
		return street;
	}
	public void setStreet(String street) {
		this.street = street;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	
	public Contact getContact() {
		return contact;
	}
	public void setContact(Contact contact) {
		this.contact = contact;
	}
	
	
	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label = label;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getFull() {
		StringBuilder sb = new StringBuilder();
		if (street != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(street);
		}
		if (postalCode != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(postalCode);
		}
		if (city != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(city);
		}
		if (country != null) {
			if (sb.length() > 0) {
				sb.append(", ");
			}
			sb.append(country);
		}
		if (sb.length() == 0) {
			return null;
		}
		return sb.toString();
	}
	@Override
	public String toString() {
		return "Address [country=" + country + ", street=" + street
				+ ", postalCode=" + postalCode + ", city=" + city
				+ ", location=" + location + "]";
	}
	
	
}

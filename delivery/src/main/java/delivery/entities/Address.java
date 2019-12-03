package delivery.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data
@NoArgsConstructor
public class Address {

	private String phone;
	private String address;
	private String city;

	private String state;

	private String zip;

	private String country;
}

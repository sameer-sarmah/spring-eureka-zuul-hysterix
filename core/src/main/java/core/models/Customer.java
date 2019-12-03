package core.models;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Customer {

	private String name;

	private String email;

	private Address address;
}

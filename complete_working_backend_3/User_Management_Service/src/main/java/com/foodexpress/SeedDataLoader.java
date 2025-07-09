package com.foodexpress;
import java.util.Optional;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.foodexpress.entity.Customer;
import com.foodexpress.entity.Roles;
import com.foodexpress.repository.CustomerRepository;
import com.foodexpress.repository.RolesRepository;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class SeedDataLoader implements CommandLineRunner {

	
	private final RolesRepository rolesRepository;

	
	private final CustomerRepository customerRepository;
	

	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(String... args) throws Exception {
		if (rolesRepository.count() == 0) {
			rolesRepository.save(new Roles(1, "Admin"));
			rolesRepository.save(new Roles(2, "Customer"));
			rolesRepository.save(new Roles(3, "Delivery Agent"));
			rolesRepository.save(new Roles(4, "Restaurant Owner"));
		}

		if (customerRepository.existsById(1L)==false) {
			Optional<Roles> roles = rolesRepository.findById(1);
			Roles role = null;
			if (roles.isPresent()) {
				role = roles.get();
			}
			Customer admin= new Customer();
			admin.setFirstName("madhav");
			admin.setLastName("narasimha");
			admin.setEmail("madhav@gmail.com");
			admin.setPassword("password");
			admin.setPhoneNo("8473843456");
			admin.setAddress("vskp");
			admin.setRoles(role);
			customerRepository.save(admin);
		}

	}

}

package com.projet5.safetyNet.test.Unitaire.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.projet5.safetyNet.model.Firestation;


public class testModelFirestation {
	@Test
	void testConstructorAndGetterFirestation() {
		Firestation firestation = new Firestation("AddressTest", "5");

		assertThat(firestation.getAddress()).isEqualTo("AddressTest");
		assertThat(firestation.getStation()).isEqualTo("5");
	}

	@Test
	void testSetterFirestation() {
		Firestation firestation = new Firestation("AddressTest", "5");

		firestation.setAddress("AddressModif");
		firestation.setStation("10");

		assertThat(firestation.getAddress()).isEqualTo("AddressModif");
		assertThat(firestation.getStation()).isEqualTo("10");
	}

	@Test
	void testFIrestationHashcode() {
	    Firestation firestation1 = new Firestation("AddressTest", "2");
	    Firestation firestation2 = new Firestation("AddressTest", "2");
	    
	    assertThat(firestation1.hashCode()).isEqualTo(firestation2.hashCode());
	}

	@Test
	void testFirestationToString() {
	    Firestation firestation1 = new Firestation("AddressTest", "2");
	    
	    String expectedToString = "Firestation(address=AddressTest, station=2)";
	    
	    assertThat(firestation1.toString()).isEqualTo(expectedToString);
	}
	
	@Test
	void testFirestationEquals() {
	    Firestation firestation1 = new Firestation("AddressTest", "2");
	    Firestation firestation2 = new Firestation("AddressTest", "2");
	    Firestation firestation3 = new Firestation("AddressDifferent", "5");

	    assertThat(firestation1).isEqualTo(firestation2); // Égalité
	    assertThat(firestation1).isNotEqualTo(firestation3); // Non-égalité
	}

}

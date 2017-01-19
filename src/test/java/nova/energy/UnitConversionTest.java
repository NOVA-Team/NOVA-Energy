/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nova.energy;

import org.assertj.core.data.Offset;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author ExE Boss
 */
public class UnitConversionTest {

	public UnitConversionTest() {
	}

	@Test
	public void testConvert() {
		UnitConversion RF_JOULE = UnitConversion.getConvertion(Unit.REDFLUX, Unit.JOULE).get();
		UnitConversion JOULE_RF = UnitConversion.getConvertion(Unit.JOULE, Unit.REDFLUX).get();

		assertThat(RF_JOULE.convert(24)).isEqualTo(5, Offset.offset(1 / 10_000_000D));
		assertThat(JOULE_RF.convert(5)).isEqualTo(24, Offset.offset(1 / 10_000_000D));
	}
}

package org.openhab.core.library.items;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.OnOffType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.types.State;

/**
 * @author Andreas Brenk
 * @since 1.8.0
 */
public class DimmerItemTest {

	@Test
	public void testStateAsPercent() {
		doTestStateAs(OnOffType.OFF, PercentType.valueOf("0"));
		doTestStateAs(OnOffType.ON, PercentType.valueOf("100"));

		doTestStateAs(DecimalType.valueOf("0"), PercentType.valueOf("0"));
		doTestStateAs(DecimalType.valueOf("42"), PercentType.valueOf("42"));
		doTestStateAs(DecimalType.valueOf("100"), PercentType.valueOf("100"));

		doTestStateAs(PercentType.valueOf("0"), PercentType.valueOf("0"));
		doTestStateAs(PercentType.valueOf("42"), PercentType.valueOf("42"));
		doTestStateAs(PercentType.valueOf("100"), PercentType.valueOf("100"));
	}

	@Test
	public void testStateAsDecimal() {
		doTestStateAs(OnOffType.OFF, DecimalType.valueOf("0"));
		doTestStateAs(OnOffType.ON, DecimalType.valueOf("100"));

		doTestStateAs(DecimalType.valueOf("0"), DecimalType.valueOf("0"));
		doTestStateAs(DecimalType.valueOf("42"), DecimalType.valueOf("42"));
		doTestStateAs(DecimalType.valueOf("100"), DecimalType.valueOf("100"));

		doTestStateAs(PercentType.valueOf("0"), DecimalType.valueOf("0"));
		doTestStateAs(PercentType.valueOf("42"), DecimalType.valueOf("42"));
		doTestStateAs(PercentType.valueOf("100"), DecimalType.valueOf("100"));
	}

	@Test
	public void testStateAsOnOff() {
		doTestStateAs(OnOffType.ON, OnOffType.ON);
		doTestStateAs(OnOffType.OFF, OnOffType.OFF);

		doTestStateAs(DecimalType.valueOf("0"), OnOffType.OFF);
		doTestStateAs(DecimalType.valueOf("42"), OnOffType.ON);
		doTestStateAs(DecimalType.valueOf("100"), OnOffType.ON);

		doTestStateAs(PercentType.valueOf("0"), OnOffType.OFF);
		doTestStateAs(PercentType.valueOf("42"), OnOffType.ON);
		doTestStateAs(PercentType.valueOf("100"), OnOffType.ON);
	}

	private void doTestStateAs(State initialState, State expectedState) {
		Class<? extends State> expectedType = expectedState.getClass();

		DimmerItem dimmerItem = new DimmerItem("Test_Dimmer");
		dimmerItem.setState(initialState);

		State actualState = dimmerItem.getStateAs(expectedType);

		assertThat(actualState, instanceOf(expectedType));
		assertThat(actualState, equalTo(expectedState));
	}

}

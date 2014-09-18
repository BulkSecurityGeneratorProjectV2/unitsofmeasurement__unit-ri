/**
 *  Unit-API - Units of Measurement API for Java
 *  Copyright 2010-2014, Jean-Marie Dautelle, Werner Keil, V2COM and individual
 *  contributors by the @author tag.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package tec.units.ri;

import static javax.measure.format.FormatBehavior.LOCALE_NEUTRAL;

import java.text.ParsePosition;

import javax.measure.Measurement;
import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.IncommensurableException;
import javax.measure.UnconvertibleException;
import javax.measure.format.ParserException;
import javax.measure.function.UnitConverter;

import tec.units.ri.format.MeasurementFormat;

/**
 * An amount of measurement, consisting of a V and a Unit. BaseMeasurement
 * objects are immutable.
 *
 * @see Unit
 * @author <a href="mailto:units@catmedia.us">Werner Keil</a>
 * @param <Q>
 *            The type of the quantity.
 * @version 0.6, $Date: 2014-08-03 $
 */
public class BaseMeasurement<Q extends Quantity<Q>> extends
		AbstractMeasurement<Q> implements Comparable<BaseMeasurement<Q>> {
	// FIXME Bug 338334 overwrite equals()

	/**
	 *
	 */
	// private static final long serialVersionUID = 1794798190459768561L;

	private final Number value;

	/*
	 * (non-Javadoc)
	 *
	 * @see AbstractMeasurement#equals(java.lang.Object)
	 */
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (this.getClass() == obj.getClass()) {
			return super.equals(obj);
		} else {
			if (obj instanceof Quantity) {
				@SuppressWarnings("rawtypes")
				Quantity m = (Quantity) obj;
				if (m.getValue().getClass() == this.getValue().getClass()
						&& m.getUnit().getClass() == this.getUnit().getClass()) {
					return super.equals(obj);
				} else {
					// if (this.getQuantityUnit() instanceof AbstractUnit<?>) {
					// if
					// }
					return super.equals(obj);
				}
			}
			return false;
		}
	}

	/**
	 * Indicates if this measure is exact.
	 */
	private final boolean isExact;

	/**
	 * Indicates if this measure is big.
	 */
	private final boolean isBig;

	/**
	 * Holds the exact value (when exact) stated in this measure unit.
	 */
	// private long exactValue;

	protected BaseMeasurement(Number number, Unit<Q> unit) {
		super(unit);
		value = number;
		isExact = false;
		isBig = false;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see Measurement#doubleValue(javax.measure.Unit)
	 */
	@Override
    public double doubleValue(Unit<Q> unit) {
		Unit<Q> myUnit = getUnit();
		try {
			UnitConverter converter = unit.getConverterTo(myUnit);
			return converter.convert(numberValue().doubleValue());
		} catch (UnconvertibleException e) {
			throw e;
		} // catch (IncommensurableException e) {
			// throw new IllegalArgumentException(e.getMessage());
			// }
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.uomo.units.AbstractMeasurement#longValue(javax.measure
	 * .Unit)
	 */
    public long longValue(Unit<Q> unit) {
		Unit<Q> myUnit = getUnit();
		try {
			UnitConverter converter = unit.getConverterToAny(myUnit);
//			if ((numberValue() instanceof BigDecimal || numberValue() instanceof BigInteger)
//					&& converter instanceof AbstractConverter) {
//				return (((AbstractConverter) converter).convert(
//						BigDecimal.valueOf(numberValue().longValue()),
//						MathContext.DECIMAL128)).longValue();
//			} else {
				double result = doubleValue(unit);
				if ((result < Long.MIN_VALUE) || (result > Long.MAX_VALUE)) {
					throw new ArithmeticException("Overflow (" + result + ")");
				}
				return (long) result;
//			}
		} catch (UnconvertibleException e) {
			throw e;
		} catch (IncommensurableException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.measure.Quantity#numberValue()
	 */
    public Number getValue() {
		return value;
	}

	/**
	 * Indicates if this measured amount is exact. An exact amount is guarantee
	 * exact only when stated in this measure unit (e.g.
	 * <code>this.longValue()</code>); stating the amount in any other unit may
	 * introduce conversion errors.
	 *
	 * @return <code>true</code> if this measure is exact; <code>false</code>
	 *         otherwise.
	 */
	public boolean isExact() {
		return isExact;
	}

	/**
	 * Indicates if this measured amount is a big number, i.E. BigDecimal or
	 * BigInteger. In all other cases this would be false.
	 *
	 * @return <code>true</code> if this measure is big; <code>false</code>
	 *         otherwise.
	 */
	public boolean isBig() {
		return isBig;
	}

    public String toString() {
		return String.valueOf(numberValue()) + " " + String.valueOf(getUnit());
	}

	public int compareTo(BaseMeasurement<Q> o) {
		// TODO Auto-generated method stub
		return 0;
	}
	
    /**
     * Returns the measurement of unknown type corresponding to the specified representation.
     * This method can be used to parse dimensionless quantities.<br/><code>
     *     Measurement<Number, Dimensionless> proportion = AbstractMeasurement.of("0.234").asType(Dimensionless.class);
     * </code>
     *
     * <p> Note: This method handles only
     * {@link javax.measure.unit.UnitFormat#getStandard standard} unit format
     * (<a href="http://unitsofmeasure.org/">UCUM</a> based). Locale-sensitive
     * measure formatting and parsing are handled by the {@link MeasurementFormat}
     * class and its subclasses.</p>
     *
     * @param csq the decimal value and its unit (if any) separated by space(s).
     * @return <code>MeasureFormat.getStandard().parse(csq, new ParsePosition(0))</code>
     */
    @SuppressWarnings("rawtypes")
	public static Measurement of(CharSequence csq) {
        try {
			return MeasurementFormat.getInstance(LOCALE_NEUTRAL).parse(csq, new ParsePosition(0));
		} catch (IllegalArgumentException | ParserException e) {
			throw new IllegalArgumentException(e); // TODO could we handle this differently?
		}
    }

    /**
     * Returns the scalar measure for the specified <code>float</code> stated in
     * the specified unit.
     *
     * @param floatValue the measurement value.
     * @param unit the measurement unit.
     * @return the corresponding <code>float</code> measure.
     */
    public static <Q extends Quantity<Q>> Measurement<Q> of(double value,
            Unit<Q> unit) {
        return new BaseMeasurement(value, unit);
    }
    
    // privates
	private Number numberValue() {
		return Double.valueOf(String.valueOf(value));
	}
}

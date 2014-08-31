/**
 *  Unit-API - Units of Measurement API for Java
 *  Copyright 2013-2014, Jean-Marie Dautelle, Werner Keil, V2COM and individual
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

import javax.measure.Quantity;
import javax.measure.Unit;
import javax.measure.UnconvertibleException;
import javax.measure.function.UnitConverter;

/**
 * An amount of quantity, consisting of a Number and a Unit. BaseQuantity
 * objects are immutable.
 * 
 * @see AbstractQuantity
 * @see Quantity
 * @author <a href="mailto:units@catmedia.us">Werner Keil</a>
 * @param <Q>
 *            The type of the quantity.
 * @version 0.8, $Date: 2014-08-31 $
 */
public class BaseQuantity<Q extends Quantity<Q>> extends AbstractQuantity<Q>
		implements Comparable<BaseQuantity<Q>> {
	// FIXME Bug 338334 overwrite equals()

	/**
	 * 
	 */
	// private static final long serialVersionUID = 7312161895652321241L;

	private final Number value;

	/*
	 * (non-Javadoc)
	 * 
	 * @see AbstractMeasurement#equals(java.lang.Object)
	 */
	@Override
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
	 * Holds the exact value (when exact) stated in this measure unit.
	 */
	// private long exactValue;

	/**
	 * Holds the minimum value stated in this measure unit. For inexact
	 * measures: minimum < maximum
	 */
	// private double minimum;

	/**
	 * Holds the maximum value stated in this measure unit. For inexact
	 * measures: maximum > minimum
	 */
	// private double maximum;

	public BaseQuantity(Number number, Unit<Q> unit) {
		super(unit);
		value = number;
		isExact = false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see AbstractQuantity#doubleValue(javax.measure.Unit)
	 */
	public double doubleValue(Unit<Q> unit) {
		Unit<Q> myUnit = getUnit();
		try {
			UnitConverter converter = unit.getConverterTo(myUnit);
			return converter.convert(getValue().doubleValue());
		} catch (UnconvertibleException e) {
			throw e;
		} // catch (IncommensurableException e) {
			// throw new IllegalArgumentException(e.getMessage());
			// }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see AbstractQuantity#longValue(javax.measure.Unit)
	 */
	public long longValue(Unit<Q> unit) {
//		Unit<Q> myUnit = getUnit();
		try {
//			UnitConverter converter = unit.getConverterToAny(myUnit);
//			if ((getValue() instanceof BigDecimal || getValue() instanceof BigInteger)
//					&& converter instanceof AbstractConverter) {
//				return (((AbstractConverter) converter).convert(
//						BigDecimal.valueOf(getValue().longValue()),
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
		} 
//			catch (IncommensurableException e) {
//			throw new IllegalArgumentException(e.getMessage());
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javax.measure.Quantity#getValue()
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public BaseQuantity<Q> add(AbstractQuantity<Q> that) {
		final AbstractQuantity<Q> thatToUnit = that.to(getUnit());
		return new BaseQuantity(this.getValue().doubleValue()
				+ thatToUnit.getValue().doubleValue(), getUnit());
	}

	public String toString() {
		return String.valueOf(getValue()) + " " + String.valueOf(getUnit());
	}

	@Override
	public Quantity<?> multiply(Quantity<?> that) {
		final Unit<?> unit = getUnit().multiply(that.getUnit());
		return of((getValue().doubleValue() * that.getValue().doubleValue()),
				unit);
	}

	@Override
	public BaseQuantity<Q> multiply(Number that) {
		return (BaseQuantity<Q>) of(
				(getValue().doubleValue() * that.doubleValue()), getUnit());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Quantity<Q> divide(Quantity<?> that) {
		final Unit<?> unit = getUnit().divide(that.getUnit());
		return new BaseQuantity((getValue().doubleValue() / that.getValue()
				.doubleValue()), unit);
	}

	@Override
	public Quantity<Q> divide(Number that) {
		return of(getValue().doubleValue() / that.doubleValue(), getUnit());
	}

	@Override
	public Quantity<Q> inverse() {
		@SuppressWarnings({ "rawtypes", "unchecked" })
		final Quantity<Q> m = new BaseQuantity(getValue(), getUnit()
				.inverse()); // TODO keep value same?
		return m;
	}

	@Override
	public int compareTo(BaseQuantity<Q> o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Quantity<Q> subtract(Quantity<Q> that) {
		final Quantity<Q> thatToUnit = (Quantity<Q>) that.to(getUnit());
		return new BaseQuantity(this.getValue().doubleValue()
				- thatToUnit.getValue().doubleValue(), getUnit());
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Quantity<Q> add(Quantity<Q> that) {
		final Quantity<Q> thatToUnit = (Quantity<Q>) that.to(getUnit());
		return new BaseQuantity(this.getValue().doubleValue()
				+ thatToUnit.getValue().doubleValue(), getUnit());
	}
}

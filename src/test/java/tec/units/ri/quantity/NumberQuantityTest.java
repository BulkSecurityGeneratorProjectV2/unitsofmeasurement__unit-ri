/*
 * Units of Measurement Reference Implementation
 * Copyright (c) 2005-2016, Jean-Marie Dautelle, Werner Keil, V2COM.
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of JSR-363 nor the names of its contributors may be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED
 * AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package tec.units.ri.quantity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import javax.measure.Quantity;
import javax.measure.quantity.Length;
import javax.measure.quantity.Time;

import org.junit.Assert;
import org.junit.Test;

import tec.units.ri.unit.Units;

public class NumberQuantityTest {

  @Test
  public void divideTest() {
    Quantity<Length> metre = Quantities.getQuantity(10, Units.METRE);
    Quantity<Length> result = metre.divide(10D);
    Assert.assertTrue(result.getValue().intValue() == 1);
    Assert.assertEquals(result.getUnit(), Units.METRE);

    Quantity<Time> day = Quantities.getQuantity(10, Units.DAY);
    Quantity<Time> dayResult = day.divide(Double.valueOf(2.5D));
    Assert.assertTrue(dayResult.getValue().intValue() == 4);
    Assert.assertEquals(dayResult.getUnit(), Units.DAY);
  }

  @Test
  public void addTest() {
    Quantity<Length> m = Quantities.getQuantity(10, Units.METRE);
    Quantity<Length> m2 = Quantities.getQuantity(12.5F, Units.METRE);
    Quantity<Length> m3 = Quantities.getQuantity(2.5F, Units.METRE);
    Quantity<Length> m4 = Quantities.getQuantity(5L, Units.METRE);
    Quantity<Length> result = m.add(m2).add(m3).add(m4);
    Assert.assertEquals(Integer.valueOf(29), result.getValue()); // TODO
    // precision
    Assert.assertEquals(Units.METRE, result.getUnit());
  }

  @Test
  public void addQuantityTest() {
    Quantity<Time> day = Quantities.getQuantity(1, Units.DAY);
    Quantity<Time> hours = Quantities.getQuantity(12, Units.HOUR);
    Quantity<Time> result = day.add(hours);
    Assert.assertEquals(1, result.getValue());
    Assert.assertEquals(result.getUnit(), Units.DAY);
  }

  @Test
  public void subtractTest() {
    Quantity<Length> m = Quantities.getQuantity(10, Units.METRE);
    Quantity<Length> m2 = Quantities.getQuantity(12.5, Units.METRE);
    Quantity<Length> result = m.subtract(m2);
    Assert.assertEquals(-2, result.getValue()); // TODDO precision
    Assert.assertEquals(Units.METRE, result.getUnit());
  }

  @Test
  public void subtractQuantityTest() {
    Quantity<Time> day = Quantities.getQuantity(1, Units.DAY);
    Quantity<Time> hours = Quantities.getQuantity(12, Units.HOUR);
    Quantity<Time> result = day.subtract(hours);
    Assert.assertEquals(1, result.getValue()); // TODO precision
    Assert.assertEquals(result.getUnit(), Units.DAY);
  }

  @Test
  public void multiplyTest() {
    Quantity<Length> metre = Quantities.getQuantity(10, Units.METRE);
    Quantity<Length> result = metre.multiply(10D);
    Assert.assertTrue(result.getValue().intValue() == 100);
    Assert.assertEquals(result.getUnit(), Units.METRE);
    @SuppressWarnings("unchecked")
    Quantity<Length> result2 = (Quantity<Length>) metre.multiply(Quantities.getQuantity(10, Units.METRE));
    assertEquals(100, result2.getValue().intValue());
  }

  @Test
  public void toTest() {
    Quantity<Time> day = Quantities.getQuantity(1, Units.DAY);
    Quantity<Time> hour = day.to(Units.HOUR);
    assertEquals(hour.getValue().intValue(), 24);
    assertEquals(hour.getUnit(), Units.HOUR);

    Quantity<Time> dayResult = hour.to(Units.DAY);
    assertEquals(dayResult.getValue().intValue(), day.getValue().intValue());
    assertEquals(dayResult.getValue().intValue(), day.getValue().intValue());
  }

  @Test
  public void inverseTestLength() {
    @SuppressWarnings("unchecked")
    Quantity<Length> metre = (Quantity<Length>) Quantities.getQuantity(10, Units.METRE).inverse();
    assertEquals(Float.valueOf(0.0F), Float.valueOf(metre.getValue().floatValue()));
    assertEquals("1/m", String.valueOf(metre.getUnit()));
  }

  @Test
  public void inverseTestTime() {
    Quantity<?> secInv = Quantities.getQuantity(2, Units.SECOND).inverse();
    assertEquals(Float.valueOf(0.0F), Float.valueOf(secInv.getValue().floatValue())); // TODO
    // precision
    assertEquals("1/s", String.valueOf(secInv.getUnit()));
  }

  @Test
  public void testEquals() {
    Quantity<Length> q1 = NumberQuantity.of(10, Units.METRE);
    Quantity<Length> q2 = Quantities.getQuantity(10, Units.METRE);
    assertEquals(q1, q2);
  }

  @Test
  public void testIsExact() {
    @SuppressWarnings({ "rawtypes", "unchecked" })
    NumberQuantity<Length> q1 = new NumberQuantity(10, Units.METRE);
    assertFalse(q1.isExact());
  }

}

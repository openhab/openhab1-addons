package org.openhab.persistence.jdbc.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedList;
import java.util.Queue;
//import org.apache.commons.math3.stat.StatUtils;

/**
 * Calculates the average/mean of a number series.
 * 
 * @author Helmut Lehmeyer
 * @since 1.8.0
 */
public class MovingAverage {

    private final Queue<BigDecimal> win = new LinkedList<BigDecimal>();
    private final int period;
    private BigDecimal sum = BigDecimal.ZERO;

    public MovingAverage(int period) {
        assert period > 0 : "Period must be a positive integer";
        this.period = period;
    }

    public void add(Double num) {
        add(new BigDecimal(num));
    }

    public void add(Long num) {
        add(new BigDecimal(num));
    }

    public void add(Integer num) {
        add(new BigDecimal(num));
    }

    public void add(BigDecimal num) {
        sum = sum.add(num);
        win.add(num);
        if (win.size() > period) {
            sum = sum.subtract(win.remove());
        }
    }

    public BigDecimal getAverage() {
        if (win.isEmpty())
            return BigDecimal.ZERO; // technically the average is undefined
        BigDecimal divisor = BigDecimal.valueOf(win.size());
        return sum.divide(divisor, 2, RoundingMode.HALF_UP);
    }

    public double getAverageDouble() {
        return getAverage().doubleValue();
    }

    public int getAverageInteger() {
        return getAverage().intValue();
    }
}
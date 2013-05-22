/*
 * ARX: Efficient, Stable and Optimal Data Anonymization
 * Copyright (C) 2012 - 2013 Florian Kohlmayer, Fabian Prasser
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package org.deidentifier.arx.criteria;

import org.deidentifier.arx.framework.check.distribution.Distribution;
import org.deidentifier.arx.framework.check.groupify.HashGroupifyEntry;

/**
 * The entropy l-diversity criterion
 * @author Prasser, Kohlmayer
 */
public class EntropyLDiversity extends LDiversity {

    private static final long   serialVersionUID = -354688551915634000L;

    /** Helper*/
    private final double        logL;
    /** Helper*/
    private static final double log2             = Math.log(2);

    /**
     * Creates a new instance
     * @param l
     */
    public EntropyLDiversity(int l) {
        super(l);
        logL = Math.log(l) / Math.log(2d);
    }

    @Override
    public boolean isAnonymous(HashGroupifyEntry entry) {

        Distribution d = entry.distribution;

        // If less than l values are present skip
        if (d.size() < l) { return false; }

        // Count number of elements in entrys distribution
        double totalElements = 0;
        double sum1 = 0d;

        final int[] buckets = d.getBuckets();
        for (int i = 0; i < buckets.length; i += 2) {
            if (buckets[i] != -1) { // bucket not empty
                final double frequency = buckets[i + 1];
                sum1 += frequency * log2(frequency);
                totalElements += frequency;
            }
        }

        double val = (1d / totalElements) * sum1 - log2(totalElements) * totalElements;

        // // compute entropy
        // double val = 0d;
        // for (int i = 0; i < buckets.length; i += 2) {
        // if (buckets[i] != -1) { // bucket not empty
        // final int frequency = buckets[i + 1];
        // final double p = ((double) frequency / (double) totalElements);
        // val += p * log2(p);
        // }
        // }
        val = -val;

        // check
        return val >= logL;
    }

    /**
     * Computes log 2
     * 
     * @param num
     * @return
     */
    private final double log2(final double num) {
        return Math.log(num) / log2;
    }
}

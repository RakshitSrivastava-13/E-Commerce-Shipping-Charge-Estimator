package com.jumbotail.shipping.model.enums;

/**
 * Transport modes determined by the distance between warehouse and customer.
 * <ul>
 *   <li><b>AEROPLANE</b> – used for distances &gt; 500 km (Rs 1/km/kg)</li>
 *   <li><b>TRUCK</b> – used for distances 100–500 km (Rs 2/km/kg)</li>
 *   <li><b>MINI_VAN</b> – used for distances 0–100 km (Rs 3/km/kg)</li>
 * </ul>
 */
public enum TransportMode {
    AEROPLANE,
    TRUCK,
    MINI_VAN
}

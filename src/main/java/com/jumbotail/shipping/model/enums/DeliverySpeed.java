package com.jumbotail.shipping.model.enums;

/**
 * Delivery speed options available to customers.
 * <ul>
 *   <li><b>STANDARD</b> – Rs 10 standard courier charge + calculated shipping charge</li>
 *   <li><b>EXPRESS</b> – Rs 10 standard courier charge + Rs 1.2/kg extra + calculated shipping charge</li>
 * </ul>
 */
public enum DeliverySpeed {
    STANDARD,
    EXPRESS
}

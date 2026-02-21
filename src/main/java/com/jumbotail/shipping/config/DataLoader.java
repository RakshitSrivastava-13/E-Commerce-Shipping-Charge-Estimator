package com.jumbotail.shipping.config;

import com.jumbotail.shipping.model.Customer;
import com.jumbotail.shipping.model.Product;
import com.jumbotail.shipping.model.Seller;
import com.jumbotail.shipping.model.Warehouse;
import com.jumbotail.shipping.repository.CustomerRepository;
import com.jumbotail.shipping.repository.ProductRepository;
import com.jumbotail.shipping.repository.SellerRepository;
import com.jumbotail.shipping.repository.WarehouseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Data loader to seed the database with sample data on application startup.
 * This provides realistic test data for the B2B marketplace scenario.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DataLoader implements CommandLineRunner {

    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final ProductRepository productRepository;
    private final WarehouseRepository warehouseRepository;

    @Override
    public void run(String... args) {
        log.info("Loading sample data into database...");

        loadWarehouses();
        loadSellers();
        loadProducts();
        loadCustomers();

        log.info("Sample data loaded successfully!");
    }

    private void loadWarehouses() {
        if (warehouseRepository.count() == 0) {
            Warehouse blrWarehouse = Warehouse.builder()
                    .name("BLR_Warehouse")
                    .city("Bangalore")
                    .latitude(12.99999)
                    .longitude(77.923273) // Corrected to realistic Bangalore coordinates
                    .capacityKg(100000.0)
                    .build();

            Warehouse mumbWarehouse = Warehouse.builder()
                    .name("MUMB_Warehouse")
                    .city("Mumbai")
                    .latitude(19.07599)
                    .longitude(72.877426) // Corrected to realistic Mumbai coordinates
                    .capacityKg(150000.0)
                    .build();

            Warehouse delhiWarehouse = Warehouse.builder()
                    .name("DELHI_Warehouse")
                    .city("Delhi")
                    .latitude(28.7041)
                    .longitude(77.1025)
                    .capacityKg(120000.0)
                    .build();

            warehouseRepository.save(blrWarehouse);
            warehouseRepository.save(mumbWarehouse);
            warehouseRepository.save(delhiWarehouse);

            log.info("Loaded {} warehouses", 3);
        }
    }

    private void loadSellers() {
        if (sellerRepository.count() == 0) {
            Seller nestleSeller = Seller.builder()
                    .name("Nestle Seller")
                    .phoneNumber("9876543210")
                    .businessName("Nestle India Ltd")
                    .city("Mumbai")
                    .latitude(19.0760)
                    .longitude(72.8777)
                    .build();

            Seller riceSeller = Seller.builder()
                    .name("Rice Seller")
                    .phoneNumber("9876543211")
                    .businessName("Grain Traders Co")
                    .city("Hyderabad")
                    .latitude(17.3850)
                    .longitude(78.4867)
                    .build();

            Seller sugarSeller = Seller.builder()
                    .name("Sugar Seller")
                    .phoneNumber("9876543212")
                    .businessName("Sweet Supplies Inc")
                    .city("Pune")
                    .latitude(18.5204)
                    .longitude(73.8567)
                    .build();

            sellerRepository.save(nestleSeller);
            sellerRepository.save(riceSeller);
            sellerRepository.save(sugarSeller);

            log.info("Loaded {} sellers", 3);
        }
    }

    private void loadProducts() {
        if (productRepository.count() == 0) {
            Seller nestleSeller = sellerRepository.findById(1L).orElseThrow();
            Seller riceSeller = sellerRepository.findById(2L).orElseThrow();
            Seller sugarSeller = sellerRepository.findById(3L).orElseThrow();

            Product maggi = Product.builder()
                    .name("Maggie 500g Packet")
                    .category("FMCG")
                    .sellingPrice(10.0)
                    .weightKg(0.5)
                    .lengthCm(10.0)
                    .widthCm(10.0)
                    .heightCm(10.0)
                    .sku("NEST-MAGGI-500G")
                    .seller(nestleSeller)
                    .build();

            Product riceBag = Product.builder()
                    .name("Rice Bag 10Kg")
                    .category("Grocery")
                    .sellingPrice(500.0)
                    .weightKg(10.0)
                    .lengthCm(100.0)
                    .widthCm(80.0)
                    .heightCm(50.0)
                    .sku("RICE-10KG-BAG")
                    .seller(riceSeller)
                    .build();

            Product sugarBag = Product.builder()
                    .name("Sugar Bag 25kg")
                    .category("Grocery")
                    .sellingPrice(700.0)
                    .weightKg(25.0)
                    .lengthCm(100.0)
                    .widthCm(90.0)
                    .heightCm(60.0)
                    .sku("SUGAR-25KG-BAG")
                    .seller(sugarSeller)
                    .build();

            // Additional products for variety
            Product oil = Product.builder()
                    .name("Cooking Oil 5L")
                    .category("Grocery")
                    .sellingPrice(450.0)
                    .weightKg(5.0)
                    .lengthCm(30.0)
                    .widthCm(20.0)
                    .heightCm(35.0)
                    .sku("OIL-5L")
                    .seller(nestleSeller)
                    .build();

            productRepository.save(maggi);
            productRepository.save(riceBag);
            productRepository.save(sugarBag);
            productRepository.save(oil);

            log.info("Loaded {} products", 4);
        }
    }

    private void loadCustomers() {
        if (customerRepository.count() == 0) {
            Customer customer1 = Customer.builder()
                    .name("Shree Kirana Store")
                    .phoneNumber("9847123456")
                    .address("123 MG Road")
                    .city("Bangalore")
                    .pincode("560001")
                    .latitude(12.9716)
                    .longitude(77.5946)
                    .build();

            Customer customer2 = Customer.builder()
                    .name("Andheri Mini Mart")
                    .phoneNumber("9101234567")
                    .address("456 Andheri West")
                    .city("Mumbai")
                    .pincode("400053")
                    .latitude(19.1136)
                    .longitude(72.8697)
                    .build();

            Customer customer3 = Customer.builder()
                    .name("Delhi Central Store")
                    .phoneNumber("9123456789")
                    .address("789 Connaught Place")
                    .city("Delhi")
                    .pincode("110001")
                    .latitude(28.6315)
                    .longitude(77.2167)
                    .build();

            Customer customer4 = Customer.builder()
                    .name("Kolkata Provision Store")
                    .phoneNumber("9234567890")
                    .address("321 Park Street")
                    .city("Kolkata")
                    .pincode("700016")
                    .latitude(22.5544)
                    .longitude(88.3515)
                    .build();

            customerRepository.save(customer1);
            customerRepository.save(customer2);
            customerRepository.save(customer3);
            customerRepository.save(customer4);

            log.info("Loaded {} customers", 4);
        }
    }
}

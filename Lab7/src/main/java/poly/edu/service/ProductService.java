package poly.edu.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import poly.edu.entity.Product;

import java.util.List;

public interface ProductService {
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    List<Product> findByNameContaining(String name);

    Page<Product> findAllByNameLike(String keywords, Pageable pageable);
}

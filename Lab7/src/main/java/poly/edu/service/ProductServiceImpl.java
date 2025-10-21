package poly.edu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import poly.edu.dao.ProductDAO;
import poly.edu.entity.Product;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductDAO productDAO;

    @Autowired
    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> findByPriceBetween(double minPrice, double maxPrice) {
        return productDAO.findByPriceBetween(minPrice, maxPrice);
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        return productDAO.findByNameContainingIgnoreCase(name);
    }

    @Override
    public Page<Product> findAllByNameLike(String keywords, Pageable pageable) {
        return productDAO.findAllByNameLike(keywords, pageable);
    }
}

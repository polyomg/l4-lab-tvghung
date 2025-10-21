package poly.edu.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import poly.edu.entity.Product;
import poly.edu.entity.Report;

import java.util.List;


public interface ProductDAO extends JpaRepository<Product, Integer> {

    List<Product> findByPriceBetween(double minPrice, double maxPrice);

    org.springframework.data.domain.Page<Product> findAllByNameLike(String keywords, Pageable pageable);

    @Query("SELECT o.category AS category, SUM(o.price) AS sum, COUNT(o) AS count "
            + "FROM Product o "
            + "GROUP BY o.category "
            + "ORDER BY SUM(o.price) DESC")
    List<Report> getInventoryByCategory();
    List<Product> findByNameContainingIgnoreCase(String name);

}

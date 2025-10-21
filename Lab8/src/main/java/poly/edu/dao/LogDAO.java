package poly.edu.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import poly.edu.entity.Log;

@Repository
public interface LogDAO extends JpaRepository<Log, Integer> {
}

package hiberspring.repository;

import hiberspring.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    @Query("select e from Employee e join Branch b " +
            "on e.branch.id = b.id " +
            "where size(b.products) >0 " +
            "order by e.firstName asc , e.lastName asc , length(e.position) desc " )
    List<Employee> findByBranch_ProductsCount();
}

package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pcharts;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pcharts entity.
 */
public interface PchartsRepository extends JpaRepository<Pcharts,Long> {

}

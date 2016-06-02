package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Pchart;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Pchart entity.
 */
public interface PchartRepository extends JpaRepository<Pchart,Long> {

}

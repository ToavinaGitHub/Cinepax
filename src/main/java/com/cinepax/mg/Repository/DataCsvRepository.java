package com.cinepax.mg.Repository;

import com.cinepax.mg.Model.DataCsv;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface DataCsvRepository extends JpaRepository<DataCsv,Integer> {


}

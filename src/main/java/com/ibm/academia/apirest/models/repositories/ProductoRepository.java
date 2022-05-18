package com.ibm.academia.apirest.models.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.ibm.academia.apirest.commons.models.entity.Producto;



@Repository
public interface ProductoRepository extends PagingAndSortingRepository<Producto, Long>{

}

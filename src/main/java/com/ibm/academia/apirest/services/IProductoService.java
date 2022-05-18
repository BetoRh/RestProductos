package com.ibm.academia.apirest.services;

import java.util.List;
import java.util.Optional;

import com.ibm.academia.apirest.commons.models.entity.Producto;


public interface IProductoService {
	
	public List<Producto> buscarTodos();
	public Optional<Producto> buscarPorId(Long productoId);
	public Producto guardar(Producto producto);
	public Producto actualizar(Producto producto, Long productoId);
	public void eliminar(Long productoId);
	Producto buscarExcepciones(Long productoId);

}

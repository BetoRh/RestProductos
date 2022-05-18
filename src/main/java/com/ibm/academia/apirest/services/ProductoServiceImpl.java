package com.ibm.academia.apirest.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ibm.academia.apirest.exceptions.NotFoundException;
import com.ibm.academia.apirest.commons.models.entity.Producto;
import com.ibm.academia.apirest.models.repositories.ProductoRepository;


@Service
public class ProductoServiceImpl implements IProductoService {
	
	@Autowired
	private ProductoRepository productoRepository;

	@Override
	@Transactional(readOnly = true)
	public List<Producto> buscarTodos() {

		return (List<Producto>) productoRepository.findAll();
	}

	@Override
	public Producto buscarExcepciones(Long productoId) 
	{
		Producto producto = productoRepository.findById(productoId).orElse(null);
		
		if(producto != null)
			return producto;
		else
			throw new NotFoundException(String.format("El producto con ID: %d no existe en la BD.", productoId));
	}

	@Override
	@Transactional
	public Producto guardar(Producto producto) 
	{
		return productoRepository.save(producto);
	}

	@Override
	@Transactional
	public Producto actualizar(Producto producto, Long productoId) 
	{
		Optional<Producto> productoEncontrado = productoRepository.findById(productoId);
		
		if(!productoEncontrado.isPresent())
			throw new NotFoundException(String.format("El producto con ID: %d no existe en la BD.", productoId)); 
			
		productoEncontrado.get().setNombre(producto.getNombre());
		productoEncontrado.get().setPrecio(producto.getPrecio());
		
		
		return productoRepository.save(productoEncontrado.get());
	}

	@Override
	@Transactional
	public void eliminar(Long productoId) 
	{
		Producto producto = productoRepository.findById(productoId).orElse(null);
		
		if(producto == null)
			throw new NotFoundException(String.format("El producto con ID: %d no existe en la BD.", productoId));
		
		productoRepository.deleteById(producto.getId());
	}

	
	
	@Override
	public Optional<Producto> buscarPorId(Long productoId) {
		
		return productoRepository.findById(productoId);
	}

}

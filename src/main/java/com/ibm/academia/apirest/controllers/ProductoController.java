package com.ibm.academia.apirest.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ibm.academia.apirest.exceptions.NotFoundException;
import com.ibm.academia.apirest.commons.models.entity.Producto;
import com.ibm.academia.apirest.services.IProductoService;

@RestController
@RequestMapping("/producto")
public class ProductoController {
	
	Logger logger = LoggerFactory.getLogger(ProductoController.class);
	
	@Autowired
	private IProductoService productoService;
	
	@Autowired
	private Environment environment;
	
	@Value("${server.port}")
	private Integer port;
	
	
	/**
	 * Endpoint para lista productos
	 * @return Retorna una lista de productos
	 * @NotFoundException En caso de que no existan productos
	 * @author RAJA 13/12/2021
	 */
	@GetMapping("/listar")
	public ResponseEntity<?> listarProductos(){
		
		List<Producto> productos = productoService.buscarTodos()
			.stream()
			.map(producto -> {
				producto.setPort(Integer.parseInt(environment.getProperty("local.server.port")));
				//producto.setPort(port);
				return producto;
						
			}).collect(Collectors.toList());
		
		if(productos.isEmpty())
			throw new NotFoundException("No existen productos en la base de datos");
		
		return new ResponseEntity<List<Producto>>(productos, HttpStatus.OK);
	}
	
	/**
	 * Endpoint para consultar un producto
	 * @param productoid Parametro para la busqueda del objeto producto
	 * @return Retorna una objeto Producto
	 * @NotFoundException En caso de que no encuentre una coincidencia
	 * @author RAJA 13/12/2021
	 * @throws InterruptedException 
	 */
	@GetMapping("/detalle/productoId/{productoId}")
	public ResponseEntity<?> detalleProducto(@PathVariable Long productoId) throws InterruptedException {
		
		if(productoId.equals(100L))
			throw new IllegalStateException("Perocuto No Encontrado");
		
		if(productoId.equals(7L))
			TimeUnit.SECONDS.sleep(5L);
		
		Optional<Producto>  producto = productoService.buscarPorId(productoId);
		
		if(!producto.isPresent())
			throw new NotFoundException(String.format("El producto con ID: %d no existe", productoId));
		
		producto.get().setPort(Integer.parseInt(environment.getProperty("local.server.port")));
		//producto.get().setPort(port);
		return new ResponseEntity<Producto>(producto.get(), HttpStatus.OK);
}
/**
	 * Endpoint para crear un objeto producto
	 * @param producto Objeto con la informaci�n a crear
	 * @return Retorna un objeto de tipo producto
	 * @exception En caso de que falle creando el producto
	 * @author RAJA - 25-12-2021 
	 */
	@PostMapping("/crear")
	public ResponseEntity<?> crear(@Valid @RequestBody Producto producto, BindingResult result)
	{
		Map<String, Object> validaciones = new HashMap<String, Object>();
		if(result.hasErrors())
		{
			List<String> listaErrores = result.getFieldErrors()
					.stream()
					.map(errores -> "Campo: '" + errores.getField() + "' " + errores.getDefaultMessage())
					.collect(Collectors.toList());
			validaciones.put("Errores", listaErrores);
			return new ResponseEntity<Map<String, Object>>(validaciones, HttpStatus.BAD_REQUEST);
		}
		
		
		Producto productoGuardado = null;
		
		try
		{
			productoGuardado = productoService.guardar(producto);
		}
		catch (Exception e) 
		{
			throw e;
		}
		return new ResponseEntity<Producto>(productoGuardado, HttpStatus.CREATED);
	}
	
	/**
	 * Endpoint para modificar un objeto de tipo producto
	 * @param producto Objeto producto con la informaci�n a modificar
	 * @param productoId Par�metro de b�squeda del objeto producto
	 * @return Retorna un objeto de tipo producto modificado
	 * @NotFoundException En caso de que no encuentre el producto
	 * @author RAJA - 25-12-2021
	 */
	@PutMapping("/editar/productoId/{productoId}")
	public ResponseEntity<?> actualizar(@RequestBody Producto producto, @PathVariable Long productoId)
	{
		Producto productoEncontrado = null;
		
		try
		{
			productoEncontrado = productoService.actualizar(producto, productoId);
		}
		catch (NotFoundException e) 
		{
			throw e;
		}
		catch (Exception e) 
		{
			throw e; 
		}
		return new ResponseEntity<Producto>(productoEncontrado, HttpStatus.OK);
	}
	
	/**
	 * Endpoint para elimnar un objeto producto
	 * @param productoId Par�metro de b�squeda para el producto a eliminar
	 * @return Retorna un mensaje si la respuesta es correcta
	 * @NotFoundException En caso de que no encuentre el objeto a eliminar
	 * @author RAJA - 25-12-2021
	 */
	@DeleteMapping("/eliminar/productoId/{productoId}")
	public ResponseEntity<?> eliminar(@PathVariable Long productoId)
	{
		try
		{
			productoService.eliminar(productoId);
		}
		catch (Exception e) 
		{
			throw e; 
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}
	
	
	/**
	 * Metodo de ejemplo para controlar de manera adecuada las excepciones
	 * @param productoId
	 * @return
	 */
	public Producto capturarException(Long productoId)
	{
		Producto producto = null;
		
		try
		{
			producto = productoService.buscarExcepciones(productoId);
			
		}
		catch (NotFoundException e) 
		{
			logger.info(e.getMessage());
		}
		return producto;
	}
	
	@GetMapping("/buscar/productoId/{productoId}")
	public ResponseEntity<?> buscarPorId(Long productoId){
		
		Optional<Producto> productoEncontrado = productoService.buscarPorId(productoId);
		
		if(!productoEncontrado.isPresent())
			throw new NotFoundException(String.format("El producto con id: %d no existe en la base de datos", productoId));
		
		
		return new ResponseEntity<Producto>(productoEncontrado.get(), HttpStatus.OK);
	}
}

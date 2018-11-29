package com.bolsadeideas.sringboot.app.models.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
//import org.springframework.util.FileSystemUtils;

@Service
public class UploadFileServiceImpl implements IUploadFileService {
	
	private final static String UPLOADS_FOLDER ="uploads";
	private final Logger log = LoggerFactory.getLogger(getClass());
	

	@Override
	public Resource load(String filename) throws MalformedURLException{
		Path pathFoto = getPath(filename);
		log.info("pathFoto: "+pathFoto);
		Resource recurso = null;
		
		recurso = new UrlResource(pathFoto.toUri());
		if(!recurso.exists() || !recurso.isReadable()) {
			throw new RuntimeException("Error: no se puede cargar la imagen: "+pathFoto.toString());
		}
		
		return recurso;
	}

	@Override
	public String copy(MultipartFile file) throws IOException {
		//No es comveniente tener directorios dentro de la estructura del proyecto
		//un jar o un war solo debe de ser aplicacion
		//Path directorioRecursos = Paths.get("src//main//resources//static//uploads");
		//String rootPath = directorioRecursos.toFile().getAbsolutePath();
		//opcion 1: para subir archivos
		//String rootPath = "C://Users//hecto//Desktop//Mis Documentos compu trabajo//Spring Course//Temp//uploads";
		//opcion 2: para subir archivos
		//para que no se sobreescriban los archivos
		String uniqueFilename = UUID.randomUUID().toString() + "_"+file.getOriginalFilename();
		Path rootPath = getPath(uniqueFilename);
		log.info("EL rootPath: "+rootPath);//Path relativo al proyecto
		//log.info("EL rootAbsolutPath: "+rootAbsolutPath);//Path absoluto
		//opcion 1: para subir archivos
		/*byte[] bytes = foto.getBytes();
		Path rutaCompleta = Paths.get(rootPath +"//"+foto.getOriginalFilename());
		Files.write(rutaCompleta,bytes);*/
		//opcion 2: para subir archivos
		Files.copy(file.getInputStream(), rootPath);
		return uniqueFilename;
	}

	@Override
	public boolean delete(String filename) {
		Path rootPath = getPath(filename);
		File archivo = rootPath.toFile();
		
		if(archivo.exists() && archivo.canRead()) {
			if(archivo.delete()) {
				return true;
			}
		}
		
		return false;
	}
	
	public Path getPath(String filename) {
		return Paths.get(UPLOADS_FOLDER).resolve(filename).toAbsolutePath();
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(Paths.get(UPLOADS_FOLDER).toFile());
	}

	@Override
	public void init() throws IOException {
		Files.createDirectory(Paths.get(UPLOADS_FOLDER));
	}

}

package com.algaworks.festa.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.algaworks.festa.model.Festa;
import com.algaworks.festa.repository.Festas;

@Controller
@RequestMapping("/festas")
public class FestasController {

	@Autowired
	private Festas festas;

	@GetMapping()
	public ModelAndView listar() {
		ModelAndView mv = new ModelAndView("ListaFestas");
		List <Festa> lista = festas.findAll();
		mv.addObject("festas",lista);
		mv.addObject(new Festa());
		return mv;
	}

	@PostMapping()
	public String salvar(Festa f, HttpServletRequest request) throws IOException{
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile file =  multipartRequest.getFile("file");
		f.setImage(file.getBytes());
		festas.save(f);
		return "redirect:/festas";
	}

	@RequestMapping(value = "/image/{image_id}", produces = MediaType.IMAGE_PNG_VALUE)
	public ResponseEntity<byte[]> getImage(@PathVariable("image_id") Long imageId) throws IOException {
		byte[] imageContent = festas.findOne(imageId).getImage();
		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.IMAGE_PNG);
		return new ResponseEntity<byte[]>(imageContent, headers, HttpStatus.OK);
	}

	//URL de chamada http://.../excluir/1234				
	@RequestMapping(value = "/excluir/{id}")
	public String excluirFestaByPathVariable(@PathVariable Long id, RedirectAttributes attributes)	{
		this.festas.delete(id);
		attributes.addFlashAttribute("mensagem", "Festa excluída com sucesso!");
		return "redirect:/festas";
	}

	//URL de chamada http://.../alterar/1234
	@RequestMapping(value = "/alterar/{id}")
	public ModelAndView alterar(@PathVariable Long id, RedirectAttributes attributes) {
		ModelAndView mv = new ModelAndView("ListaFestas");
		List <Festa> lista = festas.findAll();
		mv.addObject("festas",lista);
		Festa festa = festas.findOne(id);
		mv.addObject(festa);
		return mv;
	}

}
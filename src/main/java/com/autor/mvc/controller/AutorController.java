package com.autor.mvc.controller;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import com.autor.mvc.model.Autor;
import com.autor.mvc.repositorio.AutorRepositorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/autor") // anotação para toda vez que chamar o /eventos cair aqui
public class AutorController {

	// instancia dos repositorios

	@Autowired
	private AutorRepositorio ar;

	// metodo para chamar o formulario de cadastro do evento
	@GetMapping("/form")
	public String form(Autor autor) {
		return "autor/Form"; // chamando pagina html no path tamplates/eventos/
	}

	// metodo para chamar formularion de atualizar
	@GetMapping("/atualizar")
	public String atualizar(Autor autor) {
		return "autor/Atualizar"; // chamando pagina html no path tamplates/eventos/
	}

	@GetMapping("/home")
	public String getHome() {

		return "autor/Home";

	}

	/*
	 * Metodo para salvar eventos, validando o Evento que vai cadastrado BingResult
	 * verifica se tem erros, RedirectAttributtes adiciona mensagem na pagina html
	 * 
	 */
	@PostMapping
	public String salvar(@Valid Autor autor, BindingResult result, RedirectAttributes attributes) {

		// condicao para ver se tem erros
		if (result.hasErrors()) {

			return atualizar(autor);
		}

		System.out.println(autor);
		ar.save(autor);
		System.out.println("estou no slvar MEtodo");
		attributes.addFlashAttribute("mensagem", "autor salvo com sucesso!");
		// redireciona para autors
		return "redirect:/autor";
	}

	/*
	 * metodo para listar todos os eventos
	 */
	@GetMapping
	public ModelAndView listar() {
		// Uma lista de eventos do tipo eventos recebendo todos os eventos do banco
		List<Autor> autores = ar.findAll();
		// modelAndView vai colocar os eventos dentro da pagina lista
		ModelAndView mv = new ModelAndView();
		mv.setViewName("autor/Lista");
		// adiconando os eventos na variavel eventos para chamar no html
		mv.addObject("autor", autores);
		// retornando a pagina com todos os eventos listados
		return mv;
	}

	/*
	 * 
	 * metodo para detalhar o evento e listar os convidados do evento em espesifico
	 * 
	 * @Pathvariable serve para pegar a id do evento/{id}
	 * 
	 */
	@GetMapping("/{id}")
	public ModelAndView detalhar(@PathVariable Long id) {
		// Instancia do model
		ModelAndView md = new ModelAndView();
		// Buscando no banco o evento selecionado
		Optional<Autor> opt = ar.findById(id);
		// se o evento n existir ele retorna para a lista
		if (opt.isEmpty()) {
			md.setViewName("redirect:/autor");
			return md;
		}

		// se der serto ele retorna para detalhes mostrando os dados do evento
		md.setViewName("autor/Detalhes");
		// pegando o evento selecionado
		Autor autor = opt.get();
		// adicionando o evento na pagina modelo para recuperar os dados na pagina
		md.addObject("autor", autor);

		return md;
	}

	/**
	 * 
	 * @param id
	 * @return
	 * 
	 *         metodo para selecionado um evento pela id
	 */
	@GetMapping("/{id}/selecionar")
	public ModelAndView selecionarEvento(@PathVariable Long id) {

		ModelAndView md = new ModelAndView();
		// buscando evento no banco usando a id
		Optional<Autor> opt = ar.findById(id);
		// validando se o evento existe
		if (opt.isEmpty()) {
			// se n existir ele retorna para eventos
			md.setViewName("redirect:/autor");
			return md;
		}

		// se tiver tudo certo ele pega o evento
		Autor autor = opt.get();
		md.setViewName("autor/Atualizar");
		// adiciona os dados do evento para recuperar na pagina
		md.addObject("autor", autor);

		return md;
	}

	@GetMapping("/{id}/remover")
	public String apagarEvento(@PathVariable Long id, RedirectAttributes attributes) {

		// busca o evento no banco pela id
		Optional<Autor> opt = ar.findById(id);

		// se existir ele pega o evento
		if (!opt.isEmpty()) {
			Autor autor = opt.get();

			// apagando evento
			ar.delete(autor);
			// mensagem na pagina
			attributes.addFlashAttribute("mensagem", "autor removido com sucesso!");
		}
		// retornando para a lista
		return "redirect:/autor";
	}

}
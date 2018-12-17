package fr.iat.tpcinema.web;

import fr.iat.tpcinema.dao.PersonneDao;
import fr.iat.tpcinema.model.Personne;
import fr.iat.tpcinema.service.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

//TODO séparer les acteurs des réalisateurs en typant les personnes, conditionner les controlleurs en fonction du type

@Controller
@RequestMapping(value = "/person")
public class PersonneController {

//    @Autowired
//    FilmDao filmDao;

    @Autowired
    PersonneDao personneDao;

//    @Autowired
//    RoleDao roleDao;

    @Autowired
    private Path path;


    // ========== FILMS =======================================================
//    @GetMapping("/liste-films")
//    public String listeFilms(Model model) {
//        model.addAttribute("films", filmDao.films());
//        return "liste-films";
//    }

    // Version avec @RequestParam
//    @GetMapping("/film")
//    public String detail(Model model, @RequestParam("id") String id) {
//        Integer idFilm = Integer.parseInt(id);
//        model.addAttribute("film", filmsDao.getById(idFilm));
//        return "detail";
//    }

//    // Version avec @PathVariable
//    @GetMapping("/film/{id}")
//    public String film(Model model, @PathVariable("id") String id) {
//        int idFilm = Integer.parseInt(id);
//        model.addAttribute("film", filmDao.getById(idFilm));
//        return "film";
//    }

    // ========== PERSONNES =======================================================

    @GetMapping("/list")
    public String personnesListe(Model model) {
        model.addAttribute("personnes", personneDao.getAll());
        return "person/list";
    }

    @GetMapping("/detail/{id}")
    public String personne(Model model, @PathVariable("id") String id) {
        long idPersonne = Long.parseLong(id);
        model.addAttribute("personne", personneDao.getById(idPersonne));
        return "person/detail";
    }

    @GetMapping("/form")
    public String personneForm(Model model) {
        model.addAttribute("personne", new Personne());
        return "person/form";
    }

    @PostMapping("/add")
    public String personneCreate(@ModelAttribute Personne personne){
        personneDao.save(personne);
        return "redirect:/person/list";
    }

    // TODO prioritaire : personneDeleteController
//    @GetMapping("/personne/delete/{id}")
//    public String personneDelete(@PathVariable("id") Long id){
//        personneDao.delete(id);
//        return "redirect:/personnes-liste";
//    }

    // ========== AFFICHES =======================================================
    // TODO : factoriser la gestion des paths pour les images
    // TODO : virer les request / response, et/ou trouver solution plus simple pour ces modules
    @GetMapping("/affiches/{id}")
    public void affiche(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) throws IOException {

//        String affichesPath="C:\\Users\\cyril\\OUTER HEAVEN\\CDA\\varni\\tp-springboot\\sources\\affiches\\";
        String filename = path.getAffiche() + id;

        // ============ UTILITAIRE POUR IMPORTER DES IMAGES A PARTIR D'UN FOLDER EXTERNE A L'APPLICATION ============ //
        String mime = request.getServletContext().getMimeType(filename);
        if (mime == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        response.setContentType(mime);
        File file = new File(filename);
        response.setContentLength((int) file.length());
        FileInputStream in = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.close();
        in.close();
    }

    @GetMapping("/personnes/{id}")
    public void personne(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") String id) throws IOException {

        String filename = path.getPersonne() + id;

        // ============ UTILITAIRE POUR IMPORTER DES IMAGES A PARTIR D'UN FOLDER EXTERNE A L'APPLICATION ============ //
        String mime = request.getServletContext().getMimeType(filename);
        if (mime == null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        response.setContentType(mime);
        File file = new File(filename);
        response.setContentLength((int) file.length());
        FileInputStream in = new FileInputStream(file);
        OutputStream out = response.getOutputStream();
        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.close();
        in.close();
    }
}
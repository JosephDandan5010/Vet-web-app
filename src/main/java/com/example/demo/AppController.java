package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;
import java.util.Optional;

@Controller
public class AppController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AnimalService animalService;

    @Autowired
    private AppointmentService appService;

    @Autowired
    private CustomUserDetailsService userService;

    @Autowired
    private AnimalRepository animalRepo;

    @Autowired
    private AppointmentRepository appRepo;

    @GetMapping("/index")
    public String viewWelcomePage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "index";
        }
        return "redirect:/user/home";
    }

    @GetMapping("/user/home")
    public String viewHomePage() {
        return "home";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            model.addAttribute("user", new User());
            return "signup_form";
        }

        return "redirect:/user/home";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "register_success";
    }

    @GetMapping("user/users")
    public String listUsers(Model model) {
        List<User> listUsers = (List<User>) userRepo.findAllByOrderByIdAsc();

        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication instanceof AnonymousAuthenticationToken) {
            return "login";
        }

        return "redirect:/";
    }

    @Configuration
    public static class MvcConfig implements WebMvcConfigurer {

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {

            registry.addViewController("/login").setViewName("login");

        }

    }

    @GetMapping("user/animals")
    public String listAnimals(Model model) {
        List<Animal> listAnimals = animalService.findAllOrderByIdAsc();

        model.addAttribute("listAnimals", listAnimals);

        return "animals";
    }

    @RequestMapping(value = "user/animals_save", method = RequestMethod.POST)
    public String saveAnimal(Animal a) {
        animalService.save(a);
        return "redirect:animals";
    }

    @GetMapping("user/animal_form")
    public String animalForm(Model model) {
        Animal animal = new Animal();
        model.addAttribute("animal", animal);
        return "animal_form";
    }

    @RequestMapping(value = "user/editAnimalSave", method = RequestMethod.POST)
    public String edit(Animal newAnimal) {
        Animal oldAnimal = animalService.get(newAnimal.getId());// this will load the existing animal
        oldAnimal.setHeight(newAnimal.getHeight());
        oldAnimal.setWeight(newAnimal.getWeight());
        oldAnimal.setGender(newAnimal.getGender());
        oldAnimal.setSpecies(newAnimal.getSpecies());
        oldAnimal.setOwnerName(newAnimal.getOwnerName());
        oldAnimal.setPetName(newAnimal.getPetName());
        // do the rest of the updates
        animalRepo.save(oldAnimal);

        return "redirect:/user/animals";
    }

    @RequestMapping("user/animal/edit/{id}")
    public String editAnimal(@PathVariable(name = "id") long id, Model model) {
        Optional<Animal> animal = animalRepo.findById(id);
        if (animal.isPresent()) {
            model.addAttribute("animal", animal.get());
            return "edit_animal";
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("animal/delete/{id}")
    public String deleteAnimal(@PathVariable(name = "id") long id) {
        animalService.delete(id);
        return "redirect:/user/animals";
    }

    @GetMapping("/user/appointments")
    public String listAppointments(Model model) {
        List<Appointment> listAppointments = appService.findAllOrderByIdAsc();

        model.addAttribute("listAppointments", listAppointments);

        return "appointments";
    }

    @RequestMapping(value = "/user/appointments_save", method = RequestMethod.POST)
    public String saveAppointment(Appointment app) {
        appService.save(app);
        return "redirect:appointments";
    }

    @GetMapping("/user/appointment_form")
    public String appointmentForm(Model model) {
        Appointment appointment = new Appointment();
        model.addAttribute("appointment", appointment);
        return "appointment_form";
    }

    @RequestMapping(value = "user/editAppointmentSave", method = RequestMethod.POST)
    public String edit(Appointment newAppointment) {
        Appointment oldAppointment = appService.get(newAppointment.getId());// this will load the existing animal
        oldAppointment.setName(newAppointment.getName());
        oldAppointment.setIllness(newAppointment.getIllness());
        oldAppointment.setTime(newAppointment.getTime());
        oldAppointment.setStatus(newAppointment.getStatus());
        // do the rest of the updates
        appRepo.save(oldAppointment);

        return "redirect:/user/appointments";
    }

    @RequestMapping("user/appointment/edit/{id}")
    public String editAppointment(@PathVariable(name = "id") long id, Model model) {
//        ModelAndView mav = new ModelAndView("edit_appointment");
        Optional<Appointment> appointment = appRepo.findById(id);
        if (appointment.isPresent()) {
            model.addAttribute("appointment", appointment.get());
            return "edit_appointment";
        } else {
            System.out.println("test");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping("appointment/delete/{id}")
    public String deleteAppointment(@PathVariable(name = "id") long id) {
        appService.delete(id);
        return "redirect:/user/appointments";
    }

    @RequestMapping("user/search/animals")
    public String searchAnimals(Model model, @RequestParam(value = "query", required = false) String query) {
        List<Animal> results = animalRepo.findByOwnerNameOrPetName(query);
        model.addAttribute("listAnimals", results);
        model.addAttribute("query", query);
        if (results.isEmpty()) {
            return "no_results_found";
        }
        return "search_animals_results";
    }

    @RequestMapping("user/search/appointments")
    public String searchAppointments(Model model, @RequestParam(value = "query", required = false) String query) {
        List<Appointment> results = appRepo.findByName(query);
        System.out.println(results);
        model.addAttribute("listAppointments", results);
        model.addAttribute("query", query);
        if (results.isEmpty()) {
            return "no_results_found";
        }
        return "search_appointments_results";
    }
}


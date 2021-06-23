package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

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

    @GetMapping("")
    public String viewHomePage() {
        return "index";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "signup_form";
    }

    @PostMapping("/process_register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "register_success";
    }

    @GetMapping("/users")
    public String listUsers(Model model) {
        List<User> listUsers = (List<User>) userRepo.findAll();

        model.addAttribute("listUsers", listUsers);

        return "users";
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @Configuration
    public static class MvcConfig implements WebMvcConfigurer {

        @Override
        public void addViewControllers(ViewControllerRegistry registry) {

            registry.addViewController("/login").setViewName("login");

        }

    }

    @GetMapping("/animals")
    public String listAnimals(Model model) {
        List<Animal> listAnimals = animalService.findAllOrderByIdAsc();

        model.addAttribute("listAnimals", listAnimals);

        return "animals";
    }

    @RequestMapping(value = "/animals_save", method = RequestMethod.POST)
    public String saveAnimal(Animal a) {
        animalService.save(a);
        return "redirect:/animals";
    }

    @GetMapping("animal_form")
    public String animalForm(Model model){
        Animal animal = new Animal();
        model.addAttribute("animal", animal);
        return "animal_form";
    }

    @RequestMapping(value = "/editAnimalSave", method = RequestMethod.POST)
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

        return "redirect:/animals";
    }

    @RequestMapping("/animal/edit/{id}")
    public ModelAndView editAnimal(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("edit_animal");
        Animal animal = animalService.get(id);
        mav.addObject("animal", animal);

        System.out.println(id);
        return mav;
    }

    @RequestMapping("/animal/delete/{id}")
    public String deleteAnimal(@PathVariable(name = "id") int id) {
        animalService.delete(id);
        return "redirect:/animals";
    }

    @GetMapping("/appointments")
    public String listAppointments(Model model) {
        List<Appointment> listAppointments = appService.findAllOrderByIdAsc();

        model.addAttribute("listAppointments", listAppointments);

        return "appointments";
    }

    @RequestMapping(value = "/appointments_save", method = RequestMethod.POST)
    public String saveAppointment(Appointment app) {
        appService.save(app);
        return "redirect:/appointments";
    }

    @GetMapping("appointment_form")
    public String appointmentForm(Model model){
        Appointment appointment = new Appointment();
        model.addAttribute("appointment", appointment);
        return "appointment_form";
    }

    @RequestMapping(value = "/editAppointmentSave", method = RequestMethod.POST)
    public String edit(Appointment newAppointment) {
        Appointment oldAppointment = appService.get(newAppointment.getId());// this will load the existing animal
        oldAppointment.setName(newAppointment.getName());
        oldAppointment.setIllness(newAppointment.getIllness());
        oldAppointment.setTime(newAppointment.getTime());
        oldAppointment.setStatus(newAppointment.getStatus());
        // do the rest of the updates
        appRepo.save(oldAppointment);

        return "redirect:/appointments";
    }

    @RequestMapping("/appointment/edit/{id}")
    public ModelAndView editAppointment(@PathVariable(name = "id") int id) {
        ModelAndView mav = new ModelAndView("edit_appointment");
        Appointment appointment = appService.get(id);
        mav.addObject("appointment", appointment);

        System.out.println(id);
        return mav;
    }

    @RequestMapping("/appointment/delete/{id}")
    public String deleteAppointment(@PathVariable(name = "id") int id) {
        appService.delete(id);
        return "redirect:/appointments";
    }
    @RequestMapping("/search/{query}")
    public String search(@PathVariable(name = "query") String query) {
        System.out.println(query);
        return "search_results";
    }
}


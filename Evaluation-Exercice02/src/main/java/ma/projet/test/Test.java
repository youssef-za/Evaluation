package ma.projet.test;

import ma.projet.service.EmployeService;
import ma.projet.service.EmployeTacheService;
import ma.projet.service.ProjetService;
import ma.projet.service.TacheService;
import ma.projet.classes.Employe;
import ma.projet.classes.Projet;
import ma.projet.classes.Tache;
import ma.projet.classes.EmployeTache;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(HibernateUtil.class);

        EmployeService employeService = context.getBean(EmployeService.class);
        EmployeTacheService employeTacheService = context.getBean(EmployeTacheService.class);
        ProjetService projetService = context.getBean(ProjetService.class);
        TacheService tacheService = context.getBean(TacheService.class);


        Employe emp1 = new Employe();
        emp1.setNom("Zaia");
        emp1.setPrenom("Youssef");
        emp1.setTelephone("0655865942");
        employeService.create(emp1);

        Employe emp2 = new Employe();
        emp2.setNom("Sabro");
        emp2.setPrenom("Youssef");
        emp2.setTelephone("0780256495");
        employeService.create(emp2);


        Projet proj1 = new Projet();
        proj1.setNom("Migration Cloud CRM");
        proj1.setChef(emp1); // Hammam Elkentaoui est chef de projet
        proj1.setDateDebut(LocalDate.of(2025, 3, 1));
        proj1.setDateFin(LocalDate.of(2025, 9, 30));
        projetService.create(proj1);

        Projet proj2 = new Projet();
        proj2.setNom("Refonte E-commerce");
        proj2.setChef(emp2); // Abdellah Elkentaoui est chef de projet
        proj2.setDateDebut(LocalDate.of(2025, 4, 15));
        proj2.setDateFin(LocalDate.of(2025, 11, 15));
        projetService.create(proj2);


        Tache t1 = new Tache();
        t1.setNom("Analyse des besoins");
        t1.setProjet(proj1);
        t1.setPrix(1200f);
        t1.setDateDebut(LocalDate.of(2025, 3, 10));
        t1.setDateFin(null);
        tacheService.create(t1);

        Tache t2 = new Tache();
        t2.setNom("Maquettage UI/UX");
        t2.setProjet(proj2);
        t2.setPrix(900f);
        t2.setDateDebut(LocalDate.of(2025, 4, 20));
        t2.setDateFin(null);
        tacheService.create(t2);

        Tache t3 = new Tache();
        t3.setNom("Configuration VM Azure");
        t3.setProjet(proj1);
        t3.setPrix(2000f);
        t3.setDateDebut(LocalDate.of(2025, 3, 15));
        t3.setDateFin(LocalDate.of(2025, 4, 1));
        tacheService.create(t3);

        Tache t4 = new Tache();
        t4.setNom("Migration Données Client");
        t4.setProjet(proj1);
        t4.setPrix(3500f);
        t4.setDateDebut(LocalDate.of(2025, 4, 5));
        t4.setDateFin(LocalDate.of(2025, 4, 20));
        tacheService.create(t4);


        EmployeTache et1 = new EmployeTache();
        et1.setEmploye(emp1);
        et1.setTache(t1);
        employeTacheService.create(et1);

        EmployeTache et2 = new EmployeTache();
        et2.setEmploye(emp1);
        et2.setTache(t3);
        employeTacheService.create(et2);

        EmployeTache et3 = new EmployeTache();
        et3.setEmploye(emp2);
        et3.setTache(t2);
        employeTacheService.create(et3);

        EmployeTache et4 = new EmployeTache();
        et4.setEmploye(emp1);
        et4.setTache(t4);
        employeTacheService.create(et4);


        System.out.println("\n--- Tâches assignées à Hammam Elkentaoui (ID: " + emp1.getId() + ") ---");
        employeService.afficherTachesParEmploye(emp1.getId());

        System.out.println("\n--- Projets gérés par Hammam Elkentaoui (ID: " + emp1.getId() + ") ---");
        employeService.afficherProjetsGeresParEmploye(emp1.getId());

        System.out.println("\n--- Tâches planifiées (non terminées) pour le projet CRM (ID: " + proj1.getId() + ") ---");
        projetService.afficherTachesPlanifieesParProjet(proj1.getId());

        System.out.println("\n--- Tâches réalisées (terminées) pour le projet CRM (ID: " + proj1.getId() + ") ---");
        projetService.afficherTachesRealiseesParProjet(proj1.getId());

        System.out.println("\n--- Tâches dont le prix est > 1000 DH ---");
        tacheService.afficherTachesPrixSuperieurA1000();

        System.out.println("\n--- Tâches réalisées entre le 1er Avril et le 30 Avril 2025 ---");
        LocalDate debut = LocalDate.of(2025, 4, 1);
        LocalDate fin = LocalDate.of(2025, 4, 30);
        tacheService.afficherTachesEntreDates(debut, fin);

        context.close();
    }
}
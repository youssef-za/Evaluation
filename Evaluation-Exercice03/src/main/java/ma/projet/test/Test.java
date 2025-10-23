package ma.projet.test;

import ma.projet.beans.Femme;
import ma.projet.beans.Homme;
import ma.projet.beans.Mariage;
import ma.projet.service.FemmeService;
import ma.projet.service.HommeService;
import ma.projet.service.MariageService;
import ma.projet.util.HibernateUtil;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(HibernateUtil.class);

        FemmeService femmeService = context.getBean(FemmeService.class);
        HommeService hommeService = context.getBean(HommeService.class);
        MariageService mariageService = context.getBean(MariageService.class);

        // --- Création des Femmes ---
        Femme f1 = new Femme(); f1.setCin("A111"); f1.setNom("Bennani"); f1.setPrenom("Fatima"); f1.setDateNaissance(LocalDate.of(1965, 3, 12));
        Femme f2 = new Femme(); f2.setCin("B222"); f2.setNom("Alaoui"); f2.setPrenom("Khadija"); f2.setDateNaissance(LocalDate.of(1970, 7, 5));
        Femme f3 = new Femme(); f3.setCin("C333"); f3.setNom("Saidi"); f3.setPrenom("Aicha"); f3.setDateNaissance(LocalDate.of(1980, 11, 4));
        Femme f4 = new Femme(); f4.setCin("D444"); f4.setNom("Filali"); f4.setPrenom("Nadia"); f4.setDateNaissance(LocalDate.of(1968, 9, 3));
        Femme f5 = new Femme(); f5.setCin("E555"); f5.setNom("Cherkaoui"); f5.setPrenom("Meryem"); f5.setDateNaissance(LocalDate.of(1975, 6, 10));

        Arrays.asList(f1, f2, f3, f4, f5).forEach(femmeService::create);

        // --- Création des Hommes ---
        Homme h1 = new Homme(); h1.setNom("Amrani"); h1.setPrenom("Youssef");
        Homme h2 = new Homme(); h2.setNom("Tazi"); h2.setPrenom("Rachid");
        Homme h3 = new Homme(); h3.setNom("Lahlou"); h3.setPrenom("Mehdi");

        Arrays.asList(h1, h2, h3).forEach(hommeService::create);

        // --- Création des Mariages ---
        // Mariages de Youssef Amrani (h1)
        Mariage m1 = new Mariage(); m1.setHomme(h1); m1.setFemme(f1); m1.setDateDebut(LocalDate.of(1990,9,3)); m1.setNbrEnfant(4);
        Mariage m2 = new Mariage(); m2.setHomme(h1); m2.setFemme(f2); m1.setDateDebut(LocalDate.of(1995,9,3)); m1.setNbrEnfant(2);
        Mariage m3 = new Mariage(); m3.setHomme(h1); m3.setFemme(f3); m1.setDateDebut(LocalDate.of(2000,11,4)); m1.setNbrEnfant(3);
        // Mariage terminé (divorce)
        Mariage m4 = new Mariage(); m4.setHomme(h1); m4.setFemme(f4); m1.setDateDebut(LocalDate.of(1989,9,3)); m1.setDateFin(LocalDate.of(1990,9,3)); m1.setNbrEnfant(0);

        // Mariages de Rachid Tazi (h2)
        Mariage m5 = new Mariage(); m5.setHomme(h2); m5.setFemme(f1); m1.setDateDebut(LocalDate.of(1992,5,10)); m1.setNbrEnfant(1);
        Mariage m6 = new Mariage(); m6.setHomme(h2); m6.setFemme(f2); m1.setDateDebut(LocalDate.of(1996,7,12)); m1.setNbrEnfant(2);
        Mariage m7 = new Mariage(); m7.setHomme(h2); m7.setFemme(f3); m1.setDateDebut(LocalDate.of(2000,3,5)); m1.setNbrEnfant(1);
        Mariage m8 = new Mariage(); m8.setHomme(h2); m8.setFemme(f4); m1.setDateDebut(LocalDate.of(2003,1,20)); m1.setNbrEnfant(2);

        // Mariages de Mehdi Lahlou (h3)
        Mariage m9 = new Mariage(); m9.setHomme(h3); m9.setFemme(f1); m1.setDateDebut(LocalDate.of(2010,6,1)); m1.setNbrEnfant(1);
        Mariage m10 = new Mariage(); m10.setHomme(h3); m10.setFemme(f2); m1.setDateDebut(LocalDate.of(2012,8,15)); m1.setNbrEnfant(0);

        Arrays.asList(m1,m2,m3,m4,m5,m6,m7,m8,m9,m10).forEach(mariageService::create);

        // --- Début des Tests de lecture ---

        System.out.println("\n--- Liste des Femmes ---");
        femmeService.findAll().forEach(f -> System.out.printf("%-5s %-25s %-25s %-12s%n",
                f.getCin(), f.getNom(), f.getPrenom(), f.getDateNaissance()));


        Femme plusAgee = femmeService.findAll().stream()
                .min((fA, fB) -> fA.getDateNaissance().compareTo(fB.getDateNaissance()))
                .orElse(null);
        System.out.println("\nFemme la plus âgée : " + (plusAgee != null ? plusAgee.getCin() + " " + plusAgee.getNom() + " " + plusAgee.getPrenom() : "N/A"));


        System.out.println("\n--- Épouses de l'homme " + h1.getNom() + " " + h1.getPrenom() + " ---");
        hommeService.afficherEpousesEntreDates(h1.getId(), LocalDate.of(1980,1,1), LocalDate.of(2025,12,31));


        System.out.println("\n--- Nombre d'enfants de " + f1.getNom() + " " + f1.getPrenom() + " (Mariages entre 1980 et 2020) ---");
        int nbEnfants = femmeService.nombreEnfantsEntreDates(f1.getId(), LocalDate.of(1980,1,1), LocalDate.of(2020,12,31));
        System.out.println("Nombre d'enfants : " + nbEnfants);


        System.out.println("\n--- Femmes mariées deux fois ou plus ---");
        femmeService.femmesMarieesAuMoinsDeuxFois().forEach(f -> System.out.printf("%-5s %-25s %-25s%n", f.getCin(), f.getNom(), f.getPrenom()));


        System.out.println("\n--- Hommes mariés à 4 femmes (ou plus) entre 1980 et 2025 ---");
        hommeService.afficherNombreHommesAvecQuatreFemmes(LocalDate.of(1980,1,1), LocalDate.of(2025,12,31));


        System.out.println("\n--- Mariages de l'homme " + h1.getNom() + " " + h1.getPrenom() + " avec détails ---");
        mariageService.afficherMariagesAvecDetails(h1.getId());

        context.close();
    }
}
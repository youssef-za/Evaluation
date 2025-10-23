package ma.projet.test;

import ma.projet.classes.Categorie;
import ma.projet.classes.Produit;
import ma.projet.classes.Commande;
import ma.projet.classes.LigneCommandeProduit;
import ma.projet.service.ProduitService;
import ma.projet.service.CommandeService;
import ma.projet.service.LigneCommandeService;
import ma.projet.util.HibernateConfig;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.time.LocalDate;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext(HibernateConfig.class);

        ProduitService produitService = context.getBean(ProduitService.class);
        CommandeService commandeService = context.getBean(CommandeService.class);
        LigneCommandeService ligneService = context.getBean(LigneCommandeService.class);

        Categorie catLaptop = new Categorie();
        catLaptop.setCode("DAP");
        catLaptop.setLibelle("PC Postes");

        Categorie catPeripherique = new Categorie();
        catPeripherique.setCode("PER");
        catPeripherique.setLibelle("Périphériques");

        // --- Création des Produits ---
        // Produit > 100
        Produit p1 = new Produit();
        p1.setReference("DELL-XPS13");
        p1.setPrix(15000f);
        p1.setCategorie(catLaptop);

        // Produit == 100 (pour tester la condition > 100)
        Produit p2 = new Produit();
        p2.setReference("LOGI-M185");
        p2.setPrix(100f);
        p2.setCategorie(catPeripherique);

        // Produit > 100
        Produit p3 = new Produit();
        p3.setReference("HP-SPECTRE");
        p3.setPrix(13500f);
        p3.setCategorie(catLaptop);

        // Produit > 100
        Produit p4 = new Produit();
        p4.setReference("MS-KEYB");
        p4.setPrix(450f);
        p4.setCategorie(catPeripherique);

        // Persistance des produits (et catégories par cascade si configuré)
        produitService.create(p1);
        produitService.create(p2);
        produitService.create(p3);
        produitService.create(p4);

        // --- Création des Commandes ---
        Commande c1 = new Commande();
        c1.setDate(LocalDate.of(2025, 9, 15)); // Date plus récente
        commandeService.create(c1);

        Commande c2 = new Commande();
        c2.setDate(LocalDate.of(2025, 10, 20)); // Date plus récente
        commandeService.create(c2);

        // --- Création des Lignes de Commande ---
        // Commande c1 contient 1x DELL-XPS13 et 2x MS-KEYB
        LigneCommandeProduit l1 = new LigneCommandeProduit();
        l1.setProduit(p1);
        l1.setCommande(c1);
        l1.setQuantite(1); // Quantité réaliste

        LigneCommandeProduit l2 = new LigneCommandeProduit();
        l2.setProduit(p4);
        l2.setCommande(c1);
        l2.setQuantite(2);

        // Commande c2 contient 1x HP-SPECTRE et 3x LOGI-M185
        LigneCommandeProduit l3 = new LigneCommandeProduit();
        l3.setProduit(p3);
        l3.setCommande(c2);
        l3.setQuantite(1);

        LigneCommandeProduit l4 = new LigneCommandeProduit();
        l4.setProduit(p2);
        l4.setCommande(c2);
        l4.setQuantite(3);

        ligneService.create(l1);
        ligneService.create(l2);
        ligneService.create(l3);
        ligneService.create(l4);


        // --- Début des Tests de lecture ---

        System.out.println("\n--- Produits de la catégorie 'PC Portables' ---");
        // Utilisation de l'objet 'catLaptop' directement
        List<Produit> produitsCatLaptop = produitService.findByCategorie(catLaptop);
        for (Produit p : produitsCatLaptop) {
            System.out.printf("Réf : %-12s Prix : %-8.2f Catégorie : %s%n",
                    p.getReference(), p.getPrix(), p.getCategorie().getLibelle());
        }

        System.out.println("\n--- Produits de la commande C1 (ID: " + c1.getId() + ") ---");
        // Test de l'affichage formaté pour la commande c1
        produitService.findByCommande(c1.getId());

        System.out.println("\n--- Produits commandés en 2025 ---");
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2025, 12, 31);
        // Test de la recherche par date
        produitService.findByDateCommande(start, end);

        System.out.println("\n--- Produits dont le prix est > 100 DH ---");
        // Devrait afficher DELL-XPS13, HP-SPECTRE, et MS-KEYB
        // Ne devrait PAS afficher LOGI-M185 (car prix == 100)
        produitService.findProduitsPrixSuperieur(100f);

        context.close();
    }
}
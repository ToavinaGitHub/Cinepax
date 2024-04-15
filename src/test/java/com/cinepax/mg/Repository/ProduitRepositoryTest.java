package com.cinepax.mg.Repository;



import com.cinepax.mg.Exception.ValeurInvalideException;
import com.cinepax.mg.Model.Produit;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

@DataJpaTest
public class ProduitRepositoryTest {

    @Autowired
    ProduitRepository produitRepository;
    @Test
    public void produitGetAll() throws ValeurInvalideException {
        Produit p = new Produit("PD001","produit 1",350,1);
        Produit p2 = new Produit("PD002","produit 2",1350,1);
        produitRepository.save(p);
        produitRepository.save(p2);
        List<Produit> allProduit = produitRepository.findAll();
        Assertions.assertThat(allProduit).isNotNull();
        Assertions.assertThat(allProduit.size()).isEqualTo(2);
    }
}

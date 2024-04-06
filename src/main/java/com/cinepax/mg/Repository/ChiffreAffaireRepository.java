    package com.cinepax.mg.Repository;

    import com.cinepax.mg.Model.Tarif;
    import com.cinepax.mg.view.V_film_plus_vues_jour;
    import com.cinepax.mg.view.V_montant_film_jour;
    import com.cinepax.mg.view.V_montant_produit_jour;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.stereotype.Repository;

    import java.util.List;


    @Repository
    public interface ChiffreAffaireRepository extends JpaRepository<Tarif,String>{

        @Query(value = "SELECT d,id_produit,libelle,vola from v_montant_produit_jour",nativeQuery = true)
        List<V_montant_produit_jour> getMontantProduitParJour();

        @Query(value = "SELECT d,id_film,titre,vola from v_montant_film_jour",nativeQuery = true)
        List<V_montant_film_jour> getMontantFilmParJour();


        @Query(value = "SELECT id_film,titre,vue from v_film_plus_vues_jour",nativeQuery = true)
        List<V_film_plus_vues_jour> getFilmPlusVue();

    }

package com.cinepax.mg.Model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "details_vente_billet")
public class DetailsVenteBillet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    String idDetailsVenteBillet;

    @ManyToOne
            @JoinColumn(name = "idVenteBillet" , nullable = false)
    VenteBillet venteBillet;
    @ManyToOne
        @JoinColumn(name = "idPlaceSalle" , nullable = false)
    PlaceSalle placeSalle;

}

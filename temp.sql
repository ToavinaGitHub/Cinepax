create table genre_film
(
    id_genre varchar(255) default generate_genre_film_id() not null
        primary key,
    etat     integer                                       not null,
    libelle  varchar(255)
);

alter table genre_film
    owner to postgres;

create table film
(
    id_film       varchar(255) default generate_film_id() not null
        primary key,
    description   varchar(255),
    duree         double precision                        not null,
    etat          integer                                 not null,
    sary          varchar(255),
    titre         varchar(255),
    id_genre_film varchar(255)                            not null
        constraint fkq4atcim1eu159a7n0db2kc6ms
            references genre_film
);

alter table film
    owner to postgres;

create table produit
(
    id_produit varchar(255) default generate_produit_id() not null
        primary key,
    etat       integer                                    not null,
    libelle    varchar(255),
    prix       double precision                           not null
);

alter table produit
    owner to postgres;

create table prix_produit
(
    id_prix_produit serial
        primary key,
    daty            timestamp(6),
    etat            integer          not null,
    prix            double precision not null,
    id_produit      varchar(255)     not null
        constraint fkeb7f8qm3bnsgc88456mq7hmpw
            references produit
);

alter table prix_produit
    owner to postgres;

create trigger produit_prix_trigger
    after insert or update
        of prix
    on produit
    for each row
execute procedure insert_into_prix_produit();

create table salle
(
    id_salle varchar(255) default generate_salle_id() not null
        primary key,
    capacite double precision                         not null,
    etat     integer                                  not null,
    nom      varchar(255)
);

alter table salle
    owner to postgres;

create table event
(
    id_event varchar(255) default generate_event_id() not null
        primary key,
    date     timestamp(6),
    etat     integer                                  not null,
    heure    timestamp(6),
    prix     double precision                         not null,
    id_film  varchar(255)                             not null
        constraint fkytk5xdonp3yh4jkgo2p6y4db
            references film,
    id_salle varchar(255)                             not null
        constraint fkacdc1xm28p2ktny0p4ayds6yq
            references salle
);

alter table event
    owner to postgres;

create table place_salle
(
    id_place_salle integer generated always as identity
        primary key,
    etat           integer      not null,
    numero         varchar(255),
    range          varchar(255),
    id_salle       varchar(255) not null
        constraint fkocl4qgp3v88j5pgg6a5lxff4l
            references salle
);

alter table place_salle
    owner to postgres;

create table transaction_produit
(
    id_transaction_produit varchar(255) default generate_trans_id() not null
        primary key,
    daty                   timestamp(6),
    etat                   integer                                  not null,
    montant                double precision                         not null,
    pu                     double precision                         not null,
    quantite               double precision                         not null,
    id_produit             varchar(255)                             not null
        constraint fkobtdkkgpap6kx2oyxmrj32v3e
            references produit
);

alter table transaction_produit
    owner to postgres;

create trigger update_montant_produit_trigger
    after insert or update
        of pu, quantite
    on transaction_produit
    for each row
execute procedure f_montant_transaction_produit();

create table vente_billet
(
    id_vente_billet varchar(255) default generate_vente_billet_id() not null
        primary key,
    date_vente      timestamp(6),
    etat            integer                                         not null,
    montant         double precision,
    nombre          double precision                                not null,
    places          varchar(255),
    prix            double precision                                not null,
    id_event        varchar(255)                                    not null
        constraint fk4kq11ur8sy2qpw15iypnbe11k
            references event
);

alter table vente_billet
    owner to postgres;

create table details_vente_billet
(
    id_details_vente_billet varchar(255) default generate_det_vente_billet_id() not null
        primary key,
    id_place_salle          integer                                             not null
        constraint fk85nm3kryjh215xb1ncgqr7hfs
            references place_salle,
    id_vente_billet         varchar(255)                                        not null
        constraint fk9i6aqy5f65tjgq43x26bofiav
            references vente_billet
);

alter table details_vente_billet
    owner to postgres;

create trigger update_montant_billet_trigger
    after insert or update
        of prix, nombre
    on vente_billet
    for each row
execute procedure f_montant_vente_billet();

create table utilisateur
(
    id_utilisateur varchar(255) default generate_user_id() not null
        primary key,
    genre          integer                                 not null,
    nom            varchar(255),
    prenom         varchar(255),
    profil         smallint
        constraint utilisateur_profil_check
            check ((profil >= 0) AND (profil <= 1))
);

alter table utilisateur
    owner to postgres;


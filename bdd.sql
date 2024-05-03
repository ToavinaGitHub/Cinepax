CREATE SEQUENCE trans_id_seq START 1;

-- 2. Créez une fonction pour générer les valeurs de la séquence avec un préfixe
CREATE OR REPLACE FUNCTION generate_trans_id() RETURNS VARCHAR AS $$
DECLARE
    next_id INTEGER;
BEGIN
    -- Obtenez le prochain numéro de séquence
    SELECT nextval('trans_id_seq') INTO next_id;
-- Formatez l'ID avec le préfixe "ETU" et le numéro de séquence, en remplissant les chiffres manquants avec des zéros
    RETURN 'TRANS' || LPAD(next_id::TEXT, 7, '0');
END;
$$ LANGUAGE plpgsql;

ALTER TABLE transaction_produit
    ALTER COLUMN id_transaction_produit SET DEFAULT generate_trans_id();


----------------Apres modif prix
CREATE OR REPLACE FUNCTION insert_into_prix_produit()
    RETURNS TRIGGER AS
$$
BEGIN
    INSERT INTO prix_produit (daty,prix,id_produit,etat)
    VALUES (CURRENT_TIMESTAMP,new.prix, new.id_produit,1);
    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;


CREATE TRIGGER produit_prix_trigger
    AFTER INSERT OR UPDATE OF prix ON produit
    FOR EACH ROW
EXECUTE FUNCTION insert_into_prix_produit();


---------------------Montant vente
CREATE OR REPLACE FUNCTION f_montant_vente_billet()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE vente_billet SET montant = prix*nombre where vente_billet.id_vente_billet=new.id_vente_billet;
    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;

DROP TRIGGER update_montant_billet_trigger on vente_billet;

CREATE TRIGGER update_montant_billet_trigger
    AFTER INSERT OR UPDATE OF prix,nombre ON vente_billet
    FOR EACH ROW
EXECUTE FUNCTION f_montant_vente_billet();

-----------------------------Montant vente produit

CREATE OR REPLACE FUNCTION f_montant_transaction_produit()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE transaction_produit SET montant = pu*quantite where transaction_produit.id_transaction_produit=new.id_transaction_produit;
    RETURN NEW;
END;
$$
    LANGUAGE plpgsql;

DROP TRIGGER update_montant_produit_trigger on transaction_produit;

CREATE TRIGGER update_montant_produit_trigger
    AFTER INSERT OR UPDATE OF pu,quantite ON transaction_produit
    FOR EACH ROW
EXECUTE FUNCTION f_montant_transaction_produit();



-----------------Place par event
CREATE OR REPLACE view v_place_event as
SELECT p.id_place_salle, p.range, p.numero,e.id_event,d.id_vente_billet,
       case
           WHEN d.id_vente_billet is null then 1
           WHEN d.id_vente_billet is not null then 0
        end
        dispo

FROM place_salle p
         JOIN salle s ON s.id_salle = p.id_salle
         JOIN event e ON e.id_salle = s.id_salle
         LEFT JOIN details_vente_billet d ON d.id_place_salle = p.id_place_salle
         LEFT JOIN vente_billet v ON v.id_vente_billet = d.id_vente_billet
         LEFT JOIN event e2 ON e2.id_event = v.id_event;

DROP VIEW v_place_event;
CREATE OR REPLACE view v_place_event as


SELECT p.id_place_salle,p.range,p.numero,v.id_vente_billet,v.id_event,s.id_salle,
       CASE
           WHEN d.id_vente_billet is null THEN 1
           WHEN d.id_vente_billet is not null  THEN 0
        END dispo
    FROM place_salle  p
    left join details_vente_billet d on d.id_place_salle =p.id_place_salle
    left join vente_billet v on d.id_vente_billet=v.id_vente_billet
    left join event e on v.id_event = e.id_event
    left join salle s on e.id_salle=s.id_salle
    LEFT JOIN event e2 ON e2.id_event = v.id_event;

select * from v_place_event;

SELECT id_place_salle,range,numero,dispo from v_place_event;

--Nombre range par salle


SELECT count(p) from (SELECT DISTINCT range from place_salle WHERE id_salle=?1) as p;

SELECT id_place_salle,range,numero,dispo,id_vente_billet from v_place_event;


 SELECT p.id_place_salle,p.range,p.numero
from place_salle p
join salle
on salle.id_salle=p.id_salle
join event e
on e.id_salle = salle.id_salle
EXCEPT
SELECT p2.id_place_salle,p2.range,p2.numero
FROM details_vente_billet d
join place_salle p2
on p2.id_place_salle=d.id_place_salle
join vente_billet v on d.id_vente_billet=v.id_vente_billet
join event e on v.id_event=e.id_event
 where e.id_event='EVN0000001';


-----------Vente de billet par film

CREATE OR REPLACE VIEW v_stats_film as
SELECT f.id_film,f.titre,sum(v.nombre) as quantite ,sum(v.montant) as montant
FROM vente_billet v
join event ev
on v.id_event=ev.id_event
join film f
on ev.id_film=f.id_film
GROUP BY f.id_film;

SELECT v.id_film,v.titre,v.montant,v.quantite from v_stats_film v;


select *
from event where heure<now();

UPDATE event set etat=0 where heure<now();

drop function f_set_etat_event();

-- Créer la fonction
CREATE OR REPLACE FUNCTION f_set_etat_event()
    RETURNS TRIGGER AS
$$
BEGIN
    UPDATE event SET etat = 0  WHERE heure < now();
END;
$$
    LANGUAGE plpgsql;

DROP TRIGGER set_etat_event_trigger ON event;

CREATE TRIGGER set_etat_event_trigger
    AFTER INSERT ON event
    FOR EACH ROW
EXECUTE FUNCTION f_set_etat_event();

select *from v_place_event;

select now();




CREATE OR REPLACE view v_place_event as;
SELECT p.id_place_salle,p.range,p.numero,e.id_event,v.id_vente_billet,s.id_salle,
       CASE
           WHEN d.id_vente_billet is null THEN 1
           WHEN d.id_vente_billet is not null  THEN 0
        END dispo
FROM place_salle p
         JOIN salle s ON s.id_salle = p.id_salle
          join event e ON e.id_salle = s.id_salle
         LEFT JOIN details_vente_billet d ON d.id_place_salle = p.id_place_salle
         LEFT JOIN vente_billet v ON v.id_vente_billet = d.id_vente_billet
         LEFT JOIN event e2 ON e2.id_event = v.id_event;


SELECT p.id_place_salle,p.range,p.numero,d.id_vente_billet,e.id_event,s.id_salle
FROM place_salle p
    left join details_vente_billet d on p.id_place_salle = d.id_place_salle
    left join vente_billet v on d.id_vente_billet = v.id_vente_billet
    left join event e on v.id_event=e.id_event
    left join salle s on s.id_salle=e.id_salle
    left join event e2 on e2.id_event=v.id_event;

select *from v_place_event;

drop view v_place_event;

create or replace view v_place_event as
SELECT
    p.id_place_salle,
    p.range,
    p.numero,
    e.id_event,
    v.id_vente_billet,
    s.id_salle,
    CASE
        WHEN d.id_vente_billet IS NULL THEN 1
        WHEN d.id_vente_billet IS NOT NULL AND v.id_event = e.id_event THEN 0
        END AS dispo
FROM
    place_salle p
        JOIN
    salle s ON s.id_salle = p.id_salle
        JOIN
    event e ON e.id_salle = s.id_salle
        LEFT JOIN
    details_vente_billet d ON d.id_place_salle = p.id_place_salle
        LEFT JOIN
    vente_billet v ON v.id_vente_billet = d.id_vente_billet
        LEFT JOIN
    event e2 ON e2.id_event = v.id_event;


CREATE OR REPLACE VIEW v_place_event AS;

SELECT
    p.id_place_salle,
    p.range,
    p.numero,
    e.id_event,
    s.id_salle,
    CASE
        WHEN EXISTS (
            SELECT 1
            FROM details_vente_billet d
                     JOIN vente_billet v ON d.id_vente_billet = v.id_vente_billet
            WHERE d.id_place_salle = p.id_place_salle
              AND v.id_event = e.id_event
        ) THEN 0
        ELSE 1
        END AS dispo,
    CASE
        WHEN EXISTS (
            SELECT 1
            FROM details_vente_billet d
                     JOIN vente_billet v ON d.id_vente_billet = v.id_vente_billet
            WHERE d.id_place_salle = p.id_place_salle
              AND v.id_event = e.id_event
        ) THEN v.id_vente_billet
        ELSE NULL
        END AS id_vente_billet
FROM
    place_salle p
        JOIN
    salle s ON s.id_salle = p.id_salle
        JOIN
    event e ON e.id_salle = s.id_salle;




 SELECT r1.id_place_salle,r2.id_vente_billet,r1.range,r1.numero,r1.id_event,r1.id_salle,r2.id_event,
       CASE
            when r2.id_event is null then 1
            when r2.id_event is not null then 0
        END as dispo
from
(select * from v_temp_1 where id_event='EVN0000001' ) r1
left join
(select * from v_temp_2 where id_event='EVN0000001') as r2
on r1.id_place_salle=r2.id_place_salle;






CREATE VIEW v_temp_1 as SELECT a.id_place_salle,a.range,a.numero,e.id_event,salle.id_salle
    FROM place_salle a
    join salle on salle.id_salle=a.id_salle
    join event e on salle.id_salle = e.id_salle;

CREATE view v_temp_2 as select e.id_event,d.id_vente_billet,d.id_place_salle
                        from details_vente_billet d
                                 join vente_billet v on d.id_vente_billet = v.id_vente_billet
                                 join event e on v.id_event=e.id_event

------------Jour 2

CREATE SEQUENCE tarif_id_seq START 1;

CREATE OR REPLACE FUNCTION generate_tarif_id() RETURNS VARCHAR AS $$
DECLARE
    next_id INTEGER;
BEGIN
    -- Obtenez le prochain numéro de séquence
    SELECT nextval('tarif_id_seq') INTO next_id;
    RETURN 'TARIF' || LPAD(next_id::TEXT, 3, '0');
END;
$$ LANGUAGE plpgsql;


ALTER TABLE tarif
    ALTER COLUMN id_tarif SET DEFAULT generate_tarif_id();


SELECT * from tarif where heure_debut <= extract(hour from now()) and heure_fin > extract(hour from now()) and age='Enfant';


-----Chiffre d'affaire par jour par Produit

CREATE OR REPLACE view v_montant_produit_jour as
SELECT DATE(tr.daty) d ,pr.id_produit,pr.libelle,sum(tr.montant) vola
from transaction_produit tr
join produit pr on tr.id_produit = pr.id_produit
where tr.type=10
group by d , pr.id_produit;

SELECT d,id_produit,libelle,vola from v_montant_produit_jour;

drop view v_montant_film_jour;
-----Chiffre d'affaire par jour par Film
CREATE OR REPLACE view v_montant_film_jour as
select DATE(event.date) d ,f.id_film,f.titre , SUM(v.montant) vola
from vente_billet v
join event on v.id_event = event.id_event
join film f on event.id_film = f.id_film
where v.etat=1
group by d , f.id_film;

SELECT d,id_film,titre,vola from v_montant_film_jour;

----Film les plus vues
CREATE OR REPLACE view v_film_plus_vues_jour as
SELECT f.id_film,f.titre, sum(v.nombre) vue
from vente_billet v
join event on v.id_event = event.id_event
join film f on event.id_film = f.id_film
where v.etat=1
group by f.id_film
order by vue desc ;

SELECT id_film,titre,vue from v_film_plus_vues_jour;


drop table dataCsv;

CREATE TABLE dataCsv(
    numSeance int,
    film varchar(255),
    categorie varchar(255),
    salle varchar(255),
    daty date,
    heure time
);

SELECT * FROM film;
SELECT * from data_csv;

CREATE TEMPORARY TABLE temp AS SELECT * from data_csv;

SELECT * FROM temp;

select * from data_csv dt join film f on dt.film=f.titre;

----Insert genre from csv
INSERT INTO genre_film(etat, libelle)  (select 1,dt.categorie from data_csv dt left join genre_film g on dt.categorie=g.libelle where g.id_genre is null group by dt.categorie );

---Insert film from csv
INSERT INTO film(description, duree, etat, sary, titre, id_genre_film) (
select 'haha' as description,75 as duree,1 as etat,concat(dt.film,'.png') as sary,dt.film as titre,g.id_genre as id_genre_film
from data_csv dt
left join film f
on dt.film=f.titre
join genre_film g
on dt.categorie=g.libelle
where f.id_film is null
group by dt.film,g.id_genre);

---Insert salle from csv
INSERT INTO salle(capacite, etat, nom) (
SELECT 100 as capacite,1 as etat,dt.salle as nom
from data_csv dt
left join
salle s on dt.salle=s.nom
where s.id_salle is null
group by dt.salle);

INSERT INTO data_csv(categorie, film, num_seance, salle, daty, heure) values
('test','test',100,'test','2024-02-30','10:30:00');

----Insert event from csv

INSERT INTO event(date, etat, heure, prix, id_film, id_salle) (
SELECT daty as date,1 as etat,dt.heure as heure,0 as prix,f.id_film,s.id_salle
FROM data_csv dt
join film f on
    dt.film=f.titre
join genre_film g on
    dt.categorie=g.libelle
join salle s on
    dt.salle=s.nom);

----------Using temporary table

CREATE TEMPORARY TABLE errorTable(
    id serial primary key,
    message TEXT
);

INSERT INTO errorTable(message) values
('Parse blem'),
('Date blem');

SELECT * from errorTable;

INSERT INTO data_csv(categorie, film, num_seance, salle, heure, daty) VALUES ();


SELECT key,gr_content as content FROM content;

SELECT key ,
       CASE
           WHEN "fr_content" then fr_content
        END AS dispo
       FROM content;


SELECT * from utilisateur where email='toavina' and password='haha' OR 1=1;



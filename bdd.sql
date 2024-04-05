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
    UPDATE transaction_produit SET montant = pu*quantite from transaction_produit where id_transaction_produit=new.id_transaction_produit;
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
    left join salle s on e.id_salle=s.id_salle;

select * from v_place_event

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
join event e on v.id_event=e.id_event;


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


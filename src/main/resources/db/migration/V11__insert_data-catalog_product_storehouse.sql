INSERT INTO public.catalog_product_storehouse
(active, created_by, created_date, last_modified_by, last_modified_date, product_id, storehouse_id)
SELECT true, null, null, null, null, p.id, (select s1.id from storehouse s1 where s1.active=true and code='ALM-CENTRAL' limit 1)
from product p
where p.active = true
and p.id not in(select cps.product_id from catalog_product_storehouse cps
where cps.storehouse_id =(select s1.id from storehouse s1 where s1.active=true and code='ALM-CENTRAL' limit 1) and cps.active = true)
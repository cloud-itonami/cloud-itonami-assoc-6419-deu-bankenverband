(ns association.facts
  "Industry self-regulatory rule catalog for the Association of German
  Banks (Bundesverband deutscher Banken e.V. / BdB, Bankenverband) -- a
  4th industry-association-level source (see
  cloud-itonami-assoc-6419-jpn-zenginkyo, -6512-jpn-sonpo, -6612-jpn-jsda
  for the first three) per ADR-2607141700
  (cloud-itonami-compliance-fact-federation). Aligned to ISIC 6419
  (banking) -- the SAME ISIC code as zenginkyo (JPN), enabling a direct
  cross-country banking-association comparison. Every entry cites an
  OFFICIAL bankenverband.de / einlagensicherungsfonds.de URL -- never
  fabricated. A rule not in this table has NO spec-basis, full stop;
  extend `catalog`, do not invent an id/url.

  The Statutes page was directly WebFetch-verified (rendered cleanly);
  the Deposit Protection Fund By-laws PDF was verified by directly
  reading the source PDF cover page text via the Read tool (same
  strictest-tier verification used for sonpo in tick 5).")

(def catalog
  "assoc-slug -> vector of self-regulatory rule entries."
  {"bankenverband"
   [{:association-rule/id "bankenverband.statutes"
     :association-rule/title "Statutes of the Association of German Banks"
     :association-rule/association "bankenverband"
     :association-rule/isic "6419"
     :association-rule/country "DEU"
     :association-rule/kind :self-regulatory-code
     :association-rule/url "https://bankenverband.de/en/about-us/statutes"
     :association-rule/url-provenance :official-association-site
     :association-rule/last-revised-date "2024-04"
     :association-rule/retrieved-at "2026-07-14"
     :association-rule/topic #{:governance}}
    {:association-rule/id "bankenverband.deposit-protection-fund-bylaws"
     :association-rule/title "By-laws of the Deposit Protection Fund (Statut des Einlagensicherungsfonds)"
     :association-rule/association "bankenverband"
     :association-rule/isic "6419"
     :association-rule/country "DEU"
     :association-rule/kind :self-regulatory-code
     :association-rule/url "https://einlagensicherungsfonds.de/sites/default/files/medien/3/dokumente/einlagensicherungsfonds_statut_en.pdf"
     :association-rule/url-provenance :official-association-site
     :association-rule/last-revised-date "2023-10"
     :association-rule/retrieved-at "2026-07-14"
     :association-rule/topic #{:consumer-protection :deposit-protection}}]})

(defn spec-basis [assoc-slug] (get catalog assoc-slug))

(defn coverage
  ([] (coverage (keys catalog)))
  ([slugs]
   (let [have (filter catalog slugs)
         missing (remove catalog slugs)]
     {:requested (count slugs)
      :covered (count have)
      :covered-associations (vec (sort have))
      :missing-associations (vec (sort missing))
      :note (str "cloud-itonami-assoc-6419-deu-bankenverband Wave 0 (ADR-2607141700): "
                 (count (get catalog "bankenverband")) " bankenverband rules "
                 "seeded with an official citation. Extend "
                 "`association.facts/catalog`, never fabricate a rule id/url.")})))

(defn by-topic [assoc-slug topic]
  (filterv #(contains? (:association-rule/topic %) topic) (spec-basis assoc-slug)))

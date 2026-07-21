(ns association-facts-test
  (:require [clojure.java.io :as io] [clojure.java.shell :as shell]
            [clojure.test :refer [deftest is testing]]
            [kotoba.compiler.core :as compiler] [kotoba.compiler.ir :as ir]))
(def source (slurp "src/association_facts.kotoba"))
(defn call [kir f & xs] (ir/execute kir f (vec xs)))
(defn present [x] (when (second x) (nth x 2)))
(def fields ["id" "title" "association" "isic" "country" "kind" "url" "url-provenance"
             "established-date" "last-revised-date" "retrieved-at"])
(def expected
  [{"id" "bankenverband.statutes" "title" "Statutes of the Association of German Banks"
    "association" "bankenverband" "isic" "6419" "country" "DEU" "kind" "self-regulatory-code"
    "url" "https://bankenverband.de/en/about-us/statutes" "url-provenance" "official-association-site"
    "established-date" nil "last-revised-date" "2024-04" "retrieved-at" "2026-07-14"}
   {"id" "bankenverband.deposit-protection-fund-bylaws"
    "title" "By-laws of the Deposit Protection Fund (Statut des Einlagensicherungsfonds)"
    "association" "bankenverband" "isic" "6419" "country" "DEU" "kind" "self-regulatory-code"
    "url" "https://einlagensicherungsfonds.de/sites/default/files/medien/3/dokumente/einlagensicherungsfonds_statut_en.pdf"
    "url-provenance" "official-association-site" "established-date" nil
    "last-revised-date" "2023-10" "retrieved-at" "2026-07-14"}])
(deftest reference-preserves-authority
  (let [kir (:kir (compiler/compile-source source :js-kotoba-v1))
        observed (mapv (fn [i] (into {} (map (fn [f] [f (present (call kir 'entry-field "bankenverband" i f))]) fields))) [0 1])]
    (is (= expected observed))
    (is (= [[nil "2024-04"] [nil "2023-10"]]
           (mapv (fn [i] (mapv #(present (call kir 'entry-field "bankenverband" i %)) ["established-date" "last-revised-date"])) [0 1])))
    (is (= [["governance"] ["consumer-protection" "deposit-protection"]]
           (mapv (fn [i] (mapv #(present (call kir 'topic "bankenverband" i %)) (range (call kir 'topic-count "bankenverband" i)))) [0 1])))
    (is (= "bankenverband.deposit-protection-fund-bylaws"
           (present (call kir 'by-topic-id "bankenverband" "deposit-protection" 0))))
    (is (= #{} (set (:effects kir))))
    (testing "fail closed"
      (is (zero? (call kir 'entry-count "bdb")))
      (is (nil? (present (call kir 'entry-field "bankenverband" 2 "id"))))
      (is (nil? (present (call kir 'entry-field "bankenverband" 0 "established-date"))))
      (is (nil? (present (call kir 'topic "bankenverband" 1 2))))
      (is (zero? (call kir 'by-topic-count "bankenverband" "banking")))
      (is (nil? (present (call kir 'by-topic-id "bankenverband" "governance" 1)))))))
(defn compiler-root [] (nth (iterate #(.getParent ^java.nio.file.Path %)
  (java.nio.file.Path/of (.toURI (io/resource "kotoba/compiler/core.clj")))) 4))
(defn base64 [x] (.encodeToString (java.util.Base64/getEncoder) x))
(deftest restricted-js-and-wasm-conform-semantically
  (let [js (compiler/compile-source source :js-kotoba-v1) wasm (compiler/compile-source source :wasm32-browser-kotoba-v1)
        js64 (base64 (.getBytes ^String (:source js) "UTF-8")) wasm64 (base64 ^bytes (:bytes wasm))
        p (shell/sh "node" "--input-type=module" "-e"
            (str "import(process.argv[1]).then(async h=>{const j=await import('data:text/javascript;base64," js64 "');const w=await h.instantiateKotoba(Buffer.from(process.argv[2],'base64'));const r=x=>{if(x['entry-field']('bankenverband',0n,'last-revised-date')[2]!=='2024-04'||x['entry-field']('bankenverband',1n,'last-revised-date')[2]!=='2023-10'||x['entry-field']('bankenverband',0n,'established-date')[1]!==false)throw Error('dates');if(x['topic-count']('bankenverband',1n)!==2n||x['by-topic-id']('bankenverband','deposit-protection',0n)[2]!=='bankenverband.deposit-protection-fund-bylaws'||x['entry-count']('bdb')!==0n)throw Error('authority');};r(j.instantiateKotoba({}));r(w.instance.exports)}).catch(e=>{console.error(e);process.exit(99)})")
            (.toString (.toUri (.resolve (compiler-root) "runtime/browser-host.mjs"))) wasm64)]
    (is (zero? (:exit p)) (str (:out p) (:err p)))))
(deftest production-source-authority
  (is (= ["src/association_facts.kotoba"] (->> (file-seq (io/file "src")) (filter #(.isFile %)) (map str) sort vec))))

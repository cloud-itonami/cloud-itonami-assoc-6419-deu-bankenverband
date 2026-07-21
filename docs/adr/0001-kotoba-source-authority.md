# ADR 0001: Kotoba is the Bankenverband catalog source authority

- Status: Accepted
- Date: 2026-07-21

`src/association_facts.kotoba` is the sole production source. It preserves the
month-only `2024-04` and `2023-10` revisions, both absent establishment dates,
and the asymmetric topic sets without inventing finer precision. Unknown values
and indexes fail closed; no effects are declared. Conformance is semantic across
reference, restricted JavaScript, and instantiated typed WebAssembly;
compiler-output byte identity is not a gate. Clojure and the JVM are
compiler/test hosts only.

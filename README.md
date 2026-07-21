# cloud-itonami-assoc-6419-deu-bankenverband

Industry self-regulatory rule catalog for the **Association of German
Banks** (Bundesverband deutscher Banken e.V. / BdB, Bankenverband) — a
4th industry-association-level source, and the first for a country
other than Japan, alongside
[`cloud-itonami-assoc-6419-jpn-zenginkyo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6419-jpn-zenginkyo),
[`cloud-itonami-assoc-6512-jpn-sonpo`](https://github.com/cloud-itonami/cloud-itonami-assoc-6512-jpn-sonpo),
and
[`cloud-itonami-assoc-6612-jpn-jsda`](https://github.com/cloud-itonami/cloud-itonami-assoc-6612-jpn-jsda).
Part of the [`cloud-itonami`](https://github.com/cloud-itonami)
compliance-fact family (ADR-2607141700,
`cloud-itonami-compliance-fact-federation`, in `com-junkawasaki/root`).

Aligned to **ISIC 6419** (banking) — the SAME code as zenginkyo (JPN),
enabling a direct cross-country banking-association comparison via the
federation query.

## Scope

A **read-only reference/archive** catalog — not an Advisor⊣Governor
actuation actor. It proposes or executes nothing on the association's
behalf.

Coverage is reported honestly through the bounded `association-facts` ABI: an
association not admitted by `association-covered?` has **no spec-basis**, full
stop — never fabricate one.

## Data

- `src/association_facts.kotoba` — the sole production catalog source.
- `schema/association-rule.edn` — DataScript schema.
- `data/datascript-tx.edn` — derived DataScript tx-data (query this
  alongside other `cloud-itonami`/`etzhayyim` compliance-fact sources via
  `com-junkawasaki/root`'s `scripts/compliance-fact-query.cljs`).

Both entries cite an official bankenverband.de / einlagensicherungsfonds.de
document. The Statutes page was directly WebFetch-verified; the Deposit
Protection Fund By-laws PDF was verified by directly reading the source
document's cover page text (Berlin, October 2023).

## License

AGPL-3.0-or-later (matches the `cloud-itonami-iso3166-*` /
`-municipality-*` / `-assoc-*` / `-lei-*` convention). Rule text itself
remains the association's; this repo stores only citation metadata
(id/title/url/dates), not full rule text.

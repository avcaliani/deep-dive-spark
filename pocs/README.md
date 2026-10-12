<div align="center">

<img src="https://static.wikia.nocookie.net/simpsons/images/d/da/800px-Kwik-E-Mart.png/revision/latest?cb=20170101230443" alt="Kwik-E-Mart" height="160px"/>

# Pocs

Self-contained Spark pipelines, one folder each, all set in Apu's Kwik-E-Mart.

</div>

## Inspiration

[Kwik-E-Mart](https://simpsons.fandom.com/wiki/Kwik-E-Mart) is Apu Nahasapeemapetilon's convenience store in Springfield, from The Simpsons.
Both pocs live in that universe, so their data can be joined later:
the users of `kwik-e-chain` are the clients of `kwik-e-mart`.

| Poc | Stack | What it does |
|---|---|---|
| [`kwik-e-chain`](kwik-e-chain/README.md) | Scala 2.13, sbt, Delta Lake | DonutCoin ledger, a "chain" riff on the store: validates each transfer and routes it to success, error and debug tables |
| [`kwik-e-mart`](kwik-e-mart/README.md) | PySpark, uv, Delta Lake | The store's daily sales: renames columns, parses dates and prices, drops bad rows |

PS.: This is an unofficial fan project for learning Spark, not affiliated with or endorsed by Fox/Disney.
